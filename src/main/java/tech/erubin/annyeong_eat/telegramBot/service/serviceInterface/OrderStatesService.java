package tech.erubin.annyeong_eat.telegramBot.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.entity.OrderState;

public interface OrderStatesService {

    OrderState getState(Order order);

    void save(OrderState orderState);

    void delete(OrderState orderState);

    OrderState create(Order order);
}
