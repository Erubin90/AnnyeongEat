package tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Cheque;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;

import java.util.List;

public interface ChequeService {

    List<Cheque> getAllCheque();

    Cheque getChequeById(int id);

    Cheque getChequeByOrderAndDish(Order order, Dish dish);

    void saveCheque(Cheque cheque);

    void deleteCheque(Cheque cheque);

    void deleteAllChequesByOrder(Order order);

    Cheque createCheque(Order order, Dish dish);
}
