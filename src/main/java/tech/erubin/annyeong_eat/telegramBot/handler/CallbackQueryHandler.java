package tech.erubin.annyeong_eat.telegramBot.handler;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.buttons.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.UserEnum;
import tech.erubin.annyeong_eat.telegramBot.module.OrderModule;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Handlers;

import java.util.List;

@Component
public class CallbackQueryHandler extends Handlers {
    private final AnnyeongEatWebHook webHook;
    private final OrderModule orderModule;
    private final InlineButtons inlineButtons;

    public CallbackQueryHandler(UserServiceImpl clientService, OrderServiceImpl orderService,
                                OrderStatesServiceImpl orderStatesService, DishServiceImpl dishService,
                                ChequeDishServiceImpl chequeService, UserStatesServiceImpl stateService,
                                CafeServiceImpl cafeService,
                                @Lazy AnnyeongEatWebHook webHook, OrderModule orderModule,
                                InlineButtons inlineButtons) {
        super(clientService, orderService, orderStatesService, dishService, chequeService,
                stateService, cafeService);
        this.webHook = webHook;
        this.orderModule = orderModule;
        this.inlineButtons = inlineButtons;
    }

    public BotApiMethod<?> handleUpdate(CallbackQuery callback) {
        String userId = callback.getFrom().getId().toString();
        String buttonDate = callback.getData();
        String chatId = callback.getMessage().getChatId().toString();
        String callbackText = error;

        User user = clientService.getUser(userId);
        UserState userState = stateService.getState(user);
        UserEnum userEnum = getClientState(userState);

        String id = getTag(buttonDate);
        Order order;
        OrderState orderState;

        int messageId = callback.getMessage().getMessageId();

        BotApiMethod<?> botApiMethod;
        InlineKeyboardMarkup inlineMarkup;
        if (userEnum == UserEnum.ORDER_CAFE_MENU) {
            order = orderService.getOrderById(user);
            Dish dish = dishService.getDishById(Integer.parseInt(id));
            ChequeDish chequeDish = chequeService.getChequeByOrderAndDish(order, dish);
            int count = chequeDish.getCountDishes();
            if (buttonDate.matches("\\d+[mb][+]")) {
                count++;
                callbackText = dish.getName() + " " + addDish;
            }
            else if (buttonDate.matches("\\d+[mb][-]")) {
                if (count >= 0) {
                    if (count > 0) {
                        count--;
                        callbackText = dish.getName() + " " + subDish;
                    } else {
                        callbackText = dish.getName() + " " + emptyDish;
                    }
                }
            }
            else if (buttonDate.matches("\\d+[mb]0")) {
                callbackText = count + " " + dish.getName() + " " + subDish;
                count = 0;
            }
            else if (buttonDate.matches("\\d+(in)")) {
                callbackText = messageInfo;
            }

            if (buttonDate.matches("\\d+")) {
                callbackText = getTextDish(dish);
                inlineMarkup = inlineButtons.clientCheque(id, chequeDish);
                if (!webHook.sendPhoto(chatId, callbackText, dish.getLinkPhoto(), inlineMarkup)) {
                    callbackText = error;
                }
            }
            else if (buttonDate.matches("\\d+m[+0-]")) {
                inlineMarkup = inlineButtons.clientCheque(id, chequeDish);
                EditMessageReplyMarkup editMessageReplyMarkup =
                        new EditMessageReplyMarkup(chatId, messageId, null, inlineMarkup);
                if (!webHook.updateMarkups(editMessageReplyMarkup)) {
                    if (buttonDate.matches("\\d+m0") && count == 0) {
                        callbackText = dish.getName() + " " + emptyDish;
                    }
                    else {
                        callbackText = error;
                    }
                }
            }
            else if (buttonDate.matches("\\d+b[+0-]")) {
                inlineMarkup = inlineButtons.getFullOrderButtons(order);
                String editText = orderModule.getFullOrder(order);
                EditMessageText editMessageText =
                        new EditMessageText(chatId, messageId, null, editText, null,
                                null, inlineMarkup, null);
                if (!webHook.updateText(editMessageText)) {
                    callbackText = error;
                }
            }

            saveOrDeleteChequeDish(chequeDish, count);
            botApiMethod = getAnswerCallbackQuery(callback, callbackText);
        }
        else if (userEnum == UserEnum.OPERATOR) {
            if (buttonDate.matches("\\d+(in)")) {
                botApiMethod = getAnswerCallbackQuery(callback, messageInfo);
            }
            else {
                order = orderService.getOrderById(id);
                int size = order.getOrderStateList().size() - 1;
                OrderEnum orderEnum = OrderEnum.GET.orderState(order.getOrderStateList().get(size).getState());
                String editText = orderModule.getFullOrder(order);
                if (orderEnum == OrderEnum.ORDER_END_REGISTRATION) {
                    if (buttonDate.matches("\\d+[o][+]")) {
                        orderState = orderStatesService.create(order, OrderEnum.ORDER_ACCEPT.getValue());
                        orderStatesService.save(orderState);
                        inlineMarkup = inlineButtons.getOperatorButtons(order);
                        botApiMethod = new EditMessageText(chatId, messageId, null, editText,
                                null, null, inlineMarkup, null);
                    } else if (buttonDate.matches("\\d+[o][-]")) {
                        orderState = orderStatesService.create(order, OrderEnum.ORDER_CANCEL.getValue());
                        orderStatesService.save(orderState);
                        inlineMarkup = inlineButtons.getOperatorButtons(order);
                        botApiMethod = new EditMessageText(chatId, messageId, null, editText,
                                null, null, inlineMarkup, null);
                    } else {
                        botApiMethod = getAnswerCallbackQuery(callback, error);
                    }
                }
                else if (orderEnum == OrderEnum.ORDER_ACCEPT || orderEnum == OrderEnum.ORDER_CANCEL) {
                    inlineMarkup = inlineButtons.getOperatorButtons(order);
                    botApiMethod = new EditMessageText(chatId, messageId, null, editText,
                            null, null, inlineMarkup, null);
                }
                else {
                    botApiMethod = getAnswerCallbackQuery(callback, notWork);
                }
            }
        }
        else {
            botApiMethod = getAnswerCallbackQuery(callback, error);
        }
        return botApiMethod;
    }

    private UserEnum getClientState(UserState userState) {
        List<String> cafeNameList = cafeService.getAllCafeNames();
        if (cafeNameList.contains(userState.getState())) {
            return UserEnum.ORDER_CAFE_MENU;
        }
        else {
            return UserEnum.GET.userState(userState.getState());
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
