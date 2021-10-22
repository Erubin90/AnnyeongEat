package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Employee;
import tech.erubin.annyeong_eat.entity.EmployeeState;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.repository.EmployeeRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.EmployeeService;
import tech.erubin.annyeong_eat.telegramBot.enums.DepartmentEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;

    @Override
    public List<Employee> getEmployeeByCafeIdAndDepartmenName(Cafe cafeId, String name) {
        return repository.getDepartmentsByCafeIdAndName(cafeId, name);
    }

    @Override
    public List<User> getCourierIsFree(Cafe cafeId) {
        return getEmployeeByCafeIdAndDepartmenName(cafeId, DepartmentEnum.COURIER.getValue())
                .stream()
                .map(Employee::getUserId)
                .map(User::getEmployeeStateList)
                .map(x -> x.get(x.size() - 1))
                .filter(x -> EmployeeEnum.GET.isCourierFree(x.getState()))
                .map(EmployeeState::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cafe> getCafeByUserId(User user) {
        return repository.getAllCafeIdByUserId(user);
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
