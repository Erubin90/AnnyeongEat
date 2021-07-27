package tech.erubin.annyeong_eat.telegramBot.service.entityServises;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.ClientStates;
import tech.erubin.annyeong_eat.telegramBot.repository.ClientStatesRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface.StateService;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientStatesServiceImpl implements StateService {
    private final ClientStatesRepository repository;

    @Override
    public List<ClientStates> getAllStates() {
        return repository.findAll();
    }

    @Override
    public ClientStates getState(Client client) {
        List<ClientStates> clientStatesList = client.getClientStatesList();
        int size = clientStatesList.size();
        if (size > 0){
            return clientStatesList.get(size - 1);
        }
        else {
            return createStates(client);
        }
    }

    @Override
    public void deleteStates(ClientStates clientStates) {
        repository.delete(clientStates);
    }

    @Override
    public void saveStates(ClientStates clientStates) {
        ClientStates newClientStates = createStates(clientStates.getClientId());
        newClientStates.setState(clientStates.getState());
        repository.save(newClientStates);
    }

    @Override
    public ClientStates createStates(Client client) {
        return new ClientStates(client);
    }
}
