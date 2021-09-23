package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.User;

public interface UserService {

    User getUser(String userid);

    void save(User user);

    void delete(User user);

    User create(String telegramUserId);
}
