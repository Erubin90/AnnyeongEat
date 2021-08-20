package tech.erubin.annyeong_eat.telegramBotClient;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.erubin.annyeong_eat.telegramBotClient.module.handler.CallbackQueryHandler;
import tech.erubin.annyeong_eat.telegramBotClient.module.handler.MessageHandler;

@NoArgsConstructor
@AllArgsConstructor
public class AnnyeongEatWebHook extends TelegramWebhookBot {
    private String botUsername;
    private String botToken;
    private String botPath;

    private MessageHandler messageHandler;
    private CallbackQueryHandler callbackQueryHandler;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        BotApiMethod<?> botApiMethod = null;
        if (update.hasMessage()) {
            botApiMethod = messageHandler.handleUpdate(update);
        }
        else if (update.hasCallbackQuery()) {
            botApiMethod = callbackQueryHandler.handleUpdate(update.getCallbackQuery());
        }
        return botApiMethod;
    }

    public boolean sendPhoto(String chatId, String text, String imgPath, InlineKeyboardMarkup inlineKeyboardMarkup){
        InputFile inputFile = new InputFile(imgPath);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(text);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendPhoto);
            return true;
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMarkups(EditMessageReplyMarkup editMessage) {
        try {
            execute(editMessage);
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
