package tech.erubin.annyeong_eat.service.serviceInterface;

import tech.erubin.annyeong_eat.entity.DishOptionally;

public interface DishOptionallyService {

    void save(DishOptionally dishOptionally);

    void delete(DishOptionally dishOptionally);

    DishOptionally create();
}
