package tech.erubin.annyeong_eat.telegramBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;

@RestController
public class ControllerBot {
    @Autowired
    private final AnnyeongEatWebHook bot;

    public ControllerBot(AnnyeongEatWebHook bot) {
        this.bot = bot;
    }

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        if (update.getMessage()!= null) {
            return bot.onWebhookUpdateReceived(update);
        }
        return null;
    }

}
