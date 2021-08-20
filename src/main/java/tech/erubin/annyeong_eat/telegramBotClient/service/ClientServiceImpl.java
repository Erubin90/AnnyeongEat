package tech.erubin.annyeong_eat.telegramBotClient.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Client;
import tech.erubin.annyeong_eat.telegramBotClient.repository.ClientRepository;
import tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface.ClientService;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;

    @Override
    public Client getClientByTelegramUserId(String userid) {
        return repository.findByTelegramUserId(userid);
    }

    @Override
    public void save(Client client) {
        repository.save(client);
    }

    @Override
    public void delete(Client client) {
        repository.delete(client);
    }

    @Override
    public Client create(String telegramUserId) {
        return new Client(telegramUserId);
    }
}
