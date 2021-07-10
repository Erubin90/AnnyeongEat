package tech.erubin.annyeong_eat.telegramBot.service.entityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.repository.ClientRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityService.ServiceInterface.ClientService;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository repository;

    @Override
    public List<Client> getAllClient() {
        return repository.findAll();
    }

    @Override
    public Client getClientById(int id) {
        return repository.getById(id);
    }

    @Override
    public Client getClientByTelegramUserId(String userid) {
        return repository.findByTelegramUserId(userid);
    }

    @Override
    public void saveClient(Client client) {
        repository.save(client);
    }

    @Override
    public void deleteClient(Client client) {
        repository.delete(client);
    }

    @Override
    public Client createClient(String name, String surname, String telegramUserId, String phoneNumber) {
        return new Client(name, surname, telegramUserId, phoneNumber);
    }

    @Override
    public Client createClient(String telegramUserId) {
        return new Client(telegramUserId);
    }
}