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
    COURIER_WEEKEND("выходной"),
    COURIER_FREE("свободен"),
    COURIER_BUSY("на заказе"),
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
        return employeeState(department, state);
    }

    public EmployeeEnum employeeState(DepartmentEnum department, String state) {
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
                if (state.equals(ADMINISTRATOR_MAIN_MENU.getValue())) {
                    return ADMINISTRATOR_MAIN_MENU;
                }
                else {
                    return NO_CORRECT_STATE;
                }
            case COURIER:
                if  (state.equals(COURIER_MAIN_MENU.getValue())) {
                    return COURIER_MAIN_MENU;
                }
                else if (state.equals(COURIER_WEEKEND.getValue())) {
                    return COURIER_WEEKEND;
                }
                else if (state.equals(COURIER_FREE.getValue())) {
                    return COURIER_FREE;
                }
                else if (state.equals(COURIER_BUSY.getValue())) {
                    return COURIER_BUSY;
                }
                else {
                    return NO_CORRECT_STATE;
                }
            case DEVELOPER:
                if (state.equals(DEVELOPER_MAIN_MENU.getValue())) {
                    return DEVELOPER_MAIN_MENU;
                }
                else {
                    return NO_CORRECT_STATE;
                }
            default:
                return NO_CORRECT_STATE;
        }
    }

    public boolean isCourierFree (EmployeeEnum employeeEnum) {
        return employeeEnum == COURIER_FREE;
    }

    public boolean isCourierFree (String employee) {
        EmployeeEnum employeeEnum = employeeState(DepartmentEnum.COURIER, employee);
        return employeeEnum == COURIER_FREE;
    }
}
