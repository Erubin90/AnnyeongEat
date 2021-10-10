package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.EmployeeState;
import tech.erubin.annyeong_eat.entity.User;

public interface EmployeeStateService {
    EmployeeState getState(User user);

    void delete(EmployeeState employeeState);

    void save(EmployeeState employeeState);

    EmployeeState create(User user, String state);

    void createAndSave(User user, String state);
}
