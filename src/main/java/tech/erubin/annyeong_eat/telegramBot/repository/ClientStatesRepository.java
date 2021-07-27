package tech.erubin.annyeong_eat.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.telegramBot.entity.ClientStates;

@Repository
public interface ClientStatesRepository extends JpaRepository<ClientStates, Integer> {
}
