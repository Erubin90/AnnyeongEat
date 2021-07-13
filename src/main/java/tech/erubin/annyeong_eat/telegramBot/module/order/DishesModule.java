package tech.erubin.annyeong_eat.telegramBot.module.order;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;

@Component
public class DishesModule {
    public BotApiMethod<?> startClient(Update update, Client client){
        SendMessage sendMessage = new SendMessage();
        Message message = update.getMessage();
        String sourceText = message.getText();
        String chatId = message.getChatId().toString();
        int messageId = message.getMessageId();
        String sate = client.getState();
        String text = "Ошибка DishesModule.startClient";

        return sendMessage;
    }
}
