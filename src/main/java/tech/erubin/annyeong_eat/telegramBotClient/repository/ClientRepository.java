package tech.erubin.annyeong_eat.telegramBotClient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>{
    Client findByTelegramUserId(String userId);
}
