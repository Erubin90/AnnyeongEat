package tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBotClient.entity.Order;
import tech.erubin.annyeong_eat.telegramBotClient.entity.OrderState;

public interface OrderStatesService {
    void save(OrderState orderState);

    void delete(OrderState orderState);

    OrderState create(Order order, String state);
}
