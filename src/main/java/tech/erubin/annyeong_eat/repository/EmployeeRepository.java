package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Employee;
import tech.erubin.annyeong_eat.entity.User;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> getDepartmentsByCafeIdAndName(Cafe cafeId, String name);

    @Query("SELECT e.cafeId FROM Employee e WHERE e.userId = :user GROUP BY e.cafeId")
    List<Cafe> getAllCafeIdByUserId(@Param("user") User user);

    @Query("SELECT id FROM Employee WHERE name like 'Разработчик'")
    List<Integer> getAllDevelopers();

    Employee getDepartmentById(Integer id);
}
