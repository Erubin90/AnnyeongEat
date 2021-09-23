package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Department;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    List<Department> getDepartmentsByCafeIdAndName(Cafe cafeId, String name);

    @Query("SELECT id FROM Department WHERE name like 'Разработчик'")
    List<Integer> getAllDevelopers();

    Department getDepartmentById(Integer id);
}
