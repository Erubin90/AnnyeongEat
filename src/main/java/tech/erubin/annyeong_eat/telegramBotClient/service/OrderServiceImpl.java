package tech.erubin.annyeong_eat.telegramBotClient.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Cafe;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Client;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Order;
import tech.erubin.annyeong_eat.telegramBotClient.repository.OrderRepository;
import tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;

    @Override
    public Order getOrder(Client client) {
        List<Order> orderList = client.getOrderList().stream()
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
    public Order getOrder(Client client, Cafe cafe) {
        List<Integer> orderList = repository.getOrderByClientIdAndCafeId(cafe, client);
        if (orderList != null && !orderList.isEmpty()) {
            return repository.getById(orderList.get(0));
        }
        else {
            return createOrder(client, cafe);
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
    public Order create(Client client) {
        String orderName = String.format("заказ %s_%s", client.getId(), client.getOrderList().size() + 1);
        return new Order(client, orderName);
    }

    @Override
    public Order createOrder(Client client, Cafe cafe) {
        String orderName = String.format("заказ %s_%s", client.getId(), client.getOrderList().size() + 1);
        return new Order(client, cafe, orderName);
    }
}
