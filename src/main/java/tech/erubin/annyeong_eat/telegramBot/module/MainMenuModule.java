package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.entity.OrderState;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractModule;
import tech.erubin.annyeong_eat.telegramBot.buttons.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientStates;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderStates;

import java.util.List;

@Component
public class MainMenuModule extends AbstractModule {
    private final CafeServiceImpl cafeService;
    private final ReplyButtons replyButtons;
    private final InlineButtons inlineButtons;

    public MainMenuModule(OrderServiceImpl orderService, UserServiceImpl userService,
                          ClientStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                          EmployeeServiceImpl employeeService, ReplyButtons replyButtons, InlineButtons inlineButtons,
                          @Lazy AnnyeongEatWebHook webHook, CafeServiceImpl cafeService) {
        super(orderService, userService, userStatesService, orderStatesService, employeeService, webHook);
        this.replyButtons = replyButtons;
        this.inlineButtons = inlineButtons;
        this.cafeService = cafeService;
    }

    public BotApiMethod<?> mainMenu(Update update, User user, String sourceText) {
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getCreateOrder())) {
            text = choosingCafe;
            List<String> buttonName = cafeService.getCafeNameByCity(user.getCity());
            replyKeyboard = replyButtons.userOrderCafe(buttonName);
            userStatesService.createAndSave(user, ClientStates.ORDER_CAFE.getState());
        }
        else if (sourceText.equals(replyButtons.getCheckOrder())) {
            text = checkOrder;
            replyKeyboard = replyButtons.userCheckOrder();
            webHook.sendMessage(message(update, replyKeyboard, text));
            text = listOrder;
            replyKeyboard = inlineButtons.checkOrderMainMenu(user);
            userStatesService.createAndSave(user, ClientStates.ORDER_CHECK.getState());
        }
        else if (sourceText.equals(replyButtons.getHelp())) {
            text = help;
            replyKeyboard = replyButtons.userHelp();
            userStatesService.createAndSave(user, ClientStates.HELP.getState());
        }
        else if (sourceText.equals(replyButtons.getClientInfo())) {
            text = clientProfile(user);
            replyKeyboard = replyButtons.userProfileInfo();
            userStatesService.createAndSave(user, ClientStates.PROFILE.getState());
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userMainMenu();
        }
        return message(update, replyKeyboard, text);
    }

    public BotApiMethod<?> orderCheck(Update update, User user, String sourceText) {
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getBack())){
            text = returnMainMenu;
            replyKeyboard = replyButtons.userMainMenu();
            userStatesService.createAndSave(user, ClientStates.MAIN_MENU.getState());
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userCheckOrder();
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage help(Update update, User user, String sourceText) {
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getBack())) {
            text = returnMainMenu;
            replyKeyboard = replyButtons.userMainMenu();
            userStatesService.createAndSave(user, ClientStates.MAIN_MENU.getState());
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userHelp();
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage profile(Update update, User user,  String sourceText) {
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getBack())) {
            text = returnMainMenu;
            replyKeyboard = replyButtons.userMainMenu();
            userStatesService.createAndSave(user, ClientStates.MAIN_MENU.getState());
        }
        else {
            replyKeyboard = replyButtons.userProfileInfo();
            text = putButton;
        }
        return message(update, replyKeyboard, text);
    }

    private String clientProfile(User user) {
        int countOrder = 0;
        for (Order order : user.getOrderList()) {
            List<OrderState> orderStateList = order.getOrderStateList();
            if (orderStateList.size() > 1) {
                OrderState orderState = orderStateList.get(orderStateList.size() - 1);
                if (OrderStates.isOrderAccepted(orderState)) {
                    countOrder++;
                }
            }
        }
        return "Имя - " + user.getName() + "\n" +
                "Фамилия - " + user.getSurname() + "\n" +
                "Номер - " + user.getPhoneNumber() + "\n" +
                "Город - " + user.getCity() + "\n" +
                "Количество заказов - " + countOrder;
    }

    public BotApiMethod<?> callbackOrderCheck(CallbackQuery callback, User user, String tag) {
        BotApiMethod<?> botApiMethod;
        if (tag.equals(replyButtons.getRestart())) {
            InlineKeyboardMarkup inlineKeyboard = inlineButtons.checkOrderMainMenu(user);
            botApiMethod = editMessageReplyMarkup(callback, inlineKeyboard);
        }
        else {
            botApiMethod = answerCallbackQuery(callback, putButton);
        }
        return botApiMethod;
    }
}