package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.ClientState;

public interface ClientStateService {
    ClientState getState(User user);

    void delete(ClientState clientState);

    void save(ClientState clientState);

    ClientState create(User user, String state);

    void createAndSave(User user, String state);
}
