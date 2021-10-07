package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.ChequeDish;
import tech.erubin.annyeong_eat.entity.Dish;
import tech.erubin.annyeong_eat.entity.Order;

public interface ChequeDishService {

    ChequeDish getChequeByOrderAndDish(Order order, Dish dish);

    void save(ChequeDish chequeDish);

    void delete(ChequeDish chequeDish);

    void saveOrDeleteChequeDish(ChequeDish chequeDish, int count);

    ChequeDish create(Order order, Dish dish);
}
