package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.entity.OrderState;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Module;

import java.util.List;

@Component
public class MainMenuModule extends Module {
    private final ReplyButtons replyButtons;

    public MainMenuModule(OrderServiceImpl orderService, UserServiceImpl userService,
                          UserStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                          DepartmentServiceImpl departmentService, ReplyButtons replyButtons) {
        super(orderService, userService, userStatesService, orderStatesService, departmentService);
        this.replyButtons = replyButtons;
    }

    public SendMessage mainMenu(Update update, User user, String sourceText) {
        String text;
        UserState userState = null;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getCreateOrder())) {
            text = choosingCafe;
            userState = userStatesService.create(user, ClientEnum.ORDER_CAFE.getValue());
            replyKeyboard = replyButtons.userOrderCafe(user);
        }
        else if (sourceText.equals(replyButtons.getCheckOrder())) {
            text = "Просмотр статуса заказа";
            userState = userStatesService.create(user, ClientEnum.ORDER_CHECK.getValue());
            replyKeyboard = replyButtons.userCheckOrder();
        }
        else if (sourceText.equals(replyButtons.getHelp())) {
            text = help;
            userState = userStatesService.create(user, ClientEnum.HELP.getValue());
            replyKeyboard = replyButtons.userHelp();
        }
        else if (sourceText.equals(replyButtons.getClientInfo())) {
            text = clientProfile(user);
            userState = userStatesService.create(user, ClientEnum.PROFILE.getValue());
            replyKeyboard = replyButtons.userProfileInfo();
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userMainMenu();
        }
        return sendMessage(update, replyKeyboard, text, userState);
    }

    public SendMessage orderChek(Update update, User user, String sourceText) {
        ReplyKeyboard replyKeyboard = replyButtons.userCheckOrder();
        UserState userState = null;
        String text = putButton;
        if (sourceText.equals(replyButtons.getBack())){
            text = returnMainMenu;
            userState = userStatesService.create(user, ClientEnum.MAIN_MENU.getValue());
            replyKeyboard = replyButtons.userMainMenu();
        }
        return sendMessage(update, replyKeyboard, text, userState);
    }

    public SendMessage help(Update update, User user, String sourceText) {
        ReplyKeyboard replyKeyboard = replyButtons.userHelp();
        UserState userState = null;
        String text = putButton;
        if (sourceText.equals(replyButtons.getBack())) {
            text = returnMainMenu;
            userState = userStatesService.create(user, ClientEnum.MAIN_MENU.getValue());
            replyKeyboard = replyButtons.userMainMenu();
        }
        return sendMessage(update, replyKeyboard, text, userState);
    }

    public SendMessage profile(Update update, User user, String sourceText) {
        ReplyKeyboard replyKeyboard = replyButtons.userProfileInfo();
        UserState userState = null;
        String text = putButton;
        if (sourceText.equals(replyButtons.getBack())) {
            text = returnMainMenu;
            userState = userStatesService.create(user, ClientEnum.MAIN_MENU.getValue());
            replyKeyboard = replyButtons.userMainMenu();
        }
        return sendMessage(update, replyKeyboard, text, userState);
    }

    private String clientProfile(User user) {
        int countOrder = 0;
        for (Order order : user.getOrderList()) {
            List<OrderState> orderStateList = order.getOrderStateList();
            if (orderStateList.size() > 1) {
                String state = orderStateList.get(orderStateList.size() - 1).getState();
                if (OrderEnum.GET.isOrderAccepted(state)) {
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
}
