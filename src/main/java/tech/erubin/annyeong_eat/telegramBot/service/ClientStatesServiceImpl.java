package tech.erubin.annyeong_eat.telegramBot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.ClientState;
import tech.erubin.annyeong_eat.telegramBot.repository.ClientStatesRepository;
import tech.erubin.annyeong_eat.telegramBot.service.serviceInterface.ClientStateService;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientStatesServiceImpl implements ClientStateService {
    private final ClientStatesRepository repository;

    @Override
    public List<ClientState> getAllStates() {
        return repository.findAll();
    }

    @Override
    public ClientState getState(Client client) {
        List<ClientState> clientStateList = client.getClientStateList();
        int size = clientStateList.size();
        if (size > 0){
            return clientStateList.get(size - 1);
        }
        else {
            return create(client);
        }
    }

    @Override
    public void delete(ClientState clientState) {
        repository.delete(clientState);
    }

    @Override
    public void save(ClientState clientState) {
        if (clientState != null) {
            ClientState newClientState = create(clientState.getClientId());
            newClientState.setState(clientState.getState());
            repository.save(newClientState);
        }
    }

    @Override
    public ClientState create(Client client) {
        return new ClientState(client);
    }
}
