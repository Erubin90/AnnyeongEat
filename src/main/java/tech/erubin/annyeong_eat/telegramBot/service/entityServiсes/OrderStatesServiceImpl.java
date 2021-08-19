package tech.erubin.annyeong_eat.telegramBot.service.entityServiсes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.entity.OrderState;
import tech.erubin.annyeong_eat.telegramBot.repository.OrderStatesRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityServiсes.serviceInterface.OrderStatesService;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderStatesServiceImpl implements OrderStatesService {
    private final OrderStatesRepository repository;

    @Override
    public OrderState getState(Order order) {
        if (order == null) {
            return null;
        }
        else {
            List<OrderState> orderStates = order.getOrderStateList();
            if (orderStates != null && !orderStates.isEmpty()) {
                return orderStates.get(orderStates.size() - 1);
            }
            return create(order);
        }
    }

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
    public OrderState create(Order order) {
        return new OrderState(order);
    }
}
