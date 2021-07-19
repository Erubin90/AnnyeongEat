package tech.erubin.annyeong_eat.telegramBot.module.registration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.CafeServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class RegistrationButtonName {

    @Value("${registration.button.clientOrEmployee.client}")
    private String orClient;

    @Value("${registration.button.clientOrEmployee.employee}")
    private String orEmployee;

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

    public List<String> getClientOrEmployeeRegistration() {
        return List.of(orClient, orEmployee);
    }
}
