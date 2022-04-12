package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.entity.User;

import java.util.List;

public interface OrderService {

    Order getOrderByUser(User user);

    List<Order> getOrdersInProgress(User user);

    Order getOrderByUserIdAndCafeId(User user, Cafe cafe);

    Order getOrderByStringId(String orderId);

    Order getOrderById(int orderId);

    Order getOrderByOrderName(String orderName);

    void save(Order order);

    void delete(Order order);

    Order create(User user, Cafe cafe);
}
