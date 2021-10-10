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
    public Order getOrderByUser(User user) {
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
    public Order getOrderByUserIdAndCafeId(User user, Cafe cafe) {
        Integer orderId = repository.getOrderByUserIdAndCafeId(cafe, user);
        if (orderId != null) {
            return repository.getById(orderId);
        }
        else {
            return create(user, cafe);
        }
    }

    @Override
    public Order getOrderByStringId(String orderId) {
        return orderId.matches("\\d+") ? repository.getById(Integer.parseInt(orderId)) : null;
    }

    @Override
    public Order getOrderById(int orderId) {
        return repository.getById(orderId);
    }

    @Override
    public Order getOrderByOrderName(String orderName) {
        return repository.getOrderByOrderName(orderName);
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
    public Order create(User user, Cafe cafe) {
        String orderName = String.format("заказ %s_%s", user.getId(), user.getOrderList().size() + 1);
        return new Order(user, cafe, orderName);
    }
}
