package tech.erubin.annyeong_eat.telegramBotClient.service.serviceInterface;

import tech.erubin.annyeong_eat.telegramBotClient.entity.Cafe;

import java.util.List;
import java.util.Set;

public interface CafeService {

    Set<String> getAllCity();

    Cafe getCafeByName(String name);

    List<String> getAllCafeNames();

    List<String> getCafeNameByCity(String city);

    void save(Cafe cafe);

    void delete(Cafe cafe);

    Cafe create();
}
