package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Employee;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.repository.EmployeeRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.EmployeeService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;

    @Override
    public List<Employee> getEmployeeByCafeIdAndDepartmenName(Cafe cafeId, String name) {
        return repository.getDepartmentsByCafeIdAndName(cafeId, name);
    }

    @Override
    public List<Integer> getCafeByUserId(User userId) {
        return repository.getAllCafeIdByUserId(userId);
    }

    @Override
    public List<Employee> getDeveloperList() {
        List<Integer> idList = repository.getAllDevelopers();
        List<Employee> employeeList = new ArrayList<>();
        for (Integer id : idList) {
            employeeList.add(repository.getDepartmentById(id));
        }
        return employeeList;
    }
}
