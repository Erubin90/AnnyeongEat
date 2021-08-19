package tech.erubin.annyeong_eat.telegramBot.service.entityServiсes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.repository.ClientRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityServiсes.serviceInterface.ClientService;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
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
    public Client createClient(String telegramUserId) {
        return new Client(telegramUserId);
    }
}
