package tech.erubin.annyeong_eat.telegramBot.service.entityServiсes.serviceInterface;

import tech.erubin.annyeong_eat.telegramBot.entity.Cafe;

import java.util.List;
import java.util.Set;

public interface CafeService {

    List<Cafe> getAllCafe();

    Set<String> getAllCity();

    Cafe getCafeById(int id);

    Cafe getCafeByName(String name);

    List<String> getAllCafeNames();

    List<String> getCafeNameByCity(String city);

    void saveCafe(Cafe cafe);

    void deleteCafe(Cafe cafe);

    Cafe createCafe();
}
