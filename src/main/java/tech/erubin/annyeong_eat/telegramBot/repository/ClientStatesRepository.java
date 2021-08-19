package tech.erubin.annyeong_eat.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.ClientState;

import java.util.List;

@Repository
public interface ClientStatesRepository extends JpaRepository<ClientState, Integer> {

    List<ClientState> getClientStateByClientId(Client client);
}
