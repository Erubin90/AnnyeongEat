package tech.erubin.annyeong_eat.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.erubin.annyeong_eat.bot.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
