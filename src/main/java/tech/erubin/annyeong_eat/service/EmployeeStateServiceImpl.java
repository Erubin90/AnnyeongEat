package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.EmployeeState;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.repository.EmployeeStatesRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.EmployeeStateService;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeStates;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeStateServiceImpl implements EmployeeStateService {
    private final EmployeeStatesRepository repository;

    @Override
    public EmployeeState getState(User user) {
        List<EmployeeState> employeeStateList = user.getEmployeeStateList();
        if (employeeStateList != null && employeeStateList.size() > 0){
            return employeeStateList.get(employeeStateList.size() - 1);
        }
        else {
            return create(user, EmployeeStates.OPERATOR_MAIN_MENU.getState());
        }
    }

    @Override
    public void delete(EmployeeState employeeState) {
        repository.delete(employeeState);
    }

    @Override
    public void save(EmployeeState employeeState) {
        if (employeeState != null) {
            repository.save(employeeState);
        }
    }

    @Override
    public EmployeeState create(User user, String state) {
        return new EmployeeState(user, state);
    }

    @Override
    public void createAndSave(User user, String state) {
        repository.save(create(user, state));
    }
}
