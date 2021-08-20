package tech.erubin.annyeong_eat.telegramBotClient.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Client;
import tech.erubin.annyeong_eat.telegramBotClient.entity.ClientState;
import tech.erubin.annyeong_eat.telegramBotClient.repository.ClientStatesRepository;
import tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface.ClientStateService;
import tech.erubin.annyeong_eat.telegramBotClient.states.ClientStateEnum;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientStatesServiceImpl implements ClientStateService {
    private final ClientStatesRepository repository;

    @Override
    public ClientState getState(Client client) {
        List<ClientState> clientStateList = client.getClientStateList();
        int size = clientStateList.size();
        if (size > 0){
            return clientStateList.get(size - 1);
        }
        else {
            return create(client, ClientStateEnum.REGISTRATION_START.getValue());
        }
    }

    @Override
    public void delete(ClientState clientState) {
        repository.delete(clientState);
    }

    @Override
    public void save(ClientState clientState) {
        if (clientState != null) {
            repository.save(clientState);
        }
    }

    @Override
    public ClientState create(Client client, String state) {
        return new ClientState(client, state);
    }
}
