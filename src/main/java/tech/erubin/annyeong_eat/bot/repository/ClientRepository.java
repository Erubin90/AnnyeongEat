package tech.erubin.annyeong_eat.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.erubin.annyeong_eat.bot.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Integer>{
}
