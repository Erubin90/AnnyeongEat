package tech.erubin.annyeong_eat.telegramBot.service.entityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.repository.DishRepository;
import tech.erubin.annyeong_eat.telegramBot.service.entityService.ServiceInterface.DishService;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
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
    public Dish createDish(String name, String type, Double price) {
        return new Dish(name, type, price);
    }
}
