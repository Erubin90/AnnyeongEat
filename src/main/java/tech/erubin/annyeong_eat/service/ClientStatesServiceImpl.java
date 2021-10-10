package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.ClientState;
import tech.erubin.annyeong_eat.repository.ClientStatesRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.ClientStateService;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientStatesServiceImpl implements ClientStateService {
    private final ClientStatesRepository repository;

    @Override
    public ClientState getState(User user) {
        List<ClientState> clientStateList = user.getClientStateList();
        if (clientStateList != null){
            return clientStateList.get(clientStateList.size() - 1);
        }
        else {
            return create(user, ClientEnum.REGISTRATION_START.getValue());
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
    public ClientState create(User user, String state) {
        return new ClientState(user, state);
    }

    @Override
    public void createAndSave(User user, String state) {
        repository.save(create(user, state));
    }
}
