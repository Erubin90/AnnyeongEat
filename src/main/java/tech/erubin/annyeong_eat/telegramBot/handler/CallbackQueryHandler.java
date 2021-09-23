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
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;
import tech.erubin.annyeong_eat.telegramBot.module.OrderModule;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Handlers;

import java.util.List;

@Component
public class CallbackQueryHandler extends Handlers {
    private final AnnyeongEatWebHook webHook;
    private final OrderModule orderModule;
    private final UserServiceImpl clientService;
    private final OrderServiceImpl orderService;
    private final OrderStatesServiceImpl orderStatesService;
    private final DishServiceImpl dishService;
    private final ChequeDishServiceImpl chequeService;
    private final UserStatesServiceImpl stateService;
    private final CafeServiceImpl cafeService;
    private final InlineButtons inlineButtons;

    public CallbackQueryHandler(@Lazy AnnyeongEatWebHook webHook, OrderModule orderModule, UserServiceImpl clientService,
                                OrderServiceImpl orderService, OrderStatesServiceImpl orderStatesService,
                                DishServiceImpl dishService, ChequeDishServiceImpl chequeService,
                                UserStatesServiceImpl stateService, CafeServiceImpl cafeService,
                                InlineButtons inlineButtons) {
        this.webHook = webHook;
        this.orderModule = orderModule;
        this.clientService = clientService;
        this.orderService = orderService;
        this.orderStatesService = orderStatesService;
        this.dishService = dishService;
        this.chequeService = chequeService;
        this.stateService = stateService;
        this.cafeService = cafeService;
        this.inlineButtons = inlineButtons;
    }

    public BotApiMethod<?> handleUpdate(CallbackQuery callback) {
        String userId = callback.getFrom().getId().toString();
        String chatId = callback.getMessage().getChatId().toString();
        int messageId = callback.getMessage().getMessageId();
        String buttonDate = callback.getData();
        String elementId = getTag(buttonDate);

        User user = clientService.getUser(userId);
        UserState userState = stateService.getState(user);
        EmployeeEnum employeeEnum = EmployeeEnum.GET.department(userState.getState());

        if (employeeEnum != null) {
            return employeeCallback(callback, employeeEnum, buttonDate, chatId, messageId, elementId);
        }
        else {
            ClientEnum clientEnum = getClientState(userState);
            return clientCallback(callback, user, clientEnum, buttonDate, chatId, messageId, elementId);

        }
    }

    private BotApiMethod<?> employeeCallback(CallbackQuery callback, EmployeeEnum employeeEnum, String buttonDate,
                                             String chatId, int messageId, String elementId) {
        BotApiMethod<?> botApiMethod;
        InlineKeyboardMarkup inlineMarkup;
        Order order;
        OrderState orderState;

        if (employeeEnum == EmployeeEnum.OPERATOR) {
            if (buttonDate.matches("\\d+(in)")) {
                botApiMethod = answerCallbackQuery(callback, messageInfo);
            }
            else {
                order = orderService.getOrderById(elementId);
                int size = order.getOrderStateList().size() - 1;
                OrderEnum orderEnum = OrderEnum.GET.orderState(order.getOrderStateList().get(size).getState());
                String editText = orderModule.getFullOrder(order);
                if (orderEnum == OrderEnum.ORDER_END_REGISTRATION) {
                    if (buttonDate.matches("\\d+[o][+]")) {
                        orderState = orderStatesService.create(order, OrderEnum.ORDER_ACCEPT.getValue());
                        orderStatesService.save(orderState);
                        inlineMarkup = inlineButtons.getOperatorButtons(order);
                        botApiMethod = editMessageText(chatId, messageId, editText, inlineMarkup);
                    }
                    else if (buttonDate.matches("\\d+[o][-]")) {
                        orderState = orderStatesService.create(order, OrderEnum.ORDER_CANCEL.getValue());
                        orderStatesService.save(orderState);
                        inlineMarkup = inlineButtons.getOperatorButtons(order);
                        botApiMethod = editMessageText(chatId, messageId, editText, inlineMarkup);
                    }
                    else {
                        botApiMethod = answerCallbackQuery(callback, error);
                    }
                }
                else if (orderEnum == OrderEnum.ORDER_ACCEPT || orderEnum == OrderEnum.ORDER_CANCEL) {
                    inlineMarkup = inlineButtons.getOperatorButtons(order);
                    botApiMethod = editMessageText(chatId, messageId, editText, inlineMarkup);
                }
                else {
                    botApiMethod = answerCallbackQuery(callback, notWork);
                }
            }
        }
        else {
            botApiMethod = answerCallbackQuery(callback, buttonNotWork);
        }
        return botApiMethod;
    }

    private BotApiMethod<?> clientCallback(CallbackQuery callback, User user, ClientEnum clientEnum, String buttonDate,
                                           String chatId, int messageId, String elementId) {
        String callbackText = error;
        BotApiMethod<?> botApiMethod;
        InlineKeyboardMarkup inlineMarkup;
        Order order;

        if (clientEnum == ClientEnum.ORDER_CAFE_MENU) {
            order = orderService.getOrderById(user);
            Dish dish = dishService.getDishById(Integer.parseInt(elementId));
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
                inlineMarkup = inlineButtons.clientCheque(elementId, chequeDish);
                if (!webHook.sendPhoto(chatId, callbackText, dish.getLinkPhoto(), inlineMarkup)) {
                    callbackText = error;
                }
            }
            else if (buttonDate.matches("\\d+m[+0-]")) {
                inlineMarkup = inlineButtons.clientCheque(elementId, chequeDish);
                EditMessageReplyMarkup editMessageReplyMarkup = editMessageReplyMarkup(chatId, messageId, inlineMarkup);
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
                EditMessageText editMessageText = editMessageText(chatId, messageId, editText, inlineMarkup);
                if (!webHook.updateText(editMessageText)) {
                    callbackText = error;
                }
            }
            saveOrDeleteChequeDish(chequeDish, count);
            botApiMethod = answerCallbackQuery(callback, callbackText);
        }
        else {
            botApiMethod = answerCallbackQuery(callback, buttonNotWork);
        }
        return botApiMethod;
    }

    private ClientEnum getClientState(UserState userState) {
        List<String> cafeNameList = cafeService.getAllCafeNames();
        if (cafeNameList.contains(userState.getState())) {
            return ClientEnum.ORDER_CAFE_MENU;
        }
        else {
            return ClientEnum.GET.userState(userState.getState());
        }
    }

    private AnswerCallbackQuery answerCallbackQuery(CallbackQuery callback, String text) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callback.getId());
        answerCallbackQuery.setText(text);
        answerCallbackQuery.setShowAlert(false);
        return answerCallbackQuery;
    }

    private EditMessageText editMessageText(String chatId, int messageId, String text,
                                            InlineKeyboardMarkup inlineMarkup) {
        return new EditMessageText(chatId, messageId, null, text,
                null, null, inlineMarkup, null);
    }

    private EditMessageReplyMarkup editMessageReplyMarkup(String chatId, int messageId,
                                                          InlineKeyboardMarkup inlineMarkup) {
        return new EditMessageReplyMarkup(chatId, messageId, null, inlineMarkup);
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

    public String getTextDish(Dish dish) {
        String dishName = dish.getName();
        double dishPrice = dish.getPrice();
        String dishComment = dish.getComment();
        return String.format("%s %s₽\n%s", dishName, dishPrice, dishComment);
    }
}
