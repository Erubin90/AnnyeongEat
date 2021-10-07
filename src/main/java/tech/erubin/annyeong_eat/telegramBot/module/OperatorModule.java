package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.buttons.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.DepartmentEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OperatorModule extends Module {
    private final ReplyButtons replyButtons;
    private final InlineButtons inlineButtons;
    private final ChequeDishServiceImpl chequeDishService;
    private final DishServiceImpl dishService;

    public OperatorModule(OrderServiceImpl orderService, UserServiceImpl userService,
                          UserStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                          EmployeeServiceImpl employeeService, @Lazy AnnyeongEatWebHook webHook,
                          ReplyButtons replyButtons, InlineButtons inlineButtons, ChequeDishServiceImpl chequeDishService, DishServiceImpl dishService) {
        super(orderService, userService, userStatesService, orderStatesService, employeeService, webHook);
        this.replyButtons = replyButtons;
        this.inlineButtons = inlineButtons;
        this.chequeDishService = chequeDishService;
        this.dishService = dishService;
    }

    public SendMessage mainMenu(Update update, User user, String soursText) {
        String text;
        if (soursText.indexOf("Заказ:") == 0) {
            text = getTextEditingOrder(soursText);
        }
        else if (soursText.equals(replyButtons.getForm())) {
            text = getForm();
        }
        else if (soursText.equals(replyButtons.getCreateOrder())) {
            text = replyButtons.getCreateOrder();
        }
        else {
            text = noCommand;
        }
        ReplyKeyboard replyKeyboard = replyButtons.employeeOperator();
        return message(update, replyKeyboard, text, null);
    }

    private String getTextEditingOrder(String sourceText) {
        Map<String, String> map = getMap(sourceText);
        Order order = orderService.getOrderByOrderName(map.get("Заказ"));
        String text;
        if (order != null) {
            OrderState orderState = order.getOrderStateList().get(order.getOrderStateList().size() - 1);
            if (OrderEnum.GET.isOrderEditing(orderState)) {
                String address = map.get("Адрес");
                String priceDelivery = map.get("Сумма доставки");
                String paymentMethod = map.get("Способ оплаты");
                String comment = map.get("Комментарий");

                StringBuilder builder = new StringBuilder();
                builder.append("В заказе \"")
                        .append(order.getOrderName())
                        .append("\" изменен(ы):");

                if ((address != null) && !address.isBlank()) {
                    order.setAddress(address);
                    builder.append("\n- адрес");
                }
                if ((priceDelivery != null) && priceDelivery.matches("\\d{1,4}")) {
                    order.setPriceDelivery(Integer.parseInt(priceDelivery));
                    builder.append("\n- сумма доставки");
                }
                if ((paymentMethod != null) && !paymentMethod.isBlank()) {
                    order.setPaymentMethod(paymentMethod);
                    builder.append("\n- способ оплаты");
                }
                if ((comment != null) && !comment.isBlank()) {
                    order.setComment(comment);
                    builder.append("\n- комментарий");
                }
                orderService.save(order);
                sendMessageDepartment(order, DepartmentEnum.OPERATOR);
                text = builder.toString();
            }
            else {
                text = noEditingOrder;
            }
        }
        else {
            text = noOrderName;
        }
        return text;
    }

    private Map<String, String> getMap (String sourceText) {
        String[] textArray = sourceText.split("\n");
        Map<String, String> map = new HashMap<>();
        for (String rows : textArray) {
            String[] kayValue = rows.split(":[ ]*");
            if (kayValue.length > 1) {
                if (rows.matches("Заказ:.+") ||
                        rows.matches("Адрес:.+") ||
                        rows.matches("Сумма доставки:.+") ||
                        rows.matches("Способ оплаты:.+") ||
                        rows.matches("Комментарий:.+")) {
                    map.put(kayValue[0], kayValue[1]);
                }
            }
        }
        return map;
    }

    private String getForm() {
        return "Заказ: заказ \n" +
                "Адрес: \n" +
                "Сумма доставки: \n" +
                "Способ оплаты: \n" +
                "Комментарий: \n" ;
    }

    public BotApiMethod<?> callbackOperatorMainMenu(CallbackQuery callback, Order order, Dish dish, String tag) {
        OrderEnum orderEnum = getOrderEnum(order);
        switch (orderEnum) {
            case ORDER_END_REGISTRATION:
                return callbackOrderAndRegistration(callback, order, tag);
            case ORDER_EDITING:
                return callbackOrderEditing(callback, order, dish, tag);
            case ORDER_ACCEPT:
                return answerCallbackQuery(callback, inlineButtons.getAccept());
            case ORDER_CANCEL:
                return answerCallbackQuery(callback, inlineButtons.getCancel());
            default:
                return answerCallbackQuery(callback, notWork);
        }
    }

    private OrderEnum getOrderEnum(Order order) {
        int size = order.getOrderStateList().size() - 1;
        OrderState orderState = order.getOrderStateList().get(size);
        return OrderEnum.GET.orderState(orderState);
    }

    private BotApiMethod<?> callbackOrderAndRegistration(CallbackQuery callback, Order order, String tag) {
        BotApiMethod<?> botApiMethod;
        OrderState orderState;
        InlineKeyboardMarkup inlineMarkup;
        switch (tag) {
            case "o+":
                orderState = orderStatesService.create(order, OrderEnum.ORDER_ACCEPT.getValue());
                orderStatesService.save(orderState);
                inlineMarkup = inlineButtons.orderAcceptButtons();
                botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                break;
            case "o-":
                orderState = orderStatesService.create(order, OrderEnum.ORDER_CANCEL.getValue());
                orderStatesService.save(orderState);
                inlineMarkup = inlineButtons.orderCancelButtons();
                botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                break;
            case "oe":
                orderState = orderStatesService.create(order, OrderEnum.ORDER_EDITING.getValue());
                orderStatesService.save(orderState);
                inlineMarkup = inlineButtons.orderEditButtons(order);
                botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                break;
            default:
                botApiMethod = answerCallbackQuery(callback, error);
                break;
        }
        return botApiMethod;
    }

    private BotApiMethod<?> callbackOrderEditing(CallbackQuery callback, Order order, Dish dish, String tag) {
        BotApiMethod<?> botApiMethod;
        OrderState orderState;
        InlineKeyboardMarkup inlineMarkup;
        String text;
        List<String> typeDishList = inlineButtons.typeDishesInCafe(order);
        if (tag.matches("o.")) {
            switch (tag) {
                case "o+":
                    orderState = orderStatesService.create(order, OrderEnum.ORDER_ACCEPT.getValue());
                    orderStatesService.save(orderState);
                    inlineMarkup = inlineButtons.orderAcceptButtons();
                    sendMessageDepartment(order, DepartmentEnum.COURIER);
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                case "o-":
                    orderState = orderStatesService.create(order, OrderEnum.ORDER_CANCEL.getValue());
                    orderStatesService.save(orderState);
                    inlineMarkup = inlineButtons.orderCancelButtons();
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                case "oe":
                    inlineMarkup = inlineButtons.orderEditButtons(order);
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                case "or":
                    inlineMarkup = inlineButtons.orderAndRegistrationButtons(order);
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                default:
                    botApiMethod = answerCallbackQuery(callback, error);
                    break;
            }
        }
        else if (tag.matches("r.")) {
            ChequeDish chequeDish = chequeDishService.getChequeByOrderAndDish(order, dish);
            int count = chequeDish.getCountDishes();
            switch (tag) {
                case "rx":
                    count = 0;
                    chequeDishService.saveOrDeleteChequeDish(chequeDish, count);
                    text = getChequeText(order, true);
                    inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                    botApiMethod = editMessageText(callback, text, inlineMarkup);
                    break;
                case "r+":
                    count++;
                    chequeDishService.saveOrDeleteChequeDish(chequeDish, count);
                    text = getChequeText(order, true);
                    inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                    botApiMethod = editMessageText(callback, text, inlineMarkup);
                    break;
                case "r-":
                    if (count > 0) {
                        count--;
                        chequeDishService.saveOrDeleteChequeDish(chequeDish, count);
                        text = getChequeText(order, true);
                        inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                        botApiMethod = editMessageText(callback, text, inlineMarkup);
                        break;
                    }
                default:
                    botApiMethod = answerCallbackQuery(callback, error);
                    break;
            }
        }
        else if (tag.matches("e.+")) {
            switch (tag) {
                case "ead":
                    inlineMarkup = inlineButtons.orderAddDishButtons(order);
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                case "ec":
                    inlineMarkup = inlineButtons.orderEditCountDishButtons(order);
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                case "eb":
                    inlineMarkup = inlineButtons.orderEditButtons(order);
                    botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
                    break;
                case "er":
                    text = getChequeText(order, true);
                    inlineMarkup = inlineButtons.orderEditButtons(order);
                    botApiMethod = editMessageText(callback, text, inlineMarkup);
                    break;
                default:
                    botApiMethod = answerCallbackQuery(callback, error);
                    break;
            }
        }
        else if (tag.equals(String.valueOf(dish.getId()))) {
            ChequeDish chequeDish = chequeDishService.getChequeByOrderAndDish(order, dish);
            chequeDish.setCountDishes(1);
            chequeDishService.save(chequeDish);
            text = getChequeText(order, true);
            List<Dish> dishList = dishService.getDishListByType(dish.getType());
            inlineMarkup = inlineButtons.orderTypeDishesButtons(order, dishList);
            botApiMethod = editMessageText(callback, text, inlineMarkup);
        }
        else if (typeDishList.contains(tag)) {
            List<Dish> dishList = dishService.getDishListByType(tag);
            inlineMarkup = inlineButtons.orderTypeDishesButtons(order, dishList);
            botApiMethod = editMessageReplyMarkup(callback, inlineMarkup);
        }
        else {
            botApiMethod = answerCallbackQuery(callback, error);
        }
        return botApiMethod;
    }
}
