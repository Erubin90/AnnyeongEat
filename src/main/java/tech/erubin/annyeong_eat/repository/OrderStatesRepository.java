package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.erubin.annyeong_eat.entity.OrderState;

public interface OrderStatesRepository extends JpaRepository<OrderState, Integer> {
}
