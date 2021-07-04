package tech.erubin.annyeong_eat.bot.service.entityService.ServiceInterface;

import tech.erubin.annyeong_eat.bot.entity.Cheque;
import tech.erubin.annyeong_eat.bot.entity.Dish;
import tech.erubin.annyeong_eat.bot.entity.Order;

import java.util.List;

public interface ChequeService {

    List<Cheque> getAllCheque();

    Cheque getChequeById(int id);

    void saveCheque(Cheque cheque);

    void deleteCheque(Cheque cheque);

    Cheque createCheque(Order order, Dish dish, Integer countDishes);
}
