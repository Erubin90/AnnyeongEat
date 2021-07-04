package tech.erubin.annyeong_eat.bot.service.entityService.ServiceInterface;

import tech.erubin.annyeong_eat.bot.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployee();

    Employee getEmployeeById(int id);

    void saveEmployee(Employee employee);

    void deleteEmployee(Employee employee);

    Employee createEmployee(String name, String surname, String telegramName, String phoneNumber, String role);
}
