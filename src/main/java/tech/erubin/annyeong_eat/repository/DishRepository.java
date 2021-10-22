package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Dish;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findDishByType(String type);

    Dish findDishByName(String name);

    @Query("SELECT d.name FROM Dish d WHERE d.cafeId = :cafe")
    List<String> findAllNameByCafeId(@Param("cafe") Cafe cafe);

    @Query("SELECT d.type FROM Dish d WHERE d.cafeId = :cafe GROUP BY d.type")
    List<String> findAllDishTypeByCafe(@Param("cafe")Cafe cafe);
}
