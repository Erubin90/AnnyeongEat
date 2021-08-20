package tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBotClient.entity.DishOptionally;

public interface DishOptionallyService {

    void save(DishOptionally dishOptionally);

    void delete(DishOptionally dishOptionally);

    DishOptionally create();
}
