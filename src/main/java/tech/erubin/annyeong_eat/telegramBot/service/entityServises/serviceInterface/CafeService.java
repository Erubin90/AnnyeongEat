package tech.erubin.annyeong_eat.telegramBot.service.entityServises.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Cafe;

import java.util.List;
import java.util.Set;

public interface CafeService {

    List<Cafe> getAllCafe();

    Set<String> getAllCity();

    Cafe getCafeById(int id);

    void saveCafe(Cafe cafe);

    void deleteCafe(Cafe cafe);

    Cafe createCafe(String name, String city, String address);
}
