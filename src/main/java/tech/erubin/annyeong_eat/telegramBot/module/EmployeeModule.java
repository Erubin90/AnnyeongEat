package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.entity.OrderState;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Module;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmployeeModule extends Module {
    private final ReplyButtons replyButtons;

    public EmployeeModule(OrderServiceImpl orderService, UserServiceImpl userService,
                          UserStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                          EmployeeServiceImpl departmentService, ReplyButtons replyButtons,
                          @Lazy AnnyeongEatWebHook webHook) {
        super(orderService, userService, userStatesService, orderStatesService, departmentService, webHook);
        this.replyButtons = replyButtons;
    }

    public SendMessage operator(Update update, String soursText) {
        String text;
        if (soursText.indexOf("Заказ:") == 0) {
            text = getTextEditingOrder(soursText);
        }
        else if (soursText.equals(replyButtons.getForm())) {
            text = getForm();
        }
        else {
            text = noCommand;
        }
        ReplyKeyboard replyKeyboard = replyButtons.employeeOperator();
        return sendMessage(update, replyKeyboard, text, null);
    }

    public SendMessage administrator(Update update, User user, String soursText) {
//        ReplyKeyboard replyKeyboard;
//        UserState userState;
//        String text;
        return null;
    }

    public SendMessage courier(Update update, User user, String soursText) {
        ReplyKeyboard replyKeyboard;
        UserState userState;
        String text;
        return null;
    }

    public SendMessage developer(Update update, User user, String soursText) {
//        ReplyKeyboard replyKeyboard;
//        UserState userState;
//        String text;
//        return sendMessage(update, replyKeyboard, text, userState);
        return null;
    }

    private String getTextEditingOrder(String sourceText) {
        Map<String, String> map = getMap(sourceText);
        Order order = orderService.getOrderByOrderName(map.get("Заказ"));
        String text;
        if (order != null) {
            OrderState orderState = order.getOrderStateList().get(order.getOrderStateList().size() - 1);
            if (OrderEnum.GET.isOrderEditing(orderState.getState())) {
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
                sendMessageOperator(order);
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
        return "Заказ:\n" +
                "Адрес:\n" +
                "Сумма доставки:\n" +
                "Способ оплаты:\n" +
                "Комментарий:\n" ;
    }
}
