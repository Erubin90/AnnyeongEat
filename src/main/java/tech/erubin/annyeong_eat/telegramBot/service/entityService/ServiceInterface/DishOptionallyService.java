package tech.erubin.annyeong_eat.telegramBot.service.entityService.ServiceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.DishOptionally;

import java.util.List;

public interface DishOptionallyService {
    List<DishOptionally> getAllDishOptionally();

    DishOptionally getDishOptionallyById(int id);

    void saveDishOptionally(DishOptionally dishOptionally);

    void deleteDishOptionally(DishOptionally dishOptionally);

    DishOptionally createDishOptionally(String name, String type, Double price);
}
