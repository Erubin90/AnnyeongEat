package tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBotClient.entity.Client;
import tech.erubin.annyeong_eat.telegramBotClient.entity.ClientState;

public interface ClientStateService {
    ClientState getState(Client client);

    void delete(ClientState clientState);

    void save(ClientState clientState);

    ClientState create(Client client, String state);
}
