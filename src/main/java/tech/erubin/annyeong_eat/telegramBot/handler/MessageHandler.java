package tech.erubin.annyeong_eat.telegramBot.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.service.UserServiceImpl;
import tech.erubin.annyeong_eat.service.UserStatesServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.DepartmentEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.module.MainMenuModule;
import tech.erubin.annyeong_eat.telegramBot.module.OperatorModule;
import tech.erubin.annyeong_eat.telegramBot.module.OrderModule;
import tech.erubin.annyeong_eat.telegramBot.module.RegistrationModule;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractHandler;

@Component
@AllArgsConstructor
public class MessageHandler extends AbstractHandler {
    private final RegistrationModule registrationModule;
    private final MainMenuModule mainMenuModule;
    private final OrderModule orderModule;
    private final OperatorModule operatorModule;
    private final UserServiceImpl clientService;
    private final UserStatesServiceImpl stateService;

    public BotApiMethod<?> handleUpdate(Update update) {
        String sourceText = update.getMessage().getText();
        User user = getUser(update);
        UserState userState = stateService.getState(user);
        DepartmentEnum department = DepartmentEnum.GET.department(user);
        if (department != DepartmentEnum.NO_CORRECT_DEPARTMENT) {
            if (department != DepartmentEnum.CLIENT) {
                EmployeeEnum employeeEnum = EmployeeEnum.GET.employeeState(department, userState);
                if (employeeEnum != EmployeeEnum.NO_CORRECT_STATE) {
                    return employeeActions(update, user, employeeEnum, sourceText);
                }
                else {
                    return null;
                }
            }
            else {
                ClientEnum clientEnum = ClientEnum.GET.userState(userState);
                if (clientEnum != ClientEnum.NO_CORRECT_STATE) {
                    return clientActions(update, user, clientEnum, sourceText);
                }
                else {
                    return null;
                }
            }
        }
        else {
            return null;
        }
    }

    private BotApiMethod<?> employeeActions(Update update, User user, EmployeeEnum employeeEnum, String sourceText) {
        switch (employeeEnum) {
            case OPERATOR_MAIN_MENU:
                return operatorModule.mainMenu(update, user, sourceText);
            default:
                return null;
        }
    }

    private BotApiMethod<?> clientActions(Update update, User user, ClientEnum clientEnum, String sourceText) {
        BotApiMethod<?> botApiMethod;
        switch (clientEnum) {
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
            default:
                botApiMethod = null;
        }
        return botApiMethod;
    }

    private User getUser(Update update) {
        String userId = update.getMessage().getFrom().getId().toString();
        return  clientService.getUser(userId);
    }
}
