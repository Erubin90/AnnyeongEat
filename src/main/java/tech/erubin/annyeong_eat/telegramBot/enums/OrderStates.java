package tech.erubin.annyeong_eat.telegramBot.enums;


import org.apache.logging.log4j.util.Strings;
import tech.erubin.annyeong_eat.entity.OrderState;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum OrderStates {
    ORDER_START_REGISTRATION("начало оформление заказа"),
    ORDER_END_REGISTRATION("заказ оформлен"),

    ORDER_EDITING("редактирование заказа"),

    ORDER_ACCEPT("заказ принят"),
    ORDER_CANCEL("заказ отменен"),
    ORDER_START_COOK("заказ начал готовиться"),
    ORDER_END_COOK("заказ готов к отправке"),
    ORDER_START_DELIVERY("курьер забрал заказ"),
    ORDER_END_DELIVERY("курьер доставил заказ"),
    UNKNOWN(Strings.EMPTY);
    
    private final String state;

    private static final Map<String, OrderStates> ORDER_STATES = Arrays.stream(OrderStates.values())
            .collect(Collectors.toMap(OrderStates::getState, Function.identity()));

    OrderStates(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public static OrderStates orderState(OrderState os) {
        return Optional.ofNullable(ORDER_STATES.get(os.getState())).orElse(UNKNOWN);
    }

    public static boolean isOrderNoEndDelivery(OrderState os) {
        OrderStates s = orderState(os);
        return !(s == ORDER_END_DELIVERY || s == ORDER_CANCEL);
    }

    public static boolean isOrderAccepted(OrderState os) {
        OrderStates s = orderState(os);
        return s != ORDER_START_REGISTRATION && s != ORDER_CANCEL;
    }

    public static boolean isOrderEditing(OrderState os) {
        OrderStates s = orderState(os);
        return s == ORDER_EDITING;
    }
}
