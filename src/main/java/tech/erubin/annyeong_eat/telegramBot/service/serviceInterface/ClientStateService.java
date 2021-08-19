package tech.erubin.annyeong_eat.telegramBot.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.ClientState;

import java.util.List;

public interface ClientStateService {
    List<ClientState> getAllStates();

    ClientState getState(Client client);

    void delete(ClientState clientState);

    void save(ClientState clientState);

    ClientState create(Client client);
}
