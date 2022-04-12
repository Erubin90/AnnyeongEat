package tech.erubin.annyeong_eat.telegramBot.enums;


import org.apache.logging.log4j.util.Strings;
import tech.erubin.annyeong_eat.entity.OrderState;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum OrderStates {
    START_REGISTRATION("начало оформление заказа"),
    END_REGISTRATION("заказ оформлен"),
    EDITING("редактирование заказа"),
    ACCEPT("заказ принят"),
    CANCEL("заказ отменен"),
    START_COOK("заказ начал готовиться"),
    END_COOK("заказ готов к отправке"),
    START_DELIVERY("курьер забрал заказ"),
    END_DELIVERY("курьер доставил заказ"),
    END_ORDER("стол оплачен"),
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

    public static boolean isOrderAccepted(OrderState os) {
        OrderStates s = orderState(os);
        return s != START_REGISTRATION && s != CANCEL;
    }

    public static boolean isOrderEditing(OrderState os) {
        OrderStates s = orderState(os);
        return s == EDITING;
    }
}
