package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.Cafe;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.repository.OrderRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;

    @Override
    public Order getOrder(User user) {
        List<Order> orderList = user.getOrderList().stream()
                .filter(x -> x.getUsing() == 1)
                .collect(Collectors.toList());
        if (orderList.isEmpty()) {
            return null;
        }
        else {
            return orderList.get(0);
        }
    }

    @Override
    public Order getOrder(User user, Cafe cafe) {
        List<Integer> orderList = repository.getOrderByClientIdAndCafeId(cafe, user);
        if (orderList != null && !orderList.isEmpty()) {
            return repository.getById(orderList.get(0));
        }
        else {
            return createOrder(user, cafe);
        }
    }

    @Override
    public void save(Order order) {
        if (order != null) {
            repository.save(order);
        }
    }

    @Override
    public void delete(Order order) {
        repository.delete(order);
    }

    @Override
    public Order create(User user) {
        String orderName = String.format("заказ %s_%s", user.getId(), user.getOrderList().size() + 1);
        return new Order(user, orderName);
    }

    @Override
    public Order createOrder(User user, Cafe cafe) {
        String orderName = String.format("заказ %s_%s", user.getId(), user.getOrderList().size() + 1);
        return new Order(user, cafe, orderName);
    }
}
