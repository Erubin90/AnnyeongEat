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
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.DepartmentEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
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

    public BotApiMethod<?> handleUpdate(Update update) {
        String sourceText = update.getMessage().getText();
        User user = getUser(update);
        DepartmentEnum department = DepartmentEnum.GET.department(user);
        if (department != DepartmentEnum.NO_CORRECT_DEPARTMENT) {
            boolean isEmployee = department != DepartmentEnum.CLIENT;
            if (isEmployee) {
                EmployeeState employeeState = employeeStateService.getState(user);
                EmployeeEnum employeeEnum = EmployeeEnum.GET.employeeState(department, employeeState);
                if (employeeEnum != EmployeeEnum.NO_CORRECT_STATE) {
                    return employeeActions(update, user, department, employeeEnum, sourceText, isEmployee);
                }
                else {
                    return null;
                }
            }
            else {
                ClientState clientState = clientStateService.getState(user);
                ClientEnum clientEnum = ClientEnum.GET.userState(clientState);
                if (clientEnum != ClientEnum.NO_CORRECT_STATE) {
                    return clientActions(update, user, clientEnum, sourceText, isEmployee);
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

    private BotApiMethod<?> employeeActions(Update update, User user, DepartmentEnum department, EmployeeEnum employeeEnum, String sourceText, boolean isEmployee) {
        switch (department){
            case OPERATOR:
                switch (employeeEnum) {
                    case OPERATOR_MAIN_MENU:
                        return operatorModule.mainMenu(update, user, sourceText);
                    case OPERATOR_CHOOSING_CAFE:
                        return orderModule.choosingCafe(update, user, sourceText, isEmployee);
                    case OPERATOR_CHOOSING_TABLE:
                        return operatorModule.choosingTable(update, user, sourceText);
                    case OPERATOR_CAFE_MENU:
                        return orderModule.cafeMenu(update, user, sourceText, isEmployee);
                    case OPERATOR_PAYMENT_METHOD:
                        return orderModule.deliveryPaymentMethod(update, user, sourceText, isEmployee);
                    case OPERATOR_COMMENT:
                        return orderModule.comment(update, user, sourceText, isEmployee);
                    case OPERATOR_CONFIRMATION:
                        return orderModule.deliveryConfirmation(update, user, sourceText, isEmployee);
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    private BotApiMethod<?> clientActions(Update update, User user, ClientEnum clientEnum, String sourceText, boolean isEmployee) {
        switch (clientEnum) {
            case MAIN_MENU:
                return mainMenuModule.mainMenu(update, user, sourceText);
            case ORDER_CHECK:
                return mainMenuModule.orderChek(update, user, sourceText);
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
            case REGISTRATION_SURNAME:
                return registrationModule.surname(update, user, sourceText);
            case REGISTRATION_PHONE_NUMBERS:
                return registrationModule.phoneNumber(update, user, sourceText);
            default:
                return null;
        }
    }

    private User getUser(Update update) {
        String userId = update.getMessage().getFrom().getId().toString();
        return  clientService.getUser(userId);
    }
}
