package tech.erubin.annyeong_eat.telegramBot.enums;

import tech.erubin.annyeong_eat.entity.EmployeeState;

public enum EmployeeEnum {
    OPERATOR_MAIN_MENU("главное меню Оператора"),
    OPERATOR_CHOOSING_CAFE("выбор кафе"),
    OPERATOR_CHOOSING_TABLE("выбор стола"),
    OPERATOR_CAFE_MENU("выбор блюд"),
    OPERATOR_PAYMENT_METHOD("указание способа оплаты"),
    OPERATOR_CONFIRMATION("подтверждение заказа"),

    ADMINISTRATOR_MAIN_MENU("главное меню Оператора"),
    COURIER_MAIN_MENU("главное меню Курьер"),
    DEVELOPER_MAIN_MENU("главное меню Разработчик"),

    NO_CORRECT_STATE("не корректное состояние"),
    GET;

    private String state;

    EmployeeEnum() {
    }

    EmployeeEnum(String state) {
        this.state = state;
    }

    public String getValue() {
        return state;
    }

    public EmployeeEnum employeeState(DepartmentEnum department, EmployeeState employeeState) {
        String state = employeeState.getState();
        switch (department) {
            case OPERATOR:
                if (state.equals(OPERATOR_MAIN_MENU.getValue())) {
                    return OPERATOR_MAIN_MENU;
                }
                else if (state.equals(OPERATOR_CHOOSING_CAFE.getValue())) {
                    return OPERATOR_CHOOSING_CAFE;
                }
                else if (state.equals(OPERATOR_CHOOSING_TABLE.getValue())) {
                    return OPERATOR_CHOOSING_TABLE;
                }
                else if (state.equals(OPERATOR_CAFE_MENU.getValue())) {
                    return OPERATOR_CAFE_MENU;
                }
                else if (state.equals(OPERATOR_PAYMENT_METHOD.getValue())) {
                    return OPERATOR_PAYMENT_METHOD;
                }
                else if (state.equals(OPERATOR_CONFIRMATION.getValue())) {
                    return OPERATOR_CONFIRMATION;
                }
                else {
                    return NO_CORRECT_STATE;
                }
            case ADMINISTRATOR:
            case COURIER:
            case DEVELOPER:
                if (state.equals(OPERATOR_MAIN_MENU.getValue())) {
                    return OPERATOR_MAIN_MENU;
                }
                else {
                    return NO_CORRECT_STATE;
                }
            default:
                return NO_CORRECT_STATE;
        }
    }
}
