package tech.erubin.annyeong_eat.telegramBot.enums;

public enum EmployeeEnum {
    CLIENT("Клиент"),
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
            else if (department.equals(CLIENT.getValue())) {
                return CLIENT;
            }
            else {
                throw new NullPointerException("Данного депатамента не существует");
            }
    }

    public boolean isEmployee(EmployeeEnum department) {
        return department != CLIENT;
    }

}
