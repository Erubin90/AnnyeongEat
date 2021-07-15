package tech.erubin.annyeong_eat.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.erubin.annyeong_eat.telegramBot.entity.Cafe;

import java.util.List;


public interface CafeRepository extends JpaRepository<Cafe, Integer> {
    List<Cafe> findAllByCity(String city);

    Cafe findCafeByName(String name);
}
