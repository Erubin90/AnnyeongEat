package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> getEmployeeByCafeIdAndDepartmenName(Cafe cafeId, String name);

    List<Department> getDeveloperList();
}
