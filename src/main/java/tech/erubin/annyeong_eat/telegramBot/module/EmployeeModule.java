package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.service.OrderServiceImpl;
import tech.erubin.annyeong_eat.service.OrderStatesServiceImpl;
import tech.erubin.annyeong_eat.service.UserServiceImpl;
import tech.erubin.annyeong_eat.service.UserStatesServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Module;

@Component
public class EmployeeModule extends Module {
    private final ReplyButtons replyButtons;

    public EmployeeModule(OrderServiceImpl orderService, UserServiceImpl userService,
                          UserStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                          ReplyButtons replyButtons) {
        super(orderService, userService, userStatesService, orderStatesService);
        this.replyButtons = replyButtons;
    }

    public SendMessage operator(Update update, User user, String soursText) {
//        ReplyKeyboard replyKeyboard;
//        UserState userState;
//        String text;
//        if (soursText.equals(replyButtons.getRestart())) {
//
//        }
//        else {
//            text = putButton;
//            replyKeyboard = replyButtons.operatorMainMenu();
//        }
        return null;
    }

    public SendMessage administrator(Update update, User user, String soursText) {
//        ReplyKeyboard replyKeyboard;
//        UserState userState;
//        String text;
        return null;
    }

    public SendMessage courier(Update update, User user, String soursText) {
        ReplyKeyboard replyKeyboard;
        UserState userState;
        String text;
        return null;
    }

    public SendMessage developer(Update update, User user, String soursText) {
//        ReplyKeyboard replyKeyboard;
//        UserState userState;
//        String text;
//        return sendMessage(update, replyKeyboard, text, userState);
        return null;
    }
}
