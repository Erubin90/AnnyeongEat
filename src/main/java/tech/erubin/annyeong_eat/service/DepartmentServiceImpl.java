package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Department;
import tech.erubin.annyeong_eat.repository.DepartmentRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.DepartmentService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;

    @Override
    public List<Department> getEmployeeByCafeIdAndDepartmenName(Cafe cafeId, String name) {
        return repository.getDepartmentsByCafeIdAndName(cafeId, name);
    }

    @Override
    public List<Department> getDeveloperList() {
        List<Integer> idList = repository.getAllDevelopers();
        List<Department> departmentList = new ArrayList<>();
        for (Integer id : idList) {
            departmentList.add(repository.getDepartmentById(id));
        }
        return departmentList;
    }


}
