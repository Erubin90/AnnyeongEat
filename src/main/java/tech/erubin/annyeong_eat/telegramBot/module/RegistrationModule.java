package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
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
                              EmployeeServiceImpl employeeService, CafeServiceImpl cafeService,
                              ReplyButtons replyButtons, CheckMessage checkMessage,
                              @Lazy AnnyeongEatWebHook webHook) {
        super(orderService, userService, userStatesService, orderStatesService, employeeService, webHook);
        this.cafeService = cafeService;
        this.replyButtons = replyButtons;
        this.checkMessage = checkMessage;
    }

    public SendMessage start(Update update, User user) {
        Set<String> allCityName = cafeService.getAllCity();
        String text = startClientRegistration + "\n" + errorNameCity;
        UserState userState = userStatesService.create(user, ClientEnum.REGISTRATION_CITY.getValue());
        ReplyKeyboard replyKeyboard = replyButtons.userRegistrationCity(allCityName);
        return message(update,  replyKeyboard, text, userState);
    }

    public SendMessage city(Update update, User user, String sourceText) {
        Set<String> allCityName = cafeService.getAllCity();
        String text = errorNameCity;
        UserState userState = null;
        ReplyKeyboard replyKeyboard = new ReplyKeyboardRemove(true);
        if (allCityName.contains(sourceText)) {
            text = cityNoError;
            user.setCity(sourceText);
            userState = userStatesService.create(user, ClientEnum.REGISTRATION_NAME.getValue());
        }
        else {
            replyKeyboard = replyButtons.userRegistrationCity(allCityName);
        }
        return message(update, replyKeyboard, text, userState, user);
    }

    public SendMessage name(Update update, User user, String sourceText) {
        String text = checkMessage.checkName(sourceText);
        ReplyKeyboard replyKeyboard = new ReplyKeyboardRemove(true);
        UserState userState = null;
        if (!text.contains(errorTrigger)) {
            text = nameNoError;
            user.setName(sourceText);
            userState = userStatesService.create(user, ClientEnum.REGISTRATION_SURNAME.getValue());
        }
        return message(update, replyKeyboard, text, userState, user);
    }

    public SendMessage surname(Update update, User user, String sourceText) {
        String text = checkMessage.checkSurname(sourceText);
        ReplyKeyboard replyKeyboard = new ReplyKeyboardRemove(true);
        UserState userState = null;
        if (!text.contains(errorTrigger)) {
            text = surnameNoError;
            user.setSurname(sourceText);
            userState = userStatesService.create(user, ClientEnum.REGISTRATION_PHONE_NUMBERS.getValue());
        }
        return message(update, replyKeyboard, text, userState, user);
    }

    public SendMessage phoneNumber(Update update, User user, String sourceText) {
        String text = checkMessage.checkPhoneNumber(sourceText);
        UserState userState = null;
        ReplyKeyboard replyKeyboard = new ReplyKeyboardRemove(true);
        if (!text.contains(errorTrigger)) {
            if (sourceText.length() == 12) {
                sourceText = "8" + sourceText.substring(2, 12);
            }
            text = phoneNumberNoError + "\n" +
                    endUserRegistration;
            user.setPhoneNumber(sourceText);
            userState = userStatesService.create(user, ClientEnum.MAIN_MENU.getValue());
            replyKeyboard = replyButtons.userMainMenu();
        }
        return message(update, replyKeyboard, text, userState, user);
    }
}
