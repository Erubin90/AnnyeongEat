package tech.erubin.annyeong_eat.telegramBot.enums;

public enum EmployeeEnum {
    OPERATOR("Оператор"),
    ADMINISTRATOR("Администратор"),
    COURIER("Курьер"),
    DEVELOPER("Разработчик"),
    GET;

    private String department;

    EmployeeEnum() {
    }

    EmployeeEnum(String department) {
        this.department = department;
    }

    public String getValue() {
        return department;
    }

    public EmployeeEnum department(String department) {
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
                return null;
            }
    }

    public boolean isEmployee(String state) {
        return department(state) != null;
    }
}
