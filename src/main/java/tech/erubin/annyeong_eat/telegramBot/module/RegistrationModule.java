package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractModule;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
import tech.erubin.annyeong_eat.telegramBot.handler.CheckMessage;

import java.util.Set;

@Component
public class RegistrationModule extends AbstractModule {
    private final CafeServiceImpl cafeService;
    private final ReplyButtons replyButtons;
    private final CheckMessage checkMessage;

    public RegistrationModule(OrderServiceImpl orderService, UserServiceImpl userService,
                              ClientStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
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
        ReplyKeyboard replyKeyboard = replyButtons.userRegistrationCity(allCityName);
        userStatesService.createAndSave(user, ClientEnum.REGISTRATION_CITY.getValue());
        return message(update,  replyKeyboard, text);
    }

    public SendMessage city(Update update, User user, String sourceText) {
        String text;
        ReplyKeyboard replyKeyboard;
        Set<String> allCityName = cafeService.getAllCity();
        if (allCityName.contains(sourceText)) {
            text = cityNoError;
            user.setCity(sourceText);
            replyKeyboard = new ReplyKeyboardRemove(true);
            userService.save(user);
            userStatesService.createAndSave(user, ClientEnum.REGISTRATION_NAME.getValue());
        }
        else {
            text = errorNameCity;
            replyKeyboard = replyButtons.userRegistrationCity(allCityName);
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage name(Update update, User user, String sourceText) {
        String text = checkMessage.checkName(sourceText);
        ReplyKeyboard replyKeyboard = new ReplyKeyboardRemove(true);
        if (!text.contains(errorTrigger)) {
            text = nameNoError;
            user.setName(sourceText);
            userService.save(user);
            userStatesService.createAndSave(user, ClientEnum.REGISTRATION_SURNAME.getValue());
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage surname(Update update, User user, String sourceText) {
        String text = checkMessage.checkSurname(sourceText);
        ReplyKeyboard replyKeyboard = new ReplyKeyboardRemove(true);
        if (!text.contains(errorTrigger)) {
            text = surnameNoError;
            user.setSurname(sourceText);
            userService.save(user);
            userStatesService.createAndSave(user, ClientEnum.REGISTRATION_PHONE_NUMBERS.getValue());
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage phoneNumber(Update update, User user, String sourceText) {
        String text = checkMessage.checkPhoneNumber(sourceText);
        ReplyKeyboard replyKeyboard = new ReplyKeyboardRemove(true);
        if (!text.contains(errorTrigger)) {
            if (sourceText.length() == 12) {
                sourceText = "8" + sourceText.substring(2, 12);
            }
            text = phoneNumberNoError + "\n" +
                    endUserRegistration;
            user.setPhoneNumber(sourceText);
            replyKeyboard = replyButtons.userMainMenu();
            userService.save(user);
            userStatesService.create(user, ClientEnum.MAIN_MENU.getValue());
        }
        return message(update, replyKeyboard, text);
    }
}
