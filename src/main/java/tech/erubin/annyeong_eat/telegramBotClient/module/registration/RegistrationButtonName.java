package tech.erubin.annyeong_eat.telegramBotClient.module.registration;

import lombok.Getter;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBotClient.service.CafeServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class RegistrationButtonName {
    private final CafeServiceImpl cafeService;

    public RegistrationButtonName(CafeServiceImpl cafeService) {
        this.cafeService = cafeService;
    }

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
