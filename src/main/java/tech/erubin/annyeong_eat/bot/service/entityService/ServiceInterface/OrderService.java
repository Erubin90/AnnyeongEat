package tech.erubin.annyeong_eat.bot.service.entityService.ServiceInterface;

import tech.erubin.annyeong_eat.bot.entity.Client;
import tech.erubin.annyeong_eat.bot.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrder();

    Order getOrderById(int id);

    void saveOrder(Order order);

    void deleteOrder(Order order);

    Order createOrder(Client client, String orderName, String address, String comment, String paymentMethod);
}
