package tech.erubin.annyeong_eat.telegramBot.module.mainMenu;

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
    private String mainMenuButtonClientOrder;

    @Value("${mainMenu.button.client.checkOrder}")
    private String mainMenuButtonClientCheckOrder;

    @Value("${mainMenu.button.client.help}")
    private String mainMenuButtonClientHelp;

    @Value("${mainMenu.button.client.info}")
    private String mainMenuButtonClientInfo;

    @Value("${button.back}")
    private String buttonBack;

    public List<String> getMainMenuClientButton(){
        return List.of(mainMenuButtonClientOrder, mainMenuButtonClientCheckOrder, mainMenuButtonClientInfo, mainMenuButtonClientHelp);
    }

    public List<String> getHelpClientButton(){
        return List.of(buttonBack);
    }

    public List<String> getCheckOrderButton(){
        return List.of(buttonBack);
    }

    public List<String> getProfileInfoButton(){
        return List.of(buttonBack);
    }
}
