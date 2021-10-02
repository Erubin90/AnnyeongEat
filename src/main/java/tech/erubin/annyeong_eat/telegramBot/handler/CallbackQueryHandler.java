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
    private final UserServiceImpl clientService;
    private final EmployeeServiceImpl employeeService;
    private final OrderServiceImpl orderService;
    private final OrderStatesServiceImpl orderStatesService;
    private final DishServiceImpl dishService;
    private final ChequeDishServiceImpl chequeService;
    private final UserStatesServiceImpl stateService;
    private final CafeServiceImpl cafeService;
    private final InlineButtons inlineButtons;

    private final OrderModule orderModule;

    public CallbackQueryHandler(@Lazy AnnyeongEatWebHook webHook, UserServiceImpl clientService,
                                EmployeeServiceImpl employeeService, OrderServiceImpl orderService, OrderStatesServiceImpl orderStatesService,
                                DishServiceImpl dishService, ChequeDishServiceImpl chequeService,
                                UserStatesServiceImpl stateService, CafeServiceImpl cafeService,
                                InlineButtons inlineButtons, OrderModule orderModule) {
        this.webHook = webHook;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.orderService = orderService;
        this.orderStatesService = orderStatesService;
        this.dishService = dishService;
        this.chequeService = chequeService;
        this.stateService = stateService;
        this.cafeService = cafeService;
        this.inlineButtons = inlineButtons;
        this.orderModule = orderModule;
    }

    public BotApiMethod<?> handleUpdate(CallbackQuery callback) {
        String userId = callback.getFrom().getId().toString();
        String chatId = callback.getMessage().getChatId().toString();
        int messageId = callback.getMessage().getMessageId();
        String[] idList = callback.getData().split("/");

        Order order = orderService.getOrderByStringId(idList[0]);
        Dish dish = dishService.getDishById(idList[1]);
        User user = clientService.getUser(userId);
        UserState userState = stateService.getState(user);
        EmployeeEnum employeeEnum = EmployeeEnum.GET.department(userState.getState());
        String tag = idList[2];

        System.out.println(tag);
        if (tag.equals(tagInfo)) {
            return answerCallbackQuery(callback, messageInfo);
        }
        if (employeeEnum != null) {
            return employeeCallback(callback, order, dish, employeeEnum, chatId, messageId, tag);
        }
        else {
            ClientEnum clientEnum = getClientState(userState);
            return clientCallback(callback, order, dish, clientEnum, chatId, messageId, tag);

        }
    }

    private BotApiMethod<?> employeeCallback(CallbackQuery callback, Order order, Dish dish,
                                             EmployeeEnum employeeEnum, String chatId, int messageId, String tag) {
        BotApiMethod<?> botApiMethod;
        switch (employeeEnum) {
            case OPERATOR:
                botApiMethod = operator(callback, order, dish, chatId, messageId, tag);
                break;
            case ADMINISTRATOR:
                botApiMethod = administrator(callback, order, dish, chatId, messageId, tag);
                break;
            case COURIER:
            default:
                botApiMethod = answerCallbackQuery(callback, buttonNotWork);
        }
        return botApiMethod;
    }

    private BotApiMethod<?> operator(CallbackQuery callback, Order order, Dish dish,
                                     String chatId, int messageId, String tag) {
        BotApiMethod<?> botApiMethod;
        InlineKeyboardMarkup inlineMarkup;
        OrderState orderState;
        String text;
        int size = order.getOrderStateList().size() - 1;
        OrderEnum orderEnum = OrderEnum.GET.orderState(order.getOrderStateList().get(size).getState());

        switch (orderEnum) {
            case ORDER_END_REGISTRATION:
                text = orderModule.getChequeText(order, true);
                if (tag.equals("o+")) {
                    orderState = orderStatesService.create(order, OrderEnum.ORDER_ACCEPT.getValue());
                    orderStatesService.save(orderState);
                    inlineMarkup = inlineButtons.orderAcceptButtons();
                    botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                }
                else if (tag.equals("o-")) {
                    orderState = orderStatesService.create(order, OrderEnum.ORDER_CANCEL.getValue());
                    orderStatesService.save(orderState);
                    inlineMarkup = inlineButtons.orderCancelButtons();
                    botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                }
                else if (tag.equals("oe")) {
                    orderState = orderStatesService.create(order, OrderEnum.ORDER_EDITING.getValue());
                    orderStatesService.save(orderState);
                    inlineMarkup = inlineButtons.orderEditButtons(order);
                    botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                }
                else {
                    botApiMethod = answerCallbackQuery(callback, error);
                }
                break;
            case ORDER_EDITING:
                List<String> typeDishList = inlineButtons.typeDishesInCafe(order);
                if (tag.matches("o.")) {
                    text = orderModule.getChequeText(order, true);
                    if ("o+".equals(tag)) {
                        orderState = orderStatesService.create(order, OrderEnum.ORDER_ACCEPT.getValue());
                        orderStatesService.save(orderState);
                        inlineMarkup = inlineButtons.orderAcceptButtons();
                        sendMessageEmployee(order, EmployeeEnum.COURIER);
                        botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                    }
                    else if ("o-".equals(tag)) {
                        orderState = orderStatesService.create(order, OrderEnum.ORDER_CANCEL.getValue());
                        orderStatesService.save(orderState);
                        inlineMarkup = inlineButtons.orderCancelButtons();
                        botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                    }
                    else if ("oe".equals(tag)) {
                        inlineMarkup = inlineButtons.orderEditButtons(order);
                        botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                    }
                    else if ("or".equals(tag)) {
                        inlineMarkup = inlineButtons.orderAndRegistrationButtons(order);
                        botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                    }
                    else {
                        botApiMethod = answerCallbackQuery(callback, error);
                    }
                }
                else if (tag.matches("r.")) {
                    ChequeDish chequeDish = chequeService.getChequeByOrderAndDish(order, dish);
                    int count = chequeDish.getCountDishes();
                    switch (tag) {
                        case "rx":
                            count = 0;
                            saveOrDeleteChequeDish(chequeDish, count);
                            text = orderModule.getChequeText(order, true);
                            inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                            botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                            break;
                        case "r+":
                            count++;
                            saveOrDeleteChequeDish(chequeDish, count);
                            text = orderModule.getChequeText(order, true);
                            inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                            botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                            break;
                        case "r-":
                            if (count > 0) {
                                count--;
                            saveOrDeleteChequeDish(chequeDish, count);
                            text = orderModule.getChequeText(order, true);
                            inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                            botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                            break;
                        }
                        default:
                            botApiMethod = answerCallbackQuery(callback, error);
                            break;
                    }
                }
                else if (tag.matches("e.+")) {
                    if ("ead".equals(tag)) {
                        inlineMarkup = inlineButtons.orderAddDishButtons(order);
                        botApiMethod = editMessageReplyMarkup(chatId, messageId, inlineMarkup);
                    }
                    else if ("ec".equals(tag)) {
                        inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                        botApiMethod = editMessageReplyMarkup(chatId, messageId, inlineMarkup);
                    }
                    else if ("eb".equals(tag)) {
                        inlineMarkup = inlineButtons.orderEditButtons(order);
                        botApiMethod = editMessageReplyMarkup(chatId, messageId, inlineMarkup);
                    }
                    else if ("er".equals(tag)) {
                        text = orderModule.getChequeText(order, true);
                        inlineMarkup = inlineButtons.orderEditButtons(order);
                        botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                    }
                    else {
                        botApiMethod = answerCallbackQuery(callback, error);
                    }
                }
                else if (typeDishList.contains(tag)) {
                    List<Dish> dishList = dishService.getDishListByType(tag);
                    inlineMarkup = inlineButtons.orderTypeDishesButtons(order, dishList);
                    botApiMethod = editMessageReplyMarkup(chatId, messageId, inlineMarkup);
                }
                else if (tag.equals(String.valueOf(dish.getId()))) {
                    ChequeDish chequeDish = chequeService.getChequeByOrderAndDish(order, dish);
                    chequeDish.setCountDishes(1);
                    chequeService.save(chequeDish);
                    text = orderModule.getChequeText(order, true);
                    List<Dish> dishList = dishService.getDishListByType(dish.getType());
                    inlineMarkup = inlineButtons.orderTypeDishesButtons(order, dishList);
                    botApiMethod = editMessageText(chatId, messageId, text, inlineMarkup);
                }
                else {
                    botApiMethod = answerCallbackQuery(callback, error);
                }
                break;
            case ORDER_ACCEPT:
                botApiMethod = answerCallbackQuery(callback, inlineButtons.getAccept());
                break;
            case ORDER_CANCEL:
                botApiMethod = answerCallbackQuery(callback, inlineButtons.getCancel());
                break;
            default:
                botApiMethod = answerCallbackQuery(callback, notWork);
        }
        return botApiMethod;
    }

    private BotApiMethod<?> administrator(CallbackQuery callback, Order order, Dish dish,
                                     String chatId, int messageId, String tag) {

        return null;
    }

    private BotApiMethod<?> clientCallback(CallbackQuery callback, Order order, Dish dish,
                                           ClientEnum clientEnum, String chatId, int messageId, String tag) {
        BotApiMethod<?> botApiMethod;
        InlineKeyboardMarkup inlineMarkup;
        String callbackText = error;

        if (clientEnum == ClientEnum.ORDER_CAFE_MENU) {
            ChequeDish chequeDish = chequeService.getChequeByOrderAndDish(order, dish);
            int count = chequeDish.getCountDishes();
            if (tag.equals("m+") || tag.equals("b+")) {
                count++;
                callbackText = dish.getName() + " " + addDish;
            }
            else if (tag.equals("m-") || tag.equals("b-")) {
                if (count >= 0) {
                    if (count > 0) {
                        count--;
                        callbackText = dish.getName() + " " + subDish;
                    }
                    else {
                        callbackText = dish.getName() + " " + emptyDish;
                    }
                }
            }
            else if (tag.equals("mx") || tag.equals("bx")) {
                callbackText = count + " " + dish.getName() + " " + subDish;
                count = 0;
            }
            saveOrDeleteChequeDish(chequeDish, count);
            if (tag.equals(String.valueOf(dish.getId()))) {
                callbackText = getDishText(dish);
                inlineMarkup = inlineButtons.clientCheque(chequeDish);
                if (!webHook.sendPhoto(chatId, callbackText, dish.getLinkPhoto(), inlineMarkup)) {
                    callbackText = error;
                }
            }
            else if (tag.equals("m+") || tag.equals("mx") || tag.equals("m-")) {
                inlineMarkup = inlineButtons.clientCheque(chequeDish);
                EditMessageReplyMarkup editMessageReplyMarkup = editMessageReplyMarkup(chatId, messageId, inlineMarkup);
                if (!webHook.updateMarkups(editMessageReplyMarkup, count)) {
                    callbackText = error;
                }
            }
            else if (tag.equals("b+") || tag.equals("bx") || tag.equals("b-")) {
                inlineMarkup = inlineButtons.fullOrderButtons(order);
                String editText = orderModule.getChequeText(order, false);
                EditMessageText editMessageText = editMessageText(chatId, messageId, editText, inlineMarkup);
                if (!webHook.updateText(editMessageText)) {
                    callbackText = error;
                }
            }
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

    private EditMessageText editMessageText(String chatId, int messageId, String text, InlineKeyboardMarkup inlineMarkup) {
        return new EditMessageText(chatId, messageId, null, text,
                null, null, inlineMarkup, null);
    }

    private EditMessageReplyMarkup editMessageReplyMarkup(String chatId, int messageId, InlineKeyboardMarkup inlineMarkup) {
        return new EditMessageReplyMarkup(chatId, messageId, null, inlineMarkup);
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

    private String getDishText(Dish dish) {
        String dishName = dish.getName();
        double dishPrice = dish.getPrice();
        String dishComment = dish.getComment();
        return String.format("%s %s₽\n%s", dishName, dishPrice, dishComment);
    }

    private void sendMessageEmployee(Order order, EmployeeEnum employeeEnum) {
        Cafe cafe = order.getCafeId();
        List<Employee> listOperatorsInCafe = employeeService.getEmployeeByCafeIdAndDepartmenName(cafe, employeeEnum.getValue());
        if (listOperatorsInCafe != null) {
            webHook.sendMessageDepartment(listOperatorsInCafe, employeeEnum, orderModule.getChequeText(order, true), order);
        }
    }
}