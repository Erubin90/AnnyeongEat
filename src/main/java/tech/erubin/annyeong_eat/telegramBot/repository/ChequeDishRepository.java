package tech.erubin.annyeong_eat.telegramBot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.telegramBot.entity.ChequeDish;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;

@Repository
public interface ChequeDishRepository extends JpaRepository<ChequeDish, Integer> {
    ChequeDish findChequeByOrderIdAndDishId(Order orderId, Dish dishId);
}
