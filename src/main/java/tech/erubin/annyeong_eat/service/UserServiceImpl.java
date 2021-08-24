package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.repository.UserRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User getClient(String userId, String userName) {
        User user = repository.findByTelegramUserId(userId);
        return user != null ? user : create(userId, userName);
    }

    @Override
    public void save(User user) {
        repository.save(user);
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }

    @Override
    public User create(String userId, String userName) {
        return new User(userId, userName);
    }
}
