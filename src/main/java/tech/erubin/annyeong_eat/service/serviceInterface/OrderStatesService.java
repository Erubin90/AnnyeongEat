package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.entity.OrderState;

public interface OrderStatesService {
    void save(OrderState orderState);

    void delete(OrderState orderState);

    OrderState create(Order order, String state);
}
