package tech.erubin.annyeong_eat.telegramBot.module.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.ClientState;
import tech.erubin.annyeong_eat.telegramBot.module.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.states.ClientStateEnum;
import tech.erubin.annyeong_eat.telegramBot.module.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.service.entityServiсes.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServiсes.ClientStatesServiceImpl;

@Component
@AllArgsConstructor
public class RegistrationModule {
    private final RegistrationButtonName buttonName;
    private final RegistrationTextMessage textMessage;

    private final CheckMessage checkMessage;
    private final ReplyButtons replyButtons;

    private final ClientServiceImpl clientService;
    private final ClientStatesServiceImpl stateService;

    public BotApiMethod<?> startClient(Update update, Client client, ClientStateEnum clientStateEnum, ClientState clientState) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = textMessage.getError();
        switch (clientStateEnum) {
            case REGISTRATION_START:
                text = textMessage.getStartClientRegistration();
                clientState.setState(ClientStateEnum.REGISTRATION_CITY.getValue());
                sendMessage.setReplyMarkup(replyButtons.clientRegistrationCity());
                return returnSendMessage(sendMessage, client, clientState, text);
            case REGISTRATION_CITY:
                text = textMessage.getErrorNameCity();
                if (buttonName.getAllCitySetList().contains(sourceText)) {
                    text = textMessage.getCityNoError();
                    client.setCity(sourceText);
                    clientState.setState(ClientStateEnum.REGISTRATION_NAME.getValue());
                }
                else {
                    sendMessage.setReplyMarkup(replyButtons.clientRegistrationCity());
                }
                return returnSendMessage(sendMessage, client, clientState, text);
            case REGISTRATION_NAME:
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                text = checkMessage.checkName(sourceText);
                if (!text.contains(textMessage.getErrorTrigger())) {
                    text = textMessage.getNameNoError();
                    client.setName(sourceText);
                    clientState.setState(ClientStateEnum.REGISTRATION_SURNAME.getValue());
                }
                return returnSendMessage(sendMessage, client, clientState, text);
            case REGISTRATION_SURNAME:
                text = checkMessage.checkSurname(sourceText);
                if (!text.contains(textMessage.getErrorTrigger())) {
                    client.setSurname(sourceText);
                    clientState.setState(ClientStateEnum.REGISTRATION_PHONE_NUMBERS.getValue());
                }
                return returnSendMessage(sendMessage, client, clientState, text);
            case REGISTRATION_PHONE_NUMBERS:
                text = checkMessage.checkPhoneNumber(sourceText);
                if (!text.contains(textMessage.getErrorTrigger())) {
                    if (sourceText.length() == 12) {
                        sourceText = "8" + sourceText.substring(2, 11);
                    }
                    text = textMessage.getPhoneNumberNoError() + "\n" +
                            textMessage.getEndClientRegistration();
                    client.setPhoneNumber(sourceText);
                    clientState.setState(ClientStateEnum.MAIN_MENU.getValue());
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                return returnSendMessage(sendMessage, client, clientState, text);
        }
        return returnSendMessage(sendMessage, text);
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, Client client, ClientState clientState, String text) {
        sendMessage.setText(text);
        stateService.save(clientState);
        clientService.saveClient(client);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }

}
