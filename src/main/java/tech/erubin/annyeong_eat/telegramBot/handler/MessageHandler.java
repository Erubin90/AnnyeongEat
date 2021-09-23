package tech.erubin.annyeong_eat.telegramBot.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
import tech.erubin.annyeong_eat.telegramBot.module.EmployeeModule;
import tech.erubin.annyeong_eat.telegramBot.module.MainMenuModule;
import tech.erubin.annyeong_eat.telegramBot.module.OrderModule;
import tech.erubin.annyeong_eat.telegramBot.module.RegistrationModule;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Handlers;

import java.util.List;

@Component
@AllArgsConstructor
public class MessageHandler extends Handlers {
    private final RegistrationModule registrationModule;
    private final MainMenuModule mainMenuModule;
    private final OrderModule orderModule;
    private final EmployeeModule employeeModule;
    private final UserServiceImpl clientService;
    private final UserStatesServiceImpl stateService;
    private final CafeServiceImpl cafeService;

    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> botApiMethod;
        User user = getUser(update);
        UserState userState = stateService.getState(user);
        EmployeeEnum employeeEnum = EmployeeEnum.GET.department(userState.getState());
        String sourceText = update.getMessage().getText();
        if  (employeeEnum != null) {
            botApiMethod = employeeActions(update, user, employeeEnum, userState, sourceText);
        }
        else {
            ClientEnum clientEnum = getUserEnum(userState);
            botApiMethod = clientActions(update, user, clientEnum, sourceText);
        }
        return botApiMethod;
    }

    private BotApiMethod<?> clientActions(Update update, User user, ClientEnum clientEnum, String sourceText) {
        BotApiMethod<?> botApiMethod;
        switch (clientEnum) {
            case REGISTRATION_START:
                botApiMethod = registrationModule.start(update, user);
                break;
            case REGISTRATION_CITY:
                botApiMethod = registrationModule.city(update, user, sourceText);
                break;
            case REGISTRATION_NAME:
                botApiMethod = registrationModule.name(update, user, sourceText);
                break;
            case REGISTRATION_SURNAME:
                botApiMethod = registrationModule.surname(update, user, sourceText);
                break;
            case REGISTRATION_PHONE_NUMBERS:
                botApiMethod = registrationModule.phoneNumber(update, user, sourceText);
                break;
            case MAIN_MENU:
                botApiMethod = mainMenuModule.mainMenu(update, user, sourceText);
                break;
            case ORDER_CHECK:
                botApiMethod = mainMenuModule.orderChek(update, user, sourceText);
                break;
            case HELP:
                botApiMethod = mainMenuModule.help(update, user, sourceText);
                break;
            case PROFILE:
                botApiMethod = mainMenuModule.profile(update, user, sourceText);
                break;
            case ORDER_CAFE:
                botApiMethod = orderModule.choosingCafe(update, user, sourceText);
                break;
            case ORDER_CAFE_MENU:
                botApiMethod = orderModule.cafeMenu(update, user, sourceText);
                break;
            case DELIVERY_ADDRESS:
                botApiMethod = orderModule.deliveryAddress(update, user, sourceText);
                break;
            case DELIVERY_PHONE_NUMBER:
                botApiMethod = orderModule.deliveryPhoneNumber(update,user, sourceText);
                break;
            case DELIVERY_PAYMENT_METHOD:
                botApiMethod = orderModule.deliveryPaymentMethod(update, user, sourceText);
                break;
            case DELIVERY_CONFIRMATION:
                botApiMethod = orderModule.deliveryConfirmation(update, user, sourceText);
                break;
            default:
                throw new NullPointerException("Такого состояния у клиента несуществует");
        }
        return botApiMethod;
    }

    private BotApiMethod<?> employeeActions(Update update, User user, EmployeeEnum employeeEnum, UserState userState, String sourceText) {
        BotApiMethod<?> botApiMethod;
        switch (employeeEnum) {
            case OPERATOR:
                botApiMethod = employeeModule.operator(update, user, sourceText);
                break;
            case ADMINISTRATOR:
                botApiMethod = employeeModule.administrator(update, user, sourceText);
                break;
            case COURIER:
                botApiMethod = employeeModule.courier(update, user, sourceText);
                break;
            case DEVELOPER:
                botApiMethod = employeeModule.developer(update, user, sourceText);
                break;
            default:
                throw new NullPointerException("Такого состояния у работника несуществует");
        }
        return botApiMethod;
    }

    private User getUser(Update update) {
        String userId = update.getMessage().getFrom().getId().toString();
        return  clientService.getUser(userId);
    }

    private ClientEnum getUserEnum(UserState userState) {
        List<String> cafeNameList = cafeService.getAllCafeNames();
        if (cafeNameList.contains(userState.getState())) {
            return ClientEnum.ORDER_CAFE_MENU;
        }
        else {
            return ClientEnum.GET.userState(userState.getState());
        }
    }
}
