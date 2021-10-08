package tech.erubin.annyeong_eat.telegramBot.enums;

import tech.erubin.annyeong_eat.entity.UserState;

public enum EmployeeEnum {

    OPERATOR_MAIN_MENU("главное меню"),
    OPERATOR_CAFE_MENU("создание заказа"),
    OPERATOR_METHOD_OF_OBTAINING("способ получения"),
    OPERATOR_CHOOSE_TABLE("выбор стола"),
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

    public EmployeeEnum employeeState(DepartmentEnum department, UserState userState) {
        String state = userState.getState();
        switch (department) {
            case OPERATOR:
                if (state.equals(OPERATOR_MAIN_MENU.getValue())) {
                    return OPERATOR_MAIN_MENU;
                }
                else if (state.equals(OPERATOR_CAFE_MENU.getValue())) {
                    return OPERATOR_CAFE_MENU;
                }
                else if (state.equals(OPERATOR_METHOD_OF_OBTAINING.getValue())) {
                    return OPERATOR_METHOD_OF_OBTAINING;
                }
                else if (state.equals(OPERATOR_CHOOSE_TABLE.getValue())) {
                    return OPERATOR_CHOOSE_TABLE;
                }
                else {
                    return NO_CORRECT_STATE;
                }
            case ADMINISTRATOR:
                return NO_CORRECT_STATE;
            case COURIER:
                return NO_CORRECT_STATE;
            case DEVELOPER:
                return NO_CORRECT_STATE;
            default:
                return NO_CORRECT_STATE;
        }
    }
}
