package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Employee;
import tech.erubin.annyeong_eat.repository.DepartmentRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.DepartmentService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;

    @Override
    public List<Employee> getEmployeeByCafeIdAndDepartmenName(Cafe cafeId, String name) {
        return repository.getDepartmentsByCafeIdAndName(cafeId, name);
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
