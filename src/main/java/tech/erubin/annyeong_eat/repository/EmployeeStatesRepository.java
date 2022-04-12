package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.erubin.annyeong_eat.entity.EmployeeState;

public interface EmployeeStatesRepository extends JpaRepository<EmployeeState, Integer> {
}
