package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.entity.OrderState;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractModule;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;

import java.util.List;

@Component
public class MainMenuModule extends AbstractModule {
    private final CafeServiceImpl cafeService;
    private final ReplyButtons replyButtons;

    public MainMenuModule(OrderServiceImpl orderService, UserServiceImpl userService,
                          ClientStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                          EmployeeServiceImpl employeeService, ReplyButtons replyButtons,
                          @Lazy AnnyeongEatWebHook webHook, CafeServiceImpl cafeService) {
        super(orderService, userService, userStatesService, orderStatesService, employeeService, webHook);
        this.replyButtons = replyButtons;
        this.cafeService = cafeService;
    }

    public SendMessage mainMenu(Update update, User user, String sourceText) {
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getCreateOrder())) {
            text = choosingCafe;
            List<String> buttonName = cafeService.getCafeNameByCity(user.getCity());
            replyKeyboard = replyButtons.userOrderCafe(buttonName);
            userStatesService.createAndSave(user, ClientEnum.ORDER_CAFE.getValue());
        }
        else if (sourceText.equals(replyButtons.getCheckOrder())) {
            text = "Просмотр статуса заказа";
            replyKeyboard = replyButtons.userCheckOrder();
            userStatesService.createAndSave(user, ClientEnum.ORDER_CHECK.getValue());
        }
        else if (sourceText.equals(replyButtons.getHelp())) {
            text = help;
            replyKeyboard = replyButtons.userHelp();
            userStatesService.createAndSave(user, ClientEnum.HELP.getValue());
        }
        else if (sourceText.equals(replyButtons.getClientInfo())) {
            text = clientProfile(user);
            replyKeyboard = replyButtons.userProfileInfo();
            userStatesService.createAndSave(user, ClientEnum.PROFILE.getValue());
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userMainMenu();
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage orderChek(Update update, User user, String sourceText) {
        ReplyKeyboard replyKeyboard;
        String text;
        if (sourceText.equals(replyButtons.getBack())){
            text = returnMainMenu;
            replyKeyboard = replyButtons.userMainMenu();
            userStatesService.createAndSave(user, ClientEnum.MAIN_MENU.getValue());
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userCheckOrder();
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage help(Update update, User user, String sourceText) {
        ReplyKeyboard replyKeyboard = replyButtons.userHelp();
        String text = putButton;
        if (sourceText.equals(replyButtons.getBack())) {
            text = returnMainMenu;
            replyKeyboard = replyButtons.userMainMenu();
            userStatesService.createAndSave(user, ClientEnum.MAIN_MENU.getValue());
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage profile(Update update, User user,  String sourceText) {
        ReplyKeyboard replyKeyboard = replyButtons.userProfileInfo();
        String text = putButton;
        if (sourceText.equals(replyButtons.getBack())) {
            text = returnMainMenu;
            userStatesService.createAndSave(user, ClientEnum.MAIN_MENU.getValue());
        }
        return message(update, replyKeyboard, text);
    }

    private String clientProfile(User user) {
        int countOrder = 0;
        for (Order order : user.getOrderList()) {
            List<OrderState> orderStateList = order.getOrderStateList();
            if (orderStateList.size() > 1) {
                OrderState orderState = orderStateList.get(orderStateList.size() - 1);
                if (OrderEnum.GET.isOrderAccepted(orderState)) {
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
