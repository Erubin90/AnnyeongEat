package tech.erubin.annyeong_eat.telegramBot.module.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.telegramBot.module.buttons.ButtonNames;
import tech.erubin.annyeong_eat.telegramBot.module.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.module.handler.CheckMessage;
import tech.erubin.annyeong_eat.service.UserServiceImpl;
import tech.erubin.annyeong_eat.service.UserStatesServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.states.UserStateEnum;

@Component
@AllArgsConstructor
public class RegistrationModule {
    private final RegistrationTextMessage textMessage;

    private final ButtonNames buttonNames;
    private final CheckMessage checkMessage;
    private final ReplyButtons replyButtons;

    private final UserServiceImpl clientService;
    private final UserStatesServiceImpl clientStateService;

    public BotApiMethod<?> startClient(Update update, User user, UserStateEnum userStateEnum) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = textMessage.getError();
        UserState userState = null;
        switch (userStateEnum) {
            case REGISTRATION_START:
                text = textMessage.getStartClientRegistration();
                userState = clientStateService.create(user, UserStateEnum.REGISTRATION_CITY.getValue());
                sendMessage.setReplyMarkup(replyButtons.clientRegistrationCity());
                return returnSendMessage(sendMessage, user, userState, text);
            case REGISTRATION_CITY:
                text = textMessage.getErrorNameCity();
                if (buttonNames.getAllCitySetList().contains(sourceText)) {
                    text = textMessage.getCityNoError();
                    user.setCity(sourceText);
                    userState = clientStateService.create(user, UserStateEnum.REGISTRATION_NAME.getValue());
                }
                else {
                    sendMessage.setReplyMarkup(replyButtons.clientRegistrationCity());
                }
                return returnSendMessage(sendMessage, user, userState, text);
            case REGISTRATION_NAME:
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                text = checkMessage.checkName(sourceText);
                if (!text.contains(textMessage.getErrorTrigger())) {
                    text = textMessage.getNameNoError();
                    user.setName(sourceText);
                    userState = clientStateService.create(user, UserStateEnum.REGISTRATION_SURNAME.getValue());
                }
                return returnSendMessage(sendMessage, user, userState, text);
            case REGISTRATION_SURNAME:
                text = checkMessage.checkSurname(sourceText);
                if (!text.contains(textMessage.getErrorTrigger())) {
                    user.setSurname(sourceText);
                    userState = clientStateService.create(user, UserStateEnum.REGISTRATION_PHONE_NUMBERS.getValue());
                }
                return returnSendMessage(sendMessage, user, userState, text);
            case REGISTRATION_PHONE_NUMBERS:
                text = checkMessage.checkPhoneNumber(sourceText);
                if (!text.contains(textMessage.getErrorTrigger())) {
                    if (sourceText.length() == 12) {
                        sourceText = "8" + sourceText.substring(2, 12);
                    }
                    text = textMessage.getPhoneNumberNoError() + "\n" +
                            textMessage.getEndClientRegistration();
                    user.setPhoneNumber(sourceText);
                    userState = clientStateService.create(user, UserStateEnum.MAIN_MENU.getValue());
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                return returnSendMessage(sendMessage, user, userState, text);
        }
        return returnSendMessage(sendMessage, text);
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, User user, UserState userState, String text) {
        sendMessage.setText(text);
        clientStateService.save(userState);
        clientService.save(user);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }

}
