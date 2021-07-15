package tech.erubin.annyeong_eat.telegramBot.service.entityServises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.DishOptionally;
import tech.erubin.annyeong_eat.telegramBot.repository.DishOptionallyRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface.DishOptionallyService;

import java.util.List;

@Service
public class DishOptionallyServiceImpl implements DishOptionallyService {

    @Autowired
    private DishOptionallyRepository repository;

    @Override
    public List<DishOptionally> getAllDishOptionally() {
        return repository.findAll();
    }

    @Override
    public DishOptionally getDishOptionallyById(int id) {
        return repository.getById(id);
    }

    @Override
    public void saveDishOptionally(DishOptionally dishOptionally) {
        repository.save(dishOptionally);
    }

    @Override
    public void deleteDishOptionally(DishOptionally dishOptionally) {
        repository.delete(dishOptionally);
    }

    @Override
    public DishOptionally createDishOptionally() {
        return new DishOptionally();
    }
}
