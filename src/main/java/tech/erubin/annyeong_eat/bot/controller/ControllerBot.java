package tech.erubin.annyeong_eat.bot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.bot.AnnyeongEatWebHook;

@Controller
public class ControllerBot {
    private final AnnyeongEatWebHook bot;

    public ControllerBot(AnnyeongEatWebHook bot) {
        this.bot = bot;
    }

    @RequestMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }

}
