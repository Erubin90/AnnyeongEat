package tech.erubin.annyeong_eat.telegramBot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.UserEnum;
import tech.erubin.annyeong_eat.telegramBot.module.EmployeeModule;
import tech.erubin.annyeong_eat.telegramBot.module.MainMenuModule;
import tech.erubin.annyeong_eat.telegramBot.module.OrderModule;
import tech.erubin.annyeong_eat.telegramBot.module.RegistrationModule;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Handlers;

import java.util.List;

@Component
public class MessageHandler extends Handlers {
    private final RegistrationModule registrationModule;
    private final MainMenuModule mainMenuModule;
    private final OrderModule orderModule;
    private final EmployeeModule employeeModule;

    public MessageHandler(UserServiceImpl clientService, OrderServiceImpl orderService,
                          OrderStatesServiceImpl orderStatesService, DishServiceImpl dishService,
                          ChequeDishServiceImpl chequeService, UserStatesServiceImpl stateService,
                          CafeServiceImpl cafeService,
                          OrderModule orderModule, RegistrationModule registrationModule,
                          MainMenuModule mainMenuModule, EmployeeModule employeeModule) {
        super(clientService, orderService, orderStatesService, dishService, chequeService,
                stateService, cafeService);
        this.registrationModule = registrationModule;
        this.mainMenuModule = mainMenuModule;
        this.orderModule = orderModule;
        this.employeeModule = employeeModule;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> botApiMethod;
        User user = getUser(update);
        UserEnum userEnum = getUserState(user);
        boolean isEmployee = EmployeeEnum.GET.isEmployee(userEnum.getValue());
        if  (isEmployee) {
            botApiMethod = employeeActions(update, user, userEnum);
        }
        else {
            botApiMethod = clientActions(update, user, userEnum);
        }
        return botApiMethod;
    }

    private BotApiMethod<?> clientActions(Update update, User user, UserEnum userEnum) {
        if (userEnum == UserEnum.REGISTRATION_START ||
                userEnum == UserEnum.REGISTRATION_CITY ||
                userEnum == UserEnum.REGISTRATION_NAME ||
                userEnum == UserEnum.REGISTRATION_SURNAME ||
                userEnum == UserEnum.REGISTRATION_PHONE_NUMBERS) {
            return registrationModule.start(update, user, userEnum);
        }
        else if (userEnum == UserEnum.MAIN_MENU ||
                userEnum == UserEnum.ORDER_CHECK ||
                userEnum == UserEnum.HELP ||
                userEnum == UserEnum.PROFILE) {
            return mainMenuModule.start(update, user, userEnum);
        }
        else if (userEnum == UserEnum.ORDER_CAFE ||
                userEnum == UserEnum.ORDER_CAFE_MENU ||
                userEnum == UserEnum.DELIVERY_ADDRESS ||
                userEnum == UserEnum.DELIVERY_PHONE_NUMBER ||
                userEnum == UserEnum.DELIVERY_PAYMENT_METHOD ||
                userEnum == UserEnum.DELIVERY_CONFIRMATION) {
            return orderModule.start(update, user, userEnum);
        }
        else {
            throw new NullPointerException();
        }
    }

    private BotApiMethod<?> employeeActions(Update update, User user, UserEnum userEnum) {
        if (userEnum == UserEnum.OPERATOR ||
                userEnum == UserEnum.ADMINISTRATOR ||
                userEnum == UserEnum.COURIER ||
                userEnum == UserEnum.DEVELOPER ||
                userEnum == UserEnum.CHOICE_DEPARTMENT) {
            return employeeModule.start(update, user, userEnum);
        }
        else {
            throw new NullPointerException();
        }
    }

    private User getUser(Update update) {
        String userId = update.getMessage().getFrom().getId().toString();
        return  clientService.getUser(userId);
    }

    private UserEnum getUserState(User user) {
        String userState = stateService.getState(user).getState();
        List<String> cafeNameList = cafeService.getAllCafeNames();
        if (cafeNameList.contains(userState)) {
            return UserEnum.ORDER_CAFE_MENU;
        }
        else {
            return UserEnum.GET.userState(userState);
        }
    }
}
