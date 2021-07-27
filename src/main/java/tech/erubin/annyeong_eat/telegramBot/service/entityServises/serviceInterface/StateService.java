package tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.ClientStates;

import java.util.List;

public interface StateService {
    List<ClientStates> getAllStates();

    ClientStates getState(Client client);

    void deleteStates(ClientStates clientStates);

    void saveStates(ClientStates clientStates);

    ClientStates createStates(Client client);
}
