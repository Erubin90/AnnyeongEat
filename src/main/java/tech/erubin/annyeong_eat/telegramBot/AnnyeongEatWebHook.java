package tech.erubin.annyeong_eat.telegramBot;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.service.botService.UpdateService;

@NoArgsConstructor
@AllArgsConstructor
public class AnnyeongEatWebHook extends TelegramWebhookBot {

    private String botUsername;
    private String botToken;
    private String botPath;

    private UpdateService updateService;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        BotApiMethod<Message> botApiMethod = updateService.handleUpdate(update);
        System.out.println("конец");
        return botApiMethod;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }
}
