package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Dish;

import java.util.List;

public interface DishService {

    Dish getDishByName(String dishId);

    List<String> getAllNameByCafe(Cafe cafe);

    List<String> getAllDishTypeByCafe(Cafe cafe);

    void save(Dish dish);

    void delete(Dish dish);

    List<Dish> getDishListByType(String type);

    Dish create();
}
