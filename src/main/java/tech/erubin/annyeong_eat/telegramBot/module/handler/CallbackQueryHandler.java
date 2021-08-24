package tech.erubin.annyeong_eat.telegramBot.module.handler;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.telegramBot.module.buttons.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.module.order.OrderTextMessage;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.states.UserStateEnum;

import java.util.List;

@Component
public class CallbackQueryHandler {
    private final UserServiceImpl clientService;
    private final OrderServiceImpl orderService;
    private final DishServiceImpl dishService;
    private final ChequeDishServiceImpl chequeService;
    private final UserStatesServiceImpl stateService;
    private final CafeServiceImpl cafeService;

    private final InlineButtons inlineButtons;
    private final HandlersTextMessage handlersTextMessage;
    private final OrderTextMessage orderTextMessage;

    private final AnnyeongEatWebHook webHook;

    public CallbackQueryHandler(UserServiceImpl clientService, OrderServiceImpl orderService,
                                DishServiceImpl dishService, ChequeDishServiceImpl chequeService,
                                UserStatesServiceImpl stateService, CafeServiceImpl cafeService,
                                InlineButtons inlineButtons, HandlersTextMessage handlersTextMessage,
                                OrderTextMessage orderTextMessage, @Lazy AnnyeongEatWebHook webHook) {
        this.clientService = clientService;
        this.orderService = orderService;
        this.dishService = dishService;
        this.chequeService = chequeService;
        this.stateService = stateService;
        this.cafeService = cafeService;
        this.inlineButtons = inlineButtons;
        this.handlersTextMessage = handlersTextMessage;
        this.orderTextMessage = orderTextMessage;
        this.webHook = webHook;
    }

    public BotApiMethod<?> handleUpdate(CallbackQuery callback) {
        BotApiMethod<?> botApiMethod;
        String userId = callback.getFrom().getId().toString();
        String userName = callback.getFrom().getUserName();
        User user = clientService.getClient(userId, userName);
        UserState userState = stateService.getState(user);
        UserStateEnum userStateEnum = getClientState(userState);
        String buttonDate = callback.getData();
        String chatId = callback.getMessage().getChatId().toString();

        if (userStateEnum == UserStateEnum.ORDER_CAFE_MENU) {
            String id = getTag(buttonDate);
            Dish dish = dishService.getDishById(Integer.parseInt(id));
            Order order = orderService.getOrder(user);
            ChequeDish chequeDish = chequeService.getChequeByOrderAndDish(order, dish);
            String callbackText = handlersTextMessage.getError();
            InlineKeyboardMarkup inlineMarkup;
            int count = chequeDish.getCountDishes();
            int messageId = callback.getMessage().getMessageId();
            if (buttonDate.matches("\\d+[mb][+]")) {
                count++;
                callbackText = dish.getName() + " " + handlersTextMessage.getAddDish();
            }
            else if (buttonDate.matches("\\d+[mb][-]")) {
                if (count >= 0) {
                    if (count > 0) {
                        count--;
                        callbackText = dish.getName() + " " + handlersTextMessage.getSubDish();
                    } else {
                        callbackText = dish.getName() + " " + handlersTextMessage.getEmptyDish();
                    }
                }
            }
            else if (buttonDate.matches("\\d+[mb]0")) {
                callbackText = count + " " + dish.getName() + " " + handlersTextMessage.getSubDish();
                count = 0;
            }
            else if (buttonDate.matches("\\d+[mb]=")) {
                callbackText = handlersTextMessage.getNotWork();
            }
            else if (buttonDate.matches("\\d+")) {
                callbackText = handlersTextMessage.getTextDish(dish);
                inlineMarkup = inlineButtons.clientCheque(id, chequeDish);
                if (!webHook.sendPhoto(chatId, callbackText, dish.getLinkPhoto(), inlineMarkup)) {
                    return getAnswerCallbackQuery(callback, handlersTextMessage.getError());
                }
            }
            saveOrDeleteChequeDish(chequeDish, count);
            botApiMethod = getAnswerCallbackQuery(callback, callbackText);
            if (buttonDate.matches("\\d+m[+0-]")) {
                inlineMarkup = inlineButtons.clientCheque(id, chequeDish);
                EditMessageReplyMarkup editMessageReplyMarkup =
                        new EditMessageReplyMarkup(chatId, messageId, null, inlineMarkup);
                if (!webHook.updateMarkups(editMessageReplyMarkup)) {
                    botApiMethod = getAnswerCallbackQuery(callback, handlersTextMessage.getError());
                }
            }
            else if (buttonDate.matches("\\d+b[+0-]")) {
                inlineMarkup = inlineButtons.getFullOrder(order);
                String editText = orderTextMessage.getFullOrder(order);
                EditMessageText editMessageText =
                        new EditMessageText(chatId, messageId, null, editText, null,
                                null, inlineMarkup, null);
                if (!webHook.updateText(editMessageText)) {
                    botApiMethod = getAnswerCallbackQuery(callback, handlersTextMessage.getError());
                }
            }
        }
        else {
            System.err.println("else");
            botApiMethod = getAnswerCallbackQuery(callback, handlersTextMessage.getError());
        }
        return botApiMethod;
    }

    private UserStateEnum getClientState(UserState userState) {
        List<String> cafeNameList = cafeService.getAllCafeNames();
        if (cafeNameList.contains(userState.getState())) {
            return UserStateEnum.ORDER_CAFE_MENU;
        }
        else {
            return UserStateEnum.GET.clientState(userState.getState());
        }
    }

    private AnswerCallbackQuery getAnswerCallbackQuery(CallbackQuery callback, String text) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callback.getId());
        answerCallbackQuery.setText(text);
        answerCallbackQuery.setShowAlert(false);
        return answerCallbackQuery;
    }

    private String getTag(String buttonDate) {
        if (buttonDate.matches("\\d+")) {
            return buttonDate;
        }
        else {
            return buttonDate.substring(0, buttonDate.length() - 2);
        }
    }

    private void saveOrDeleteChequeDish(ChequeDish chequeDish, int count) {
        chequeDish.setCountDishes(count);
        if (count > 0) {
            chequeService.save(chequeDish);
        }
        else {
            chequeService.delete(chequeDish);
        }
    }
}
