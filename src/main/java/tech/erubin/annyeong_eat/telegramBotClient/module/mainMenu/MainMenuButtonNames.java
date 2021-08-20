package tech.erubin.annyeong_eat.telegramBotClient.module.mainMenu;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class MainMenuButtonNames {

    @Value("${mainMenu.button.client.order}")
    private String createOrder;

    @Value("${mainMenu.button.client.checkOrder}")
    private String checkOrder;

    @Value("${mainMenu.button.client.help}")
    private String help;

    @Value("${mainMenu.button.client.info}")
    private String clientInfo;

    @Value("${button.back}")
    private String back;

    public List<String> getMainMenuClientButton(){
        return List.of(createOrder, checkOrder, clientInfo, help);
    }

    public List<String> getHelpClientButton(){
        return List.of(back);
    }

    public List<String> getCheckOrderButton(){
        return List.of(back);
    }

    public List<String> getProfileInfoButton(){
        return List.of(back);
    }

}
