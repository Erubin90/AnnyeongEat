package tech.erubin.annyeong_eat.telegramBot.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrder();

    Order getOrderById(int id);

    Order getOrder(Client client);

    void save(Order order);

    void deleteOrder(Order order);

    Order createOrder(Client client);

}
