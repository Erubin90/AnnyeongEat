package tech.erubin.annyeong_eat.telegramBot.enums;

import org.apache.logging.log4j.util.Strings;
import tech.erubin.annyeong_eat.entity.EmployeeState;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum EmployeeStates {
    OPERATOR_MAIN_MENU("главное меню Оператора"),
    OPERATOR_CHOOSING_CAFE("выбор кафе"),

    WAITER_MAIN_MENU("главное меню Официанта"),

    ADMINISTRATOR_MAIN_MENU("главное меню Администратора"),

    COURIER_MAIN_MENU("главное меню Курьер"),
    COURIER_FREE("свободен"),
    COURIER_BUSY("на заказе"),

    DEVELOPER_MAIN_MENU("главное меню Разработчик"),

    CHOOSING_TABLE("выбор стола"),
    CAFE_MENU("выбор блюд"),
    PAYMENT_METHOD("указание способа оплаты"),
    COMMENT("оставить комментарий"),
    CONFIRMATION("подтверждение заказа"),
    WEEKEND("выходной"),
    UNKNOWN(Strings.EMPTY);

    private final String state;

    private static final Map<String, EmployeeStates> EMPLOYEE_STATES = Arrays.stream(EmployeeStates.values())
            .collect(Collectors.toMap(EmployeeStates::getState, Function.identity()));

    EmployeeStates(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public static EmployeeStates employeeState(EmployeeState es) {
        return Optional.ofNullable(EMPLOYEE_STATES.get(es.getState())).orElse(UNKNOWN);
    }

    public static boolean isCourierFree (EmployeeState es) {
        EmployeeStates s = employeeState(es);
        return s == COURIER_FREE;
    }
}
