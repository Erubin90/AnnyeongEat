package tech.erubin.annyeong_eat.telegramBot.enums;

import org.apache.logging.log4j.util.Strings;
import tech.erubin.annyeong_eat.entity.Employee;
import tech.erubin.annyeong_eat.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Departments {
    OPERATOR("Оператор"),
    ADMINISTRATOR("Администратор"),
    COURIER("Курьер"),
    DEVELOPER("Разработчик"),
    CLIENT("Клиент"),
    UNKNOWN(Strings.EMPTY);

    private final String department;

    private static final Map<String, Departments> DEPARTMENTS_MAP = Arrays.stream(Departments.values())
            .collect(Collectors.toMap(Departments::getDepartment, Function.identity()));

    Departments(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public static Departments department(User user) {
        List<Employee> el = user.getEmployeeList();
        if (el != null && el.size() > 0) {
            return Optional.ofNullable(DEPARTMENTS_MAP.get(el.get(0).getName())).orElse(UNKNOWN);
        }
        else {
            return CLIENT;
        }
    }

    public static boolean isCourier(String obtainingMethod) {
        return obtainingMethod.equals(COURIER.getDepartment());
    }
}
