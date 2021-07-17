package tech.erubin.annyeong_eat.telegramBot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackQueryHandler {

    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> botApiMethod = null;

        return botApiMethod;
    }
}
