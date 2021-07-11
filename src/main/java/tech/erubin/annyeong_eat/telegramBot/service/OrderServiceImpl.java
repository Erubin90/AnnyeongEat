package tech.erubin.annyeong_eat.telegramBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.repository.OrderRepository;
import tech.erubin.annyeong_eat.telegramBot.service.ServiceInterface.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository repository;

    @Override
    public List<Order> getAllOrder() {
        return repository.findAll();
    }

    @Override
    public Order getOrderById(int id) {
        return repository.getById(id);
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
    public Order createOrder(Client client, String orderName, String address, String comment, String paymentMethod) {
        return new Order(client, orderName, address, comment, paymentMethod);
    }
}
