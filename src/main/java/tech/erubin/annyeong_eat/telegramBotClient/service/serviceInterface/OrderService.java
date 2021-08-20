package tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBotClient.entity.Cafe;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Client;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Order;

public interface OrderService {

    Order getOrder(Client client);

    Order getOrder(Client client, Cafe cafe);

    void save(Order order);

    void delete(Order order);

    Order create(Client client);

    Order createOrder(Client client, Cafe cafe);

}
