package tech.erubin.annyeong_eat.telegramBot.service.botService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.service.entityService.ClientServiceImpl;

import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@NoArgsConstructor
public class RegistrationService {
    @Autowired
    ClientServiceImpl clientService;

    String regularError;

    Map<String, String> messageError;

    Map<String, String> messageNoError;

    List<String> flags;

    public RegistrationService(ClientServiceImpl clientService, String regularError, Map<String, String> messageError,
                               Map<String, String> messageNoError, List<String> flags) {
        this.clientService = clientService;
        this.regularError = regularError;
        this.messageError = messageError;
        this.messageNoError = messageNoError;
        this.flags = flags;
    }

    public SendMessage startClient(Update update, Client client){
        SendMessage sendMessage = new SendMessage();
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();

        String state = client.getState();

        String text = "";

        switch (state) {
            case "не зарегестрирован":
                client.setState("регистрация имени");
                text = "${introductoryMessage}";
                break;
            case "регистрация имени":
                System.out.println("начало регистрации имени");
                text = handlerUserWords(message, flags.get(0));
                if (!text.matches(regularError)) {
                    client.setState("регистрация фамилии");
                }
                break;
            case "регистрация фамилии":
                text = handlerUserWords(message, flags.get(1));
                if (!text.matches(regularError)) {
                    client.setState("регистрация номера");
                }
                break;
            case "регистрация номера":
                text = handlerPhoneNumber(message, flags.get(2));
                if (!text.matches(regularError)) {
                    client.setState("регистрация города");
                }
                break;
            case "регистрация города":
                text = handlerUserWords(message, flags.get(3));
                if (!text.matches(regularError)) {
                    client.setState("главное меню");
                }
                break;
        }
        sendMessage.setText(chatId);
        sendMessage.setText(text);
        clientService.saveClient(client);
        return sendMessage;
    }

    private String handlerUserWords(Message message, String flag){
        String sourceText = message.getText();
        StringBuilder resultText = new StringBuilder(regularError + "\n");
        if (sourceText.isBlank()){
            resultText.append(messageError.get("blank"))
                    .append(" ")
                    .append(flag)
                    .append("\n");
        }
        else if (sourceText.matches("\\d+")){
            resultText.append(messageError.get("number"))
                    .append(" ")
                    .append(flag)
                    .append("\n");
        }
        else if (sourceText.matches("^[а-яА-Я]")){
            resultText.append(messageError.get("english"))
                    .append(" ")
                    .append(flag)
                    .append("\n");
        }
        else if (!sourceText.matches("(\\w*)-(\\w*)")){
            resultText.append(messageError.get("noCorrectChar"))
                    .append(" ")
                    .append(flag)
                    .append("\n");
        }
        else {
            return messageNoError.get(flag);
        }
        return resultText.toString();
    }

    private String handlerPhoneNumber(Message message, String flag) {
        String sourcePhoneNumber = message.getText();
        StringBuilder resultText = new StringBuilder(regularError + "\n");
        if(sourcePhoneNumber.isBlank()){
            resultText.append(messageError.get("blank"))
                    .append(" ")
                    .append(flag)
                    .append("\n");
        }
        else if (sourcePhoneNumber.matches("\\D")) {
            resultText.append(messageError.get("noCorrectChar"))
                    .append(" ")
                    .append(flag)
                    .append("\n");
        }
        return resultText.toString();
    }

}
