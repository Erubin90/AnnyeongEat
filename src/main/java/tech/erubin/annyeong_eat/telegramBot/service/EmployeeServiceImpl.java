package tech.erubin.annyeong_eat.telegramBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Employee;
import tech.erubin.annyeong_eat.telegramBot.repository.EmployeeRepository;
import tech.erubin.annyeong_eat.telegramBot.service.ServiceInterface.EmployeeService;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Override
    public List<Employee> getAllEmployee() {
        return repository.findAll();
    }

    @Override
    public Employee getEmployeeById(int id) {
        return repository.getById(id);
    }

    @Override
    public Employee getEmployeeByTelegramUserId(String userID) {
        return null;
    }

    @Override
    public void saveEmployee(Employee employee) {
        repository.save(employee);
    }

    @Override
    public void deleteEmployee(Employee employee) {
        repository.delete(employee);
    }

    @Override
    public Employee createEmployee(String name, String surname, String telegramName, String phoneNumber, String role) {
        return new Employee(name, surname, telegramName, phoneNumber, role);
    }

    @Override
    public Employee createEmployee() {
        return new Employee();
    }
}