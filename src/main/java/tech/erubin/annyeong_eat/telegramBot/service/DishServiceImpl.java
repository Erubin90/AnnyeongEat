package tech.erubin.annyeong_eat.telegramBot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.repository.DishRepository;
import tech.erubin.annyeong_eat.telegramBot.service.serviceInterface.DishService;

import java.util.List;

@Service
@AllArgsConstructor
public class DishServiceImpl implements DishService {
    private DishRepository repository;

    @Override
    public List<Dish> getAllDish() {
        return repository.findAll();
    }

    @Override
    public Dish getDishById(int id) {
        return repository.getById(id);
    }

    @Override
    public void saveDish(Dish dish) {
        repository.save(dish);
    }

    @Override
    public void deleteDish(Dish dish) {
        repository.delete(dish);
    }

    @Override
    public List<Dish> getDishListByType(String type) {
        return repository.findDishByType(type);
    }

    @Override
    public Dish getDishByTag(String tag) {
        return repository.findDishByTag(tag);
    }

    @Override
    public Dish createDish() {
        return new Dish();
    }
}
