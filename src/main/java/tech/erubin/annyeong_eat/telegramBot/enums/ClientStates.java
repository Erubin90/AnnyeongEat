package tech.erubin.annyeong_eat.telegramBot.enums;

import org.apache.logging.log4j.util.Strings;
import tech.erubin.annyeong_eat.entity.ClientState;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ClientStates {
    REGISTRATION_START("начало регистрации"),
    REGISTRATION_CITY("регистрация города"),
    REGISTRATION_NAME("регистрация имени"),
    REGISTRATION_SURNAME("регистрация фамилии"),
    REGISTRATION_PHONE_NUMBERS("регистрация номера телефона"),

    MAIN_MENU("главное меню"),
    HELP("помощь"),
    PROFILE("просмотр профиля"),
    ORDER_CHECK("просмотр статуса заказа"),

    ORDER_CAFE("выбор кафе"),
    ORDER_CAFE_MENU("выбор блюд"),
    ORDER_METHOD_OF_OBTAINING("способ получения"),
    DELIVERY_ADDRESS("указание адреса"),
    DELIVERY_PHONE_NUMBER("указание номера"),
    DELIVERY_PAYMENT_METHOD("способ оплаты"),
    ORDER_COMMENT("оставить комментарий"),
    DELIVERY_CONFIRMATION("подтверждение заказа"),

    UNKNOWN(Strings.EMPTY);

    private final String state;

    private static final Map<String, ClientStates> CLIENT_STATES = Arrays.stream(ClientStates.values())
            .collect(Collectors.toMap(ClientStates::getState, Function.identity()));

    ClientStates(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public static ClientStates userState(ClientState cs) {
        return Optional.ofNullable(CLIENT_STATES.get(cs.getState())).orElse(UNKNOWN);
    }
}