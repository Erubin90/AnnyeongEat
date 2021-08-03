package tech.erubin.annyeong_eat.telegramBot;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.erubin.annyeong_eat.telegramBot.handler.CallbackQueryHandler;
import tech.erubin.annyeong_eat.telegramBot.handler.MessageHandler;

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

    public boolean sendPhoto(String chatId, String url, String text, InlineKeyboardMarkup inlineKeyboardMarkup){
        InputFile inputFile = new InputFile(url);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setCaption(text);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendPhoto);
            return true;
        } catch (TelegramApiException e) {
            return false;
        }
    }

    public boolean updateMarkups(String chatId, int messageId, String text,
                                 InlineKeyboardMarkup inlineMarkup, ReplyKeyboardMarkup replyMarkup) {
        EditMessageReplyMarkup editMessage =
                new EditMessageReplyMarkup(chatId, messageId, null , inlineMarkup);
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
