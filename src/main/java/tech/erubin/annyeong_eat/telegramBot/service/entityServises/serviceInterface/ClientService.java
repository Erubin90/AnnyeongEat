package tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClient();

    Client getClientById(int id);

    Client getClientByTelegramUserId(String userid);

    void saveClient(Client client);

    void deleteClient(Client client);

    Client createClient(String telegramUserId);
}
