package tech.erubin.annyeong_eat.telegramBotClient.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBotClient.entity.DishOptionally;
import tech.erubin.annyeong_eat.telegramBotClient.repository.DishOptionallyRepository;
import tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface.DishOptionallyService;

@Service
@AllArgsConstructor
public class DishOptionallyServiceImpl implements DishOptionallyService {
    private final DishOptionallyRepository repository;

    @Override
    public void save(DishOptionally dishOptionally) {
        repository.save(dishOptionally);
    }

    @Override
    public void delete(DishOptionally dishOptionally) {
        repository.delete(dishOptionally);
    }

    @Override
    public DishOptionally create() {
        return new DishOptionally();
    }
}
