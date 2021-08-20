package tech.erubin.annyeong_eat.telegramBotClient.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.telegramBotClient.entity.ChequeDish;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Dish;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Order;

@Repository
public interface ChequeDishRepository extends JpaRepository<ChequeDish, Integer> {
    ChequeDish findChequeByOrderIdAndDishId(Order orderId, Dish dishId);
}
