package tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBotClient.entity.Client;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Order;

public interface OrderService {

    Order getOrder(Client client);

    void save(Order order);

    void delete(Order order);

    Order create(Client client);

}
