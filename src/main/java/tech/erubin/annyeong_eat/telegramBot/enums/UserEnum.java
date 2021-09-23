package tech.erubin.annyeong_eat.telegramBot.enums;

public enum UserEnum {

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
    DELIVERY_ADDRESS("указание адреса"),
    DELIVERY_PHONE_NUMBER("указание номера"),
    DELIVERY_PAYMENT_METHOD("указание способа оплаты"),
    DELIVERY_CONFIRMATION("подтверждение заказа"),

    CHOICE_DEPARTMENT("Выбор депортамента"),
    CLIENT("Клиент"),
    OPERATOR("Оператор"),
    ADMINISTRATOR("Администратор"),
    COURIER("Курьер"),
    DEVELOPER("Разработчик"),
    GET;

    private String clientState;

    UserEnum() {
    }

    UserEnum(String clientState) {
        this.clientState = clientState;
    }

    public String getValue() {
        return clientState;
    }

    public UserEnum userState(String state) {

        if (state.equals(REGISTRATION_START.clientState)) {
            return REGISTRATION_START;
        }
        else if (state.equals(REGISTRATION_CITY.clientState)) {
            return REGISTRATION_CITY;
        }
        else if (state.equals(REGISTRATION_NAME.clientState)) {
            return REGISTRATION_NAME;
        }
        else if (state.equals(REGISTRATION_SURNAME.clientState)) {
            return REGISTRATION_SURNAME;
        }
        else if (state.equals(REGISTRATION_PHONE_NUMBERS.clientState)) {
            return REGISTRATION_PHONE_NUMBERS;
        }
        else if (state.equals(MAIN_MENU.clientState)) {
            return MAIN_MENU;
        }
        else if (state.equals(HELP.clientState)) {
            return HELP;
        }
        else if (state.equals(PROFILE.clientState)) {
            return PROFILE;
        }
        else if (state.equals(ORDER_CHECK.clientState)) {
            return ORDER_CHECK;
        }
        else if (state.equals(ORDER_CAFE.clientState)) {
            return ORDER_CAFE;
        }
        else if (state.equals(ORDER_CAFE_MENU.clientState)) {
            return ORDER_CAFE_MENU;
        }
        else if (state.equals(DELIVERY_PHONE_NUMBER.clientState)) {
            return DELIVERY_PHONE_NUMBER;
        }
        else if (state.equals(DELIVERY_PAYMENT_METHOD.clientState)) {
            return DELIVERY_PAYMENT_METHOD;
        }
        else if (state.equals(DELIVERY_CONFIRMATION.clientState)) {
            return DELIVERY_CONFIRMATION;
        }
        else if (state.equals(DELIVERY_ADDRESS.clientState)) {
            return DELIVERY_ADDRESS;
        }
        else if (state.equals(OPERATOR.clientState)) {
            return OPERATOR;
        }
        else if (state.equals(ADMINISTRATOR.clientState)) {
            return ADMINISTRATOR;
        }
        else if (state.equals(COURIER.clientState)) {
            return COURIER;
        }
        else if (state.equals(DEVELOPER.clientState)) {
            return DEVELOPER;
        }
        else if (state.equals(CHOICE_DEPARTMENT.clientState)) {
            return CHOICE_DEPARTMENT;
        }
        else if (state.equals(CLIENT.clientState)) {
            return CLIENT;
        }
        else {
            return null;
        }
    }

    public boolean isEmployeeState(UserEnum userEnum) {
        return userEnum == UserEnum.OPERATOR ||
                userEnum == UserEnum.ADMINISTRATOR ||
                userEnum == UserEnum.COURIER ||
                userEnum == UserEnum.DEVELOPER;
    }
}