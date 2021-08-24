package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.Dish;

import java.util.List;

public interface DishService {

    Dish getDishById(int id);

    void save(Dish dish);

    void delete(Dish dish);

    List<Dish> getDishListByType(String type);

    Dish create();
}
