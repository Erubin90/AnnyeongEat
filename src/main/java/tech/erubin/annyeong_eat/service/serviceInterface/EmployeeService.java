package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Employee;
import tech.erubin.annyeong_eat.entity.User;

import java.util.List;

public interface EmployeeService {

    List<Employee> getEmployeeByCafeIdAndDepartmenName(Cafe cafeId, String name);

    List<Cafe> getCafeByUserId(User userId);

    List<Employee> getDeveloperList();
}
