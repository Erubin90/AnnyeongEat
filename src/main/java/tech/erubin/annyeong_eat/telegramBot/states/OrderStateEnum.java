package tech.erubin.annyeong_eat.telegramBot.states;


public enum OrderStateEnum {
    ORDER_START_REGISTRATION("оформление заказа"),
    ORDER_END_REGISTRATION("заказ оформлен"),
    ORDER_CONFIRMATION("подтверждение заказа"),
    ORDER_CONFIRMATION_PAYMENT("подтверждение оплаты"),

    ORDER_START_COOK("заказ начал готовиться"),
    ORDER_END_COOK("заказ готов к отправке"),

    ORDER_START_DELIVERY("курьер забрал заказ"),
    ORDER_END_DELIVERY("курьер доставил заказ"),

    CLIENT_CONFIRMATION("клиент подтвердил заказ"),
    ERROR("ошибка"),
    GET;

    private String orderState;

    OrderStateEnum() {
    }

    OrderStateEnum(String orderState) {
        this.orderState = orderState;
    }

    public String getValue() {
        return orderState;
    }

    public OrderStateEnum orderState(String state) {

        if (state.equals(ORDER_START_REGISTRATION.getValue())){
            return ORDER_START_REGISTRATION;
        }
        else if (state.equals(ORDER_END_REGISTRATION.getValue())){
            return ORDER_END_REGISTRATION;
        }
        else if (state.equals(ORDER_CONFIRMATION.getValue())){
            return ORDER_CONFIRMATION;
        }
        else if (state.equals(ORDER_CONFIRMATION_PAYMENT.getValue())){
            return ORDER_CONFIRMATION_PAYMENT;
        }
        else if (state.equals(ORDER_START_COOK.getValue())){
            return ORDER_START_COOK;
        }
        else if (state.equals(ORDER_END_COOK.getValue())){
            return ORDER_END_COOK;
        }
        else if (state.equals(ORDER_START_DELIVERY.getValue())){
            return ORDER_START_DELIVERY;
        }
        else if (state.equals(ORDER_END_DELIVERY.getValue())){
            return ORDER_END_DELIVERY;
        }
        else if (state.equals(CLIENT_CONFIRMATION.getValue())){
            return CLIENT_CONFIRMATION;
        }
        else {
            return ERROR;
        }
    }
}
