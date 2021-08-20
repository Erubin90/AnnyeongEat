package tech.erubin.annyeong_eat.telegramBotClient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.erubin.annyeong_eat.telegramBotClient.entity.OrderState;

public interface OrderStatesRepository extends JpaRepository<OrderState, Integer> {
}
