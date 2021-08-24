package tech.erubin.annyeong_eat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.erubin.annyeong_eat.entity.ChequeDish;
import tech.erubin.annyeong_eat.entity.Dish;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.repository.ChequeDishRepository;
import tech.erubin.annyeong_eat.service.serviceInterface.ChequeDishService;

@Service
@AllArgsConstructor
public class ChequeDishServiceImpl implements ChequeDishService {
    private final ChequeDishRepository repository;

    @Override
    public ChequeDish getChequeByOrderAndDish(Order order, Dish dish) {
        ChequeDish chequeDish = repository.findChequeByOrderIdAndDishId(order, dish);
        if (chequeDish == null) {
            chequeDish = create(order, dish);
        }
        return chequeDish;
    }

    @Override
    public void save(ChequeDish chequeDish) {
        repository.save(chequeDish);
    }

    @Override
    public void delete(ChequeDish chequeDish) {
        repository.delete(chequeDish);
    }

    @Override
    public ChequeDish create(Order order, Dish dish) {
        return new ChequeDish(order, dish);
    }
}
