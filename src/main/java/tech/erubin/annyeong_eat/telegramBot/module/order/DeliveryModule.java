package tech.erubin.annyeong_eat.telegramBot.module.order;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.ReplyButtonServiceImpl;

@Component
public class DeliveryModule {
    ClientServiceImpl clientService;
    ReplyButtonServiceImpl replyButton;

    public BotApiMethod<?> startClient(Update update, Client client){
        SendMessage sendMessage = new SendMessage();
        Message message = update.getMessage();
        String sourceText = message.getText();
        String chatId = message.getChatId().toString();
        int messageId = message.getMessageId();
        String sate = client.getState();
        String text = "Ошибка OrderModule.startClient";

        switch (sate) {
            case "доставка улица":
                break;
            case "доставка номер":
                break;
            default:
                return new DeleteMessage(chatId, messageId);
        }
        if (sendMessage.getReplyMarkup() == null) {
            sendMessage.enableMarkdown(true);
            sendMessage.setReplyMarkup(replyButton.clientMainMenu());
        }
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        clientService.saveClient(client);
        return sendMessage;
    }
}
