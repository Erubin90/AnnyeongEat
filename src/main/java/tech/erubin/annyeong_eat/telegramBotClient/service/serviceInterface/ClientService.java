package tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBotClient.entity.Client;

public interface ClientService {

    Client getClientByTelegramUserId(String userid);

    void save(Client client);

    void delete(Client client);

    Client create(String telegramUserId);
}
