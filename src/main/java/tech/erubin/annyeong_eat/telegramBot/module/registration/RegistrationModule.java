package tech.erubin.annyeong_eat.telegramBot.module.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Employee;
import tech.erubin.annyeong_eat.telegramBot.module.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.EmployeeServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.ReplyButtonServiceImpl;

@Component
@AllArgsConstructor
public class RegistrationModule {
    private final RegistrationButtonName buttonName;
    private final RegistrationTextMessage textMessage;

    private final CheckMessage checkMessage;
    private final ReplyButtonServiceImpl replyButton;

    private final ClientServiceImpl clientService;
    private final EmployeeServiceImpl employeeService;

    public BotApiMethod<?> startClient(Update update, Client client) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        String sourceText = update.getMessage().getText();
        String state = client.getState();
        String text = "Ошибка";
        switch (state) {
            case "не зарегестрирован":
                text = textMessage.getStartClientRegistration();
                client.setState("регистрация города");
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(replyButton.clientRegistrationCity());
                return returnSendMessage(sendMessage, client, text);
            case "регистрация города":
                text = textMessage.getErrorNameCity();
                if (buttonName.getAllCitySetList().contains(sourceText)) {
                    text = textMessage.getCityNoError();
                    client.setCity(sourceText);
                    client.setState("регистрация имени");
                }
                else {
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(replyButton.clientRegistrationCity());
                }
                return returnSendMessage(sendMessage, client, text);
            case "регистрация имени":
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                text = checkMessage.checkName(sourceText);
                if (!text.contains(textMessage.getErrorTrigger())) {
                    text = textMessage.getNameNoError();
                    client.setName(sourceText);
                    client.setState("регистрация фамилии");
                }
                return returnSendMessage(sendMessage, client, text);
            case "регистрация фамилии":
                text = checkMessage.checkSurname(sourceText);
                if (!text.contains(textMessage.getErrorTrigger())) {
                    client.setSurname(sourceText);
                    client.setState("регистрация номера");
                }
                return returnSendMessage(sendMessage, client, text);
            case "регистрация номера":
                text = checkMessage.checkPhoneNumber(sourceText);
                if (!text.contains(textMessage.getErrorTrigger())) {
                    if (sourceText.length() == 12) {
                        sourceText = "8" + sourceText.substring(2, 11);
                    }
                    text = textMessage.getPhoneNumberNoError() + "\n" +
                            textMessage.getEndClientRegistration();
                    client.setPhoneNumber(sourceText);
                    client.setState("главное меню");
                    client.setStatus("главное меню");
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(replyButton.clientMainMenu());
                }
                return returnSendMessage(sendMessage, client, text);
        }
        return returnSendMessage(sendMessage, text);
    }

//    public BotApiMethod<?> startClientOrEmployee(Update update, Client client, Employee employee) {
//        String chatId = update.getMessage().getChatId().toString();
//        String sourceText = update.getMessage().getText();
//        String text = "Войти как работник или клиент";
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(chatId);
//        sendMessage.setReplyMarkup(replyButton.clientOrEmployeeRegistration());
//        if (sourceText.equals(buttonName.getOrClient())) {
//
//        }
//        else if (sourceText.equals(buttonName.getOrEmployee())) {
//            text = "Добро пожаловать " + employee.getName() + "!";
//            employee.setStatus("главное меню");
//            employee.setState("главное меню");
//            sendMessage.setReplyMarkup(replyButton.employeeMainMenu(employee));
//        }
//
//        return  returnSendMessage(sendMessage, client, employee, text);
//    }

    private SendMessage returnSendMessage (SendMessage sendMessage, Client client, String text) {
        sendMessage.setText(text);
        clientService.saveClient(client);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, Client client, Employee employee, String text) {
        sendMessage.setText(text);
        employeeService.saveEmployee(employee);
        clientService.saveClient(client);
        return sendMessage;
    }

}
