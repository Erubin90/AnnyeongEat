package tech.erubin.annyeong_eat.telegramBot.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployee();

    Employee getEmployeeById(int id);

    Employee getEmployeeByTelegramUserId(String userID);

    void saveEmployee(Employee employee);

    void deleteEmployee(Employee employee);

    Employee createEmployee();
}
