package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.repository.UserStatesRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.UserStateService;
import tech.erubin.annyeong_eat.telegramBot.enums.UserEnum;

import java.util.List;

@Service
@AllArgsConstructor
public class UserStatesServiceImpl implements UserStateService {
    private final UserStatesRepository repository;

    @Override
    public UserState getState(User user) {
        List<UserState> userStateList = user.getUserStateList();
        if (userStateList != null){
            return userStateList.get(userStateList.size() - 1);
        }
        else {
            return create(user, UserEnum.REGISTRATION_START.getValue());
        }
    }

    @Override
    public void delete(UserState userState) {
        repository.delete(userState);
    }

    @Override
    public void save(UserState userState) {
        if (userState != null) {
            repository.save(userState);
        }
    }

    @Override
    public UserState create(User user, String state) {
        return new UserState(user, state);
    }
}
