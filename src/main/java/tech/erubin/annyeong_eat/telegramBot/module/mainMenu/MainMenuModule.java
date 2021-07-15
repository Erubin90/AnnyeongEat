package tech.erubin.annyeong_eat.telegramBot.module.mainMenu;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.messages.TextMessages;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.ReplyButtonServiceImpl;

@Component
@AllArgsConstructor
public class MainMenuModule {
    ClientServiceImpl clientService;
    ReplyButtonServiceImpl replyButton;
    TextMessages textMessages;

    public BotApiMethod<?> startClient(Update update, Client client) {
        SendMessage sendMessage = new SendMessage();
        Message message = update.getMessage();
        String sourceText = message.getText();
        String chatId = message.getChatId().toString();
        String sate = client.getState();
        String text = "Ошибка MainMenuModule.startClient";

        switch (sate) {
            case "главное меню":
                switch (sourceText) {
                    case "Сделать заказ":
                        text = "Выберите кафе";
                        client.setStatus("оформление заказа");
                        client.setState("выбор кафе");
                        sendMessage.enableMarkdown(true);
                        sendMessage.setReplyMarkup(replyButton.clientOrderCafe(client));
                        break;
                    case "Статус заказа":
                        text = "Просмотр статуса заказа";
                        client.setState("статус заказа");
                        sendMessage.enableMarkdown(true);
                        sendMessage.setReplyMarkup(replyButton.clientCheckOrder());
                        break;
                    case "Помощь":
                        text = "помогаем";
                        client.setState("помощь");
                        sendMessage.enableMarkdown(true);
                        sendMessage.setReplyMarkup(replyButton.clientHelp());
                        break;
                    case "Профиль":
                        text = "просмотр профиля";
                        client.setState("посмотреть профиль");
                        sendMessage.enableMarkdown(true);
                        sendMessage.setReplyMarkup(replyButton.clientProfileInfo(client));
                        break;
                    default:
                        text = "Воспользуйтесь кнопками";
                        sendMessage.enableMarkdown(true);
                        sendMessage.setReplyMarkup(replyButton.clientMainMenu());
                        break;
                }
                break;
            case "статус заказа":
                switch (sourceText) {
                    case "назад":
                        text = "главное меню";
                        client.setState("главное меню");
                        sendMessage.enableMarkdown(true);
                        sendMessage.setReplyMarkup(replyButton.clientMainMenu());
                        break;
                }
                break;
            case "помощь":
                switch (sourceText) {
                    case "назад":
                        text = "главное меню";
                        client.setState("главное меню");
                        sendMessage.enableMarkdown(true);
                        sendMessage.setReplyMarkup(replyButton.clientMainMenu());
                        break;
                }
                break;
            case "посмотреть профиль":
                switch (sourceText) {
                    case "назад":
                        text = "главное меню";
                        client.setStatus("главное меню");
                        client.setState("главное меню");
                        sendMessage.enableMarkdown(true);
                        sendMessage.setReplyMarkup(replyButton.clientMainMenu());
                        break;
                }
                break;
        }
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        clientService.saveClient(client);
        return sendMessage;
    }
}
