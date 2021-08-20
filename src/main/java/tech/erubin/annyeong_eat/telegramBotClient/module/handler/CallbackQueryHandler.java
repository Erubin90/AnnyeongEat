package tech.erubin.annyeong_eat.telegramBotClient.module.handler;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tech.erubin.annyeong_eat.telegramBotClient.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBotClient.entity.*;
import tech.erubin.annyeong_eat.telegramBotClient.module.InlineButtons;
import tech.erubin.annyeong_eat.telegramBotClient.service.*;
import tech.erubin.annyeong_eat.telegramBotClient.states.ClientStateEnum;

import java.util.List;

@Component
public class CallbackQueryHandler {
    private final ClientServiceImpl clientService;
    private final OrderServiceImpl orderService;
    private final DishServiceImpl dishService;
    private final ChequeDishServiceImpl chequeService;
    private final ClientStatesServiceImpl stateService;
    private final CafeServiceImpl cafeService;

    private final InlineButtons inlineButtons;
    private final HandlersTextMessage textMessage;

    private final AnnyeongEatWebHook webHook;

    public CallbackQueryHandler(ClientServiceImpl clientService, OrderServiceImpl orderService,
                                DishServiceImpl dishService, ChequeDishServiceImpl chequeService,
                                ClientStatesServiceImpl stateService, CafeServiceImpl cafeService,
                                InlineButtons inlineButtons, HandlersTextMessage textMessage,
                                @Lazy AnnyeongEatWebHook webHook) {
        this.clientService = clientService;
        this.orderService = orderService;
        this.dishService = dishService;
        this.chequeService = chequeService;
        this.stateService = stateService;
        this.cafeService = cafeService;
        this.inlineButtons = inlineButtons;
        this.textMessage = textMessage;
        this.webHook = webHook;
    }

    public BotApiMethod<?> handleUpdate(CallbackQuery callback) {
        BotApiMethod<?> botApiMethod = new SendMessage();
        String userId = callback.getFrom().getId().toString();
        Client client = clientService.getClientByTelegramUserId(userId);
        ClientState clientState = stateService.getState(client);
        ClientStateEnum clientStateEnum = getClientState(clientState);
        String buttonDate = callback.getData();
        String chatId = callback.getMessage().getChatId().toString();
        if (clientStateEnum == ClientStateEnum.ORDER_CAFE_MENU) {
            String id = getTag(buttonDate);
            Dish dish = dishService.getDishById(Integer.getInteger(id));
            ChequeDish chequeDish = getChequeDish(client, dish);
            InlineKeyboardMarkup inlineMarkup;
            String text = textMessage.getError();

            if (buttonDate.matches("\\d+[bm].")) {
                int count = chequeDish.getCountDishes();
                int messageId = callback.getMessage().getMessageId();
                if (buttonDate.matches("\\d+[bm][+]")) {
                    count++;
                    text = dish.getName() + " " + textMessage.getAddDish();
                } else if (buttonDate.matches("\\d+[bm][-]")) {
                    if (count >= 0) {
                        if (count > 0) {
                            count--;
                            text = dish.getName() + " " + textMessage.getSubDish();
                        } else {
                            text = dish.getName() + " " + textMessage.getEmptyDish();
                        }
                    }
                } else if (buttonDate.matches("\\d+[bm]0")) {
                    text = count + " " + dish.getName() + " " + textMessage.getSubDish();
                    count = 0;
                }
                chequeDish.setCountDishes(count);
                if (count > 0) {
                    chequeService.save(chequeDish);
                } else {
                    chequeService.delete(chequeDish);
                }
                inlineMarkup = inlineButtons.clientCheque(id, chequeDish);
                botApiMethod = getAnswerCallbackQuery(callback, text);
                if (buttonDate.matches("\\d+m[+-]")) {
                    EditMessageReplyMarkup editMessageReplyMarkup =
                            new EditMessageReplyMarkup(chatId, messageId, null, inlineMarkup);
                    if (!webHook.updateMarkups(editMessageReplyMarkup)) {
                        botApiMethod = getAnswerCallbackQuery(callback, textMessage.getError());
                    }
                }
            } else if (buttonDate.matches("\\d+")) {
                text = getTextDish(dish);
                String url = dish.getLinkPhoto();
                inlineMarkup = inlineButtons.clientCheque(id, chequeDish);
                if (!webHook.sendPhoto(chatId, text, url, inlineMarkup)) {
                    botApiMethod = getAnswerCallbackQuery(callback, textMessage.getError());
                }
            }
        }
        else {
            botApiMethod = getAnswerCallbackQuery(callback, textMessage.getError());
        }
        return botApiMethod;
    }

    private ClientStateEnum getClientState(ClientState clientState) {
        List<String> cafeNameList = cafeService.getAllCafeNames();
        if (cafeNameList.contains(clientState.getState())) {
            return ClientStateEnum.ORDER_CAFE_MENU;
        }
        else {
            return ClientStateEnum.GET.clientState(clientState.getState());
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

    private ChequeDish getChequeDish(Client client, Dish dish) {
        Order order = orderService.getOrder(client);
        return chequeService.getChequeByOrderAndDish(order, dish);
    }

    private String getTextDish(Dish dish) {
        String dishName = dish.getName();
        double dishPrice = dish.getPrice();
        String dishComment = dish.getComment();
        return String.format("%s %s₽\n%s", dishName, dishPrice, dishComment);
    }
}
