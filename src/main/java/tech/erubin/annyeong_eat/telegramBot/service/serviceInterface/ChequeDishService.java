package tech.erubin.annyeong_eat.telegramBot.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.ChequeDish;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;

import java.util.List;

public interface ChequeDishService {

    List<ChequeDish> getAllCheque();

    ChequeDish getChequeById(int id);

    ChequeDish getChequeByOrderAndDish(Order order, Dish dish);

    void saveCheque(ChequeDish chequeDish);

    void deleteCheque(ChequeDish chequeDish);

    void deleteAllChequesByOrder(Order order);

    ChequeDish createCheque(Order order, Dish dish);
}
