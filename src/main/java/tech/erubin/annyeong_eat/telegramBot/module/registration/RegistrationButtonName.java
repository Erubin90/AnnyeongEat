package tech.erubin.annyeong_eat.telegramBot.module.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.CafeServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class RegistrationButtonName {
    private CafeServiceImpl cafeService;

    public List<List<String>> getCityList() {
        List<List<String>> buttonNames = new ArrayList<>();
        Set<String> cafeCits = getAllCitySetList();
        for (String cafeCity : cafeCits) {
            buttonNames.add(List.of(cafeCity));
        }
        return buttonNames;
    }

    public Set<String> getAllCitySetList() {
        return cafeService.getAllCity();
    }
}
