package tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBotClient.entity.ChequeDish;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Dish;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Order;

public interface ChequeDishService {

    ChequeDish getChequeByOrderAndDish(Order order, Dish dish);

    void save(ChequeDish chequeDish);

    void delete(ChequeDish chequeDish);

    ChequeDish create(Order order, Dish dish);
}
