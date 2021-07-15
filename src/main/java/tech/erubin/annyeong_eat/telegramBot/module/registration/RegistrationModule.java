package tech.erubin.annyeong_eat.telegramBot.module.registration;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.messages.TextMessages;
import tech.erubin.annyeong_eat.telegramBot.module.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.ReplyButtonServiceImpl;

@Component
public class RegistrationModule {
    private ClientServiceImpl clientService;
    private CheckMessage checkMessage;
    private ReplyButtonServiceImpl replyButton;

    private String regularError;
    private String introductoryMessage;

    public RegistrationModule(ClientServiceImpl clientService, CheckMessage checkMessage,
                              ReplyButtonServiceImpl replyButton, TextMessages textMessages) {
        this.clientService = clientService;
        this.checkMessage = checkMessage;
        this.replyButton = replyButton;
        regularError = textMessages.getRegularError();
        introductoryMessage = textMessages.getStartClientRegistration();
    }

    public SendMessage startClient(Update update, Client client){
        SendMessage sendMessage = new SendMessage();
        Message message = update.getMessage();
        String sourceText = message.getText();
        String chatId = message.getChatId().toString();
        String state = client.getState();
        String text = "Ошибка";
        switch (state) {
            case "не зарегестрирован":
                client.setState("регистрация имени");
                text = introductoryMessage;
                break;
            case "регистрация имени":
                text = checkMessage.checkName(sourceText);
                if (!text.contains(regularError)) {
                    client.setName(sourceText);
                    client.setState("регистрация фамилии");
                }
                break;
            case "регистрация фамилии":
                text = checkMessage.checkSurname(sourceText);
                if (!text.contains(regularError)) {
                    client.setSurname(message.getText());
                    client.setState("регистрация номера");
                }
                break;
            case "регистрация номера":
                text = checkMessage.checkPhoneNumber(sourceText);
                if (!text.contains(regularError)) {
                    if (sourceText.length() == 12) {
                        sourceText = "8" + sourceText.substring(1, 11);
                    }
                    client.setPhoneNumber(sourceText);
                    client.setState("регистрация города");
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(replyButton.clientRegistrationCity());
                }
                break;
            case "регистрация города":
                text = checkMessage.checkCity(sourceText);
                if (!text.contains(regularError)) {
                    client.setCity(sourceText);
                    client.setStatus("главное меню");
                    client.setState("главное меню");
                    sendMessage.setReplyMarkup(replyButton.clientMainMenu());
                }
                if (sendMessage.getReplyMarkup() == null) {
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(replyButton.clientRegistrationCity());
                }
                break;
        }
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        clientService.saveClient(client);
        return sendMessage;
    }

}
