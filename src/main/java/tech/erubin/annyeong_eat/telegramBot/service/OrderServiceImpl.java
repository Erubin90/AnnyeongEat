package tech.erubin.annyeong_eat.telegramBot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Cafe;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.repository.OrderRepository;
import tech.erubin.annyeong_eat.telegramBot.service.serviceInterface.OrderService;
import tech.erubin.annyeong_eat.telegramBot.states.OrderStateEnum;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;

    @Override
    public List<Order> getAllOrder() {
        return repository.findAll();
    }

    @Override
    public Order getOrderById(int id) {
        return repository.getById(id);
    }

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

    public Order getOrder(Client client, Cafe cafe) {
        List<Integer> orderList = repository.getOrderByClientIdAndCafeIdAndOrderState(
                OrderStateEnum.ORDER_START_REGISTRATION.getValue(), cafe, client);
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
    public void deleteOrder(Order order) {
        repository.delete(order);
    }

    @Override
    public Order createOrder(Client client) {
        String orderName = String.format("заказ %s_%s", client.getId(), client.getOrderList().size() + 1);
        return new Order(client, orderName);
    }

    public Order createOrder(Client client, Cafe cafe) {
        String orderName = String.format("заказ %s_%s", client.getId(), client.getOrderList().size() + 1);
        return new Order(client, cafe, orderName);
    }
}
