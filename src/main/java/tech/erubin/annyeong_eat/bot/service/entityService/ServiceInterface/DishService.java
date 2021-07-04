package tech.erubin.annyeong_eat.bot.service.entityService.ServiceInterface;

import tech.erubin.annyeong_eat.bot.entity.Dish;

import java.util.List;

public interface DishService {
    List<Dish> getAllDish();

    Dish getDishById(int id);

    void saveDish(Dish dish);

    void deleteDish(Dish dish);

    Dish createDish(String name, String type, Double price);
}
