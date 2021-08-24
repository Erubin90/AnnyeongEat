package tech.erubin.annyeong_eat.telegramBot.module.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.telegramBot.states.UserStateEnum;
import tech.erubin.annyeong_eat.telegramBot.module.mainMenu.MainMenuModule;
import tech.erubin.annyeong_eat.telegramBot.module.order.OrderModule;
import tech.erubin.annyeong_eat.telegramBot.module.registration.RegistrationModule;
import tech.erubin.annyeong_eat.service.CafeServiceImpl;
import tech.erubin.annyeong_eat.service.UserServiceImpl;
import tech.erubin.annyeong_eat.service.UserStatesServiceImpl;

import java.util.List;

@Component
@AllArgsConstructor
public class MessageHandler {
    private final UserServiceImpl clientService;
    private final RegistrationModule registrationModule;
    private final MainMenuModule mainMenuModule;
    private final OrderModule orderModule;
    private final UserStatesServiceImpl stateService;
    private final CafeServiceImpl cafeService;
    private final HandlersTextMessage textMessage;

    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> botApiMethod = null;

        if (update.getMessage() != null && update.getMessage().hasText()) {
            String userId = update.getMessage().getFrom().getId().toString();
            String userName = update.getMessage().getFrom().getUserName();
            User user = clientService.getClient(userId, userName);
            botApiMethod = clientActions(update, user);
        }
        return botApiMethod;
    }

    private BotApiMethod<?> clientActions(Update update, User user) {
        UserState userState = stateService.getState(user);
        UserStateEnum userStateEnum = getClientState(userState);

        if (userStateEnum == UserStateEnum.REGISTRATION_START ||
                userStateEnum == UserStateEnum.REGISTRATION_CITY ||
                userStateEnum == UserStateEnum.REGISTRATION_NAME ||
                userStateEnum == UserStateEnum.REGISTRATION_SURNAME ||
                userStateEnum == UserStateEnum.REGISTRATION_PHONE_NUMBERS) {
            return registrationModule.startClient(update, user, userStateEnum);
        }
        else if (userStateEnum == UserStateEnum.MAIN_MENU ||
                userStateEnum == UserStateEnum.ORDER_CHECK ||
                userStateEnum == UserStateEnum.HELP ||
                userStateEnum == UserStateEnum.PROFILE) {
            return mainMenuModule.startClient(update, user, userStateEnum);
        }
        else if (userStateEnum == UserStateEnum.ORDER_CAFE ||
                userStateEnum == UserStateEnum.ORDER_CAFE_MENU ||
                userStateEnum == UserStateEnum.DELIVERY_ADDRESS ||
                userStateEnum == UserStateEnum.DELIVERY_PHONE_NUMBER ||
                userStateEnum == UserStateEnum.DELIVERY_PAYMENT_METHOD ||
                userStateEnum == UserStateEnum.DELIVERY_CONFIRMATION) {
            return orderModule.startClient(update, user, userStateEnum);
        }
        else {
            return new SendMessage(update.getMessage().getChatId().toString(), textMessage.getError());
        }
    }

    private UserStateEnum getClientState(UserState userState) {
        List<String> cafeNameList = cafeService.getAllCafeNames();
        if (cafeNameList.contains(userState.getState())) {
            return UserStateEnum.ORDER_CAFE_MENU;
        }
        else {
            return UserStateEnum.GET.clientState(userState.getState());
        }
    }
}
