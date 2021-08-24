package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.Order;

public interface OrderService {

    Order getOrder(User user);

    Order getOrder(User user, Cafe cafe);

    void save(Order order);

    void delete(Order order);

    Order create(User user);

    Order createOrder(User user, Cafe cafe);

}
