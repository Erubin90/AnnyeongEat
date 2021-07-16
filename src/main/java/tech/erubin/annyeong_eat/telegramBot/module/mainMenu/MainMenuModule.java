package tech.erubin.annyeong_eat.telegramBot.module.mainMenu;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.ReplyButtonServiceImpl;

@Component
@AllArgsConstructor
public class MainMenuModule {
    private ClientServiceImpl clientService;
    private MainMenuButtonNames buttonNames;
    private ReplyButtonServiceImpl replyButton;

    public BotApiMethod<?> startClient(Update update, Client client) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        String sourceText = update.getMessage().getText();
        String sate = client.getState();
        String text = "Ошибка MainMenuModule.startClient";

        switch (sate) {
            case "главное меню":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(replyButton.clientMainMenu());
                if (sourceText.equals(buttonNames.getClientOrder())) {
                    text = "Выберите кафе";
                    client.setStatus("оформление заказа");
                    client.setState("выбор кафе");
                    sendMessage.setReplyMarkup(replyButton.clientOrderCafe(client));
                }
                else if (sourceText.equals(buttonNames.getCheckOrder())) {
                    text = "Просмотр статуса заказа";
                    client.setState("статус заказа");
                    sendMessage.setReplyMarkup(replyButton.clientCheckOrder());
                }
                else if (sourceText.equals(buttonNames.getHelp())) {
                    text = "помогаем";
                    client.setState("помощь");
                    sendMessage.setReplyMarkup(replyButton.clientHelp());
                }
                else if (sourceText.equals(buttonNames.getClientInfo())) {
                    text = "просмотр профиля";
                    client.setState("посмотреть профиль");
                    sendMessage.setReplyMarkup(replyButton.clientProfileInfo(client));
                }
                else {
                    text = "Воспользуйтесь кнопками";
                }
                return returnSendMessage(sendMessage, client, text);
            case "статус заказа":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(replyButton.clientCheckOrder());
                if (sourceText.equals(buttonNames.getBack())){
                    text = "главное меню";
                    client.setState("главное меню");
                    sendMessage.setReplyMarkup(replyButton.clientMainMenu());
                }
                return returnSendMessage(sendMessage, client, text);
            case "помощь":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(replyButton.clientHelp());
                if (sourceText.equals(buttonNames.getBack())) {
                    text = "главное меню";
                    client.setState("главное меню");
                    sendMessage.setReplyMarkup(replyButton.clientMainMenu());
                }
                return returnSendMessage(sendMessage, client, text);
            case "посмотреть профиль":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(replyButton.clientProfileInfo(client));
                if (sourceText.equals(buttonNames.getBack())) {
                    text = "главное меню";
                    client.setStatus("главное меню");
                    client.setState("главное меню");
                    sendMessage.setReplyMarkup(replyButton.clientMainMenu());
                }
                return returnSendMessage(sendMessage, client, text);
        }
        return returnSendMessage(sendMessage, text);
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, Client client, String text) {
        sendMessage.setText(text);
        clientService.saveClient(client);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }
}
