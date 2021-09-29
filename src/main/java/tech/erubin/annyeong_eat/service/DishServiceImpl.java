package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.Dish;
import tech.erubin.annyeong_eat.repository.DishRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.DishService;

import java.util.List;

@Service
@AllArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishRepository repository;

    @Override
    public Dish getDishById(String dishId) {
        return dishId.matches("\\d+") ? repository.getById(Integer.parseInt(dishId)) : null;
    }

    @Override
    public void save(Dish dish) {
        repository.save(dish);
    }

    @Override
    public void delete(Dish dish) {
        repository.delete(dish);
    }

    @Override
    public List<Dish> getDishListByType(String type) {
        return repository.findDishByType(type);
    }

    @Override
    public Dish create() {
        return new Dish();
    }
}
