package tech.erubin.annyeong_eat.telegramBot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.telegramBot.entity.Cheque;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;

@Repository
public interface ChequeRepository extends JpaRepository<Cheque, Integer> {
    Cheque  findChequeByOrderIdAndDishId(Order orderId, Dish dishId);
}
