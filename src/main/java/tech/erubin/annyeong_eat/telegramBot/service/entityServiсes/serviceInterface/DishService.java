package tech.erubin.annyeong_eat.telegramBot.service.entityServiсes.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Dish;

import java.util.List;

public interface DishService {
    List<Dish> getAllDish();

    Dish getDishById(int id);

    void saveDish(Dish dish);

    void deleteDish(Dish dish);

    List<Dish> getDishListByType(String type);

    Dish getDishByTag(String tag);

    Dish createDish();
}
