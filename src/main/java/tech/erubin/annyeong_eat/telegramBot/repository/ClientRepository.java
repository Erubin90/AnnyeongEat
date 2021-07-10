package tech.erubin.annyeong_eat.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Integer>{
    Client findByTelegramUserId(String userId);
}
