package tech.erubin.annyeong_eat.telegramBot.enums;

import tech.erubin.annyeong_eat.entity.Employee;
import tech.erubin.annyeong_eat.entity.User;

import java.util.List;

public enum DepartmentEnum {
    OPERATOR("Оператор"),
    ADMINISTRATOR("Администратор"),
    COURIER("Курьер"),
    DEVELOPER("Разработчик"),
    CLIENT("Клиент"),
    NO_CORRECT_DEPARTMENT("не корректное состояние"),
    GET;

    private String department;

    DepartmentEnum(String department) {
        this.department = department;
    }

    DepartmentEnum() {
    }

    public String getValue() {
        return department;
    }

    public DepartmentEnum department(User user) {
        List<Employee> employeeList = user.getEmployeeList();
        if (employeeList != null && employeeList.size() > 0) {
            String department = employeeList.get(0).getName();
            if (department.equals(OPERATOR.getValue())) {
                return OPERATOR;
            }
            else if (department.equals(ADMINISTRATOR.getValue())) {
                return ADMINISTRATOR;
            }
            else if (department.equals(COURIER.getValue())) {
                return COURIER;
            }
            else if (department.equals(DEVELOPER.getValue())) {
                return DEVELOPER;
            }
            else {
                return NO_CORRECT_DEPARTMENT;
            }
        }
        else {
            return CLIENT;
        }
    }
}
