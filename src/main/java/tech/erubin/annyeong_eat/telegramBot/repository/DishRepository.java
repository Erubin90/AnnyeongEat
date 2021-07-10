package tech.erubin.annyeong_eat.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;

public interface DishRepository extends JpaRepository<Dish, Integer> {
}
