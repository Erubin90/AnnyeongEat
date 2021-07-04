package tech.erubin.annyeong_eat.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.bot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.dateBase.service.Servis;

@Controller
public class ControllerBot {
    private final AnnyeongEatWebHook bot;

    @Autowired
    private Servis servis;

    public ControllerBot(AnnyeongEatWebHook bot) {
        this.bot = bot;
    }

    @RequestMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }

}
