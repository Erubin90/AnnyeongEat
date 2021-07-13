package tech.erubin.annyeong_eat.telegramBot.module.mainMenu;

import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBot.module.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.messages.TextMessages;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.CafeServiceImpl;

@Component
public class CheckMessageMainMenuModule extends CheckMessage {
    public CheckMessageMainMenuModule(TextMessages message, CafeServiceImpl cafeService) {
        super(message, cafeService);
    }
}
