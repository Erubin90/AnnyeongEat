package tech.erubin.annyeong_eat.telegramBot;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.erubin.annyeong_eat.telegramBot.handler.MessageHandler;

@NoArgsConstructor
@AllArgsConstructor
public class AnnyeongEatWebHook extends TelegramWebhookBot {
    private String botUsername;
    private String botToken;
    private String botPath;

    private MessageHandler messageHandler;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        BotApiMethod<?> botApiMethod = messageHandler.handleUpdate(update);
        return botApiMethod;
    }

    public boolean sendPhoto(String chatId, String url, String text){
        InputFile inputFile = new InputFile(url);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setCaption(text);
        try {
            execute(sendPhoto);
            return true;
        } catch (TelegramApiException e) {
            return false;
        }

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
