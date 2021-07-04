package tech.erubin.annyeong_eat.bot.service.entityService.ServiceInterface;

import tech.erubin.annyeong_eat.bot.entity.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClient();

    Client getClientById(int id);

    void saveClient(Client client);

    void deleteClient(Client client);

    Client createClient(String name, String surname, String telegramName, String phoneNumber);
}
