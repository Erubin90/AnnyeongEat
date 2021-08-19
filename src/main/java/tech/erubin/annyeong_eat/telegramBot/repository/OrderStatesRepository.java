package tech.erubin.annyeong_eat.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.entity.OrderState;

public interface OrderStatesRepository extends JpaRepository<OrderState, Integer> {

    OrderState getOrderStateByOrderIdAndState(Order order, String state);
}
