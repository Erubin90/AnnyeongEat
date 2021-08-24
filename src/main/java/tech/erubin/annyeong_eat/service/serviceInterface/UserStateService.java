package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;

public interface UserStateService {
    UserState getState(User user);

    void delete(UserState userState);

    void save(UserState userState);

    UserState create(User user, String state);
}
