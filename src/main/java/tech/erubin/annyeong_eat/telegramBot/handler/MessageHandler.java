package tech.erubin.annyeong_eat.telegramBot.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.entity.EmployeeState;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.ClientState;
import tech.erubin.annyeong_eat.service.EmployeeStateServiceImpl;
import tech.erubin.annyeong_eat.service.UserServiceImpl;
import tech.erubin.annyeong_eat.service.ClientStatesServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientStates;
import tech.erubin.annyeong_eat.telegramBot.enums.Departments;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeStates;
import tech.erubin.annyeong_eat.telegramBot.module.*;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractHandler;

@Component
@AllArgsConstructor
public class MessageHandler extends AbstractHandler {
    private final RegistrationModule registrationModule;
    private final MainMenuModule mainMenuModule;
    private final OrderModule orderModule;
    private final OperatorModule operatorModule;

    private final UserServiceImpl clientService;
    private final EmployeeStateServiceImpl employeeStateService;
    private final ClientStatesServiceImpl clientStateService;

    public BotApiMethod<?> handleUpdate(Update update, String posterToken) {
        String sourceText = update.getMessage().getText();
        User user = getUser(update);
        Departments department = Departments.department(user);
        if (department != Departments.UNKNOWN) {
            boolean isEmployee = department != Departments.CLIENT;
            if (isEmployee) {
                EmployeeState employeeState = employeeStateService.getState(user);
                EmployeeStates employeeStates = EmployeeStates.employeeState(employeeState);
                if (employeeStates != EmployeeStates.UNKNOWN) {
                    return employeeActions(update, user, department, employeeStates, sourceText, isEmployee);
                }
                else {
                    return null;
                }
            }
            else {
                ClientState clientState = clientStateService.getState(user);
                ClientStates clientStates = ClientStates.userState(clientState);
                if (clientStates != ClientStates.UNKNOWN) {
                    return clientActions(update, user, clientStates, sourceText, isEmployee);
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

    private BotApiMethod<?> employeeActions(Update update, User user, Departments department, EmployeeStates employeeStates, String sourceText, boolean isEmployee) {
        switch (department){
            case OPERATOR:
                switch (employeeStates) {
                    case OPERATOR_MAIN_MENU:
                        return operatorModule.mainMenu(update, user, sourceText);
                    case OPERATOR_CHOOSING_CAFE:
                        return orderModule.choosingCafe(update, user, sourceText, isEmployee);
                    case CHOOSING_TABLE:
                        return operatorModule.choosingTable(update, user, sourceText);
                    case CAFE_MENU:
                        return orderModule.cafeMenu(update, user, sourceText, isEmployee);
                    case PAYMENT_METHOD:
                        return orderModule.deliveryPaymentMethod(update, user, sourceText, isEmployee);
                    case COMMENT:
                        return orderModule.comment(update, user, sourceText, isEmployee);
                    case CONFIRMATION:
                        return orderModule.deliveryConfirmation(update, user, sourceText, isEmployee);
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    private BotApiMethod<?> clientActions(Update update, User user, ClientStates clientStates, String sourceText, boolean isEmployee) {
        switch (clientStates) {
            case MAIN_MENU:
                return mainMenuModule.mainMenu(update, user, sourceText);
            case ORDER_CHECK:
                return mainMenuModule.orderCheck(update, user, sourceText);
            case HELP:
                return mainMenuModule.help(update, user, sourceText);
            case PROFILE:
                return mainMenuModule.profile(update, user, sourceText);
            case ORDER_CAFE:
                return orderModule.choosingCafe(update, user, sourceText, isEmployee);
            case ORDER_CAFE_MENU:
                return orderModule.cafeMenu(update, user, sourceText, isEmployee);
            case ORDER_METHOD_OF_OBTAINING:
                return orderModule.methodObtaining(update, user, sourceText);
            case DELIVERY_ADDRESS:
                return orderModule.deliveryAddress(update, user, sourceText);
            case DELIVERY_PHONE_NUMBER:
                return orderModule.deliveryPhoneNumber(update,user, sourceText);
            case DELIVERY_PAYMENT_METHOD:
                return orderModule.deliveryPaymentMethod(update, user, sourceText, isEmployee);
            case ORDER_COMMENT:
                return orderModule.comment(update, user, sourceText, isEmployee);
            case DELIVERY_CONFIRMATION:
                return orderModule.deliveryConfirmation(update, user, sourceText, isEmployee);
            case REGISTRATION_START:
                return registrationModule.start(update, user);
            case REGISTRATION_CITY:
                return registrationModule.city(update, user, sourceText);
            case REGISTRATION_NAME:
                return registrationModule.name(update, user, sourceText);
            case REGISTRATION_PHONE_NUMBERS:
                return registrationModule.phoneNumber(update, user, sourceText);
            default:
                return null;
        }
    }

    private User getUser(Update update) {
        String userId = update.getMessage().getFrom().getId().toString();
        return  clientService.getOrCreateUser(userId);
    }
}
