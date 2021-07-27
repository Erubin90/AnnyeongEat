package tech.erubin.annyeong_eat.telegramBot.service.entityServises;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Cafe;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.repository.OrderRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface.OrderService;

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
        List<Order> orderList = repository.findOrderByClientIdAndCafeId(client, cafe).stream()
                .filter(x -> x.getOrderStatus().equals("оформление"))
                .collect(Collectors.toList());
        System.err.println(orderList);
        if (orderList.isEmpty()) {
            return createOrder(client, cafe);
        }
        else {
            return orderList.get(0);
        }
    }

    @Override
    public void saveOrder(Order order) {
        repository.save(order);
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
