package tech.erubin.annyeong_eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.entity.OrderState;

import java.util.List;

@Repository
public interface OrderStatesRepository extends JpaRepository<OrderState, Integer> {

    List<OrderState> getOrderStateByOrderId(Order order);
}
