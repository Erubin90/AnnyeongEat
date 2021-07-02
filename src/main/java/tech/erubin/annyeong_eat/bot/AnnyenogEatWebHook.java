package tech.erubin.annyeong_eat.bot;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnyenogEatWebHook extends TelegramWebhookBot {

    private String botUsername;
    private String botToken;
    private String botPath;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        return null;
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
