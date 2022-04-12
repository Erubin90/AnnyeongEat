package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.User;

public interface UserService {

    User getOrCreateUser(String userid);

    User getUser(int id);

    void save(User user);

    void delete(User user);

    User create(String telegramUserId);
}
