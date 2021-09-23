package tech.erubin.annyeong_eat.telegramBot.enums;


public enum OrderEnum {
    ORDER_START_REGISTRATION("начало оформление заказа"),
    ORDER_END_REGISTRATION("заказ оформлен"),
    ORDER_ACCEPT("заказ принят"),
    ORDER_CANCEL("заказ отменен"),
    ORDER_START_COOK("заказ начал готовиться"),
    ORDER_END_COOK("заказ готов к отправке"),
    ORDER_START_DELIVERY("курьер забрал заказ"),
    ORDER_END_DELIVERY("курьер доставил заказ"),
    GET;
    
    private String orderState;

    OrderEnum() {
    }

    OrderEnum(String orderState) {
        this.orderState = orderState;
    }

    public String getValue() {
        return orderState;
    }

    public OrderEnum orderState(String state) {

        if (state.equals(ORDER_START_REGISTRATION.getValue())){
            return ORDER_START_REGISTRATION;
        }
        else if (state.equals(ORDER_END_REGISTRATION.getValue())){
            return ORDER_END_REGISTRATION;
        }
        else if (state.equals(ORDER_ACCEPT.getValue())){
            return ORDER_ACCEPT;
        }
        else if (state.equals(ORDER_CANCEL.getValue())){
            return ORDER_CANCEL;
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
        else {
            return null;
        }
    }

    public boolean isOrderAccepted(String orderState) {
        OrderEnum orderEnum = orderState(orderState);
        return orderEnum != ORDER_START_REGISTRATION && orderEnum != ORDER_CANCEL;
    }
}
