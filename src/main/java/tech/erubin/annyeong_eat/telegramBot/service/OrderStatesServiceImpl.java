package tech.erubin.annyeong_eat.telegramBot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.entity.OrderState;
import tech.erubin.annyeong_eat.telegramBot.repository.OrderStatesRepository;
import tech.erubin.annyeong_eat.telegramBot.service.serviceInterface.OrderStatesService;

@Service
@AllArgsConstructor
public class OrderStatesServiceImpl implements OrderStatesService {
    private final OrderStatesRepository repository;

    @Override
    public void save(OrderState orderState) {
        if (orderState != null) {
            repository.save(orderState);
        }
    }

    @Override
    public void delete(OrderState orderState) {
        repository.delete(orderState);
    }

    @Override
    public OrderState create(Order order, String state) {
        return new OrderState(order, state);
    }
}
