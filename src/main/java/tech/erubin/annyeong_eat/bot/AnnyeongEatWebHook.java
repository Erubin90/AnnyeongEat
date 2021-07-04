package tech.erubin.annyeong_eat.bot;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnyeongEatWebHook extends TelegramWebhookBot {

    private String botUsername;
    private String botToken;
    private String botPath;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        String telegramUserId = update.getMessage().getFrom().getId().toString();
        String chatId = update.getMessage().getChatId().toString();
        String status = update.getMessage().toString();
        Message message = update.getMessage();

        if(message != null & message.hasText()) {
            try {
                execute(new SendMessage(chatId, telegramUserId));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        try {
            execute(new SendMessage(chatId, status));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
