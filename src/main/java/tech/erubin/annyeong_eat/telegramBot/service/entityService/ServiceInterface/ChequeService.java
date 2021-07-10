package tech.erubin.annyeong_eat.telegramBot.service.entityService.ServiceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Cheque;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;

import java.util.List;

public interface ChequeService {

    List<Cheque> getAllCheque();

    Cheque getChequeById(int id);

    void saveCheque(Cheque cheque);

    void deleteCheque(Cheque cheque);

    Cheque createCheque(Order order, Dish dish, Integer countDishes);
}
