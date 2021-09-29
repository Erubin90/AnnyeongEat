package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Employee;

import java.util.List;

public interface DepartmentService {

    List<Employee> getEmployeeByCafeIdAndDepartmenName(Cafe cafeId, String name);

    List<Employee> getDeveloperList();
}
