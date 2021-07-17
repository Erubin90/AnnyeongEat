package tech.erubin.annyeong_eat.telegramBot.service.entityServises;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.repository.OrderRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface.OrderService;

import java.util.List;

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
        List<Order> orderList = client.getOrderList();
        if (orderList.isEmpty()) {
            return createOrder(client);
        }
        else {
            Order lastOrder = orderList.get(orderList.size() - 1);
            String orderStatus = lastOrder.getOrderStatus();
            if (orderStatus.equals("оформление")) {
                return lastOrder;
            }
            else {
                return createOrder(client);
            }
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
}
