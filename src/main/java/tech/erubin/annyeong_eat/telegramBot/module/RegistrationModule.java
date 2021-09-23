package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.UserEnum;
import tech.erubin.annyeong_eat.telegramBot.handler.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Module;

import java.util.Set;

@Component
public class RegistrationModule extends Module {
    private final CafeServiceImpl cafeService;
    private final ReplyButtons replyButtons;
    private final CheckMessage checkMessage;

    public RegistrationModule(OrderServiceImpl orderService, UserServiceImpl userService,
                              UserStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                              CafeServiceImpl cafeService, ReplyButtons replyButtons, CheckMessage checkMessage) {
        super(orderService, userService, userStatesService, orderStatesService);
        this.cafeService = cafeService;
        this.replyButtons = replyButtons;
        this.checkMessage = checkMessage;
    }

    public BotApiMethod<?> start(Update update, User user, UserEnum userEnum) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = error;
        UserState userState = null;
        Set<String> allCityName = cafeService.getAllCity();

        switch (userEnum) {
            case REGISTRATION_START:
                text = startClientRegistration + "\n" + errorNameCity;
                userState = userStatesService.create(user, UserEnum.REGISTRATION_CITY.getValue());
                sendMessage.setReplyMarkup(replyButtons.userRegistrationCity(allCityName));
                break;
            case REGISTRATION_CITY:
                text = errorNameCity;
                if (allCityName.contains(sourceText)) {
                    text = cityNoError;
                    user.setCity(sourceText);
                    userState = userStatesService.create(user, UserEnum.REGISTRATION_NAME.getValue());
                }
                else {
                    sendMessage.setReplyMarkup(replyButtons.userRegistrationCity(allCityName));
                }
                break;
            case REGISTRATION_NAME:
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                text = checkMessage.checkName(sourceText);
                if (!text.contains(errorTrigger)) {
                    text = nameNoError;
                    user.setName(sourceText);
                    userState = userStatesService.create(user, UserEnum.REGISTRATION_SURNAME.getValue());
                }
                break;
            case REGISTRATION_SURNAME:
                text = checkMessage.checkSurname(sourceText);
                if (!text.contains(errorTrigger)) {
                    user.setSurname(sourceText);
                    userState = userStatesService.create(user, UserEnum.REGISTRATION_PHONE_NUMBERS.getValue());
                }
                break;
            case REGISTRATION_PHONE_NUMBERS:
                text = checkMessage.checkPhoneNumber(sourceText);
                if (!text.contains(errorTrigger)) {
                    if (sourceText.length() == 12) {
                        sourceText = "8" + sourceText.substring(2, 12);
                    }
                    text = phoneNumberNoError + "\n" +
                            endUserRegistration;
                    user.setPhoneNumber(sourceText);
                    userState = userStatesService.create(user, UserEnum.MAIN_MENU.getValue());
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(replyButtons.userMainMenu(user));
                }
                break;
        }
        return sendMessage(sendMessage, user, userState, text);
    }
}
