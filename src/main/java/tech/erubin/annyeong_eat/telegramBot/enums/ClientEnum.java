package tech.erubin.annyeong_eat.telegramBot.enums;

public enum ClientEnum {
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
    ORDER_COMMENT("оставить комментарий"),
    DELIVERY_ADDRESS("указание адреса"),
    DELIVERY_PHONE_NUMBER("указание номера"),
    DELIVERY_PAYMENT_METHOD("указание способа оплаты"),
    DELIVERY_CONFIRMATION("подтверждение заказа"),
    NO_CORRECT_STATE("не корректное состояние"),
    GET;

    private String state;

    ClientEnum() {
    }

    ClientEnum(String state) {
        this.state = state;
    }

    public String getValue() {
        return state;
    }

    public ClientEnum userState(String state) {
        if (state.equals(REGISTRATION_START.getValue())) {
            return REGISTRATION_START;
        }
        else if (state.equals(REGISTRATION_CITY.getValue())) {
            return REGISTRATION_CITY;
        }
        else if (state.equals(REGISTRATION_NAME.getValue())) {
            return REGISTRATION_NAME;
        }
        else if (state.equals(REGISTRATION_SURNAME.getValue())) {
            return REGISTRATION_SURNAME;
        }
        else if (state.equals(REGISTRATION_PHONE_NUMBERS.getValue())) {
            return REGISTRATION_PHONE_NUMBERS;
        }
        else if (state.equals(MAIN_MENU.getValue())) {
            return MAIN_MENU;
        }
        else if (state.equals(HELP.getValue())) {
            return HELP;
        }
        else if (state.equals(PROFILE.getValue())) {
            return PROFILE;
        }
        else if (state.equals(ORDER_CHECK.getValue())) {
            return ORDER_CHECK;
        }
        else if (state.equals(ORDER_CAFE.getValue())) {
            return ORDER_CAFE;
        }
        else if (state.equals(ORDER_CAFE_MENU.getValue())) {
            return ORDER_CAFE_MENU;
        }
        else if (state.equals(ORDER_METHOD_OF_OBTAINING.getValue())) {
            return ORDER_METHOD_OF_OBTAINING;
        }
        else if (state.equals(ORDER_COMMENT.getValue())) {
            return ORDER_COMMENT;
        }
        else if (state.equals(DELIVERY_PHONE_NUMBER.getValue())) {
            return DELIVERY_PHONE_NUMBER;
        }
        else if (state.equals(DELIVERY_PAYMENT_METHOD.getValue())) {
            return DELIVERY_PAYMENT_METHOD;
        }
        else if (state.equals(DELIVERY_CONFIRMATION.getValue())) {
            return DELIVERY_CONFIRMATION;
        }
        else if (state.equals(DELIVERY_ADDRESS.getValue())) {
            return DELIVERY_ADDRESS;
        }
        else {
            return NO_CORRECT_STATE;
        }
    }
}