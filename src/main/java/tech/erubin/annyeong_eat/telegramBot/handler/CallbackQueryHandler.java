package tech.erubin.annyeong_eat.telegramBot.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientStates;
import tech.erubin.annyeong_eat.telegramBot.enums.Departments;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeStates;
import tech.erubin.annyeong_eat.telegramBot.module.CourierModule;
import tech.erubin.annyeong_eat.telegramBot.module.MainMenuModule;
import tech.erubin.annyeong_eat.telegramBot.module.OperatorModule;
import tech.erubin.annyeong_eat.telegramBot.module.OrderModule;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractHandler;

@Component
@AllArgsConstructor
public class CallbackQueryHandler extends AbstractHandler {
    private final MainMenuModule mainMenuModule;
    private final OrderModule orderModule;
    private final OperatorModule operatorModule;
    private final CourierModule courierModule;

    private final UserServiceImpl clientService;
    private final OrderServiceImpl orderService;
    private final DishServiceImpl dishService;
    private final EmployeeStateServiceImpl employeeStateService;
    private final ClientStatesServiceImpl clientStateService;

    public BotApiMethod<?> handleUpdate(CallbackQuery callback) {
        String userId = callback.getFrom().getId().toString();
        String[] idList = callback.getData().split("/");

        Order order = orderService.getOrderByStringId(idList[0]);
        Dish dish = dishService.getDishByName(idList[1]);
        User user = clientService.getOrCreateUser(userId);
        String tag = idList[2];
        Departments department = Departments.department(user);
        if (tag.equals(tagInfo)) {
            return answerCallbackQuery(callback, messageInfo);
        }
        if (department != Departments.UNKNOWN) {
            if (department != Departments.CLIENT) {
                EmployeeState employeeState = employeeStateService.getState(user);
                EmployeeStates employeeStates = EmployeeStates.employeeState(employeeState);
                if (employeeStates != EmployeeStates.UNKNOWN) {
                    return employeeCallback(callback, order, dish, employeeStates, tag);
                } else {
                    return null;
                }
            }
            else {
                ClientState clientState = clientStateService.getState(user);
                ClientStates clientStates = ClientStates.userState(clientState);
                if (clientStates != ClientStates.UNKNOWN) {
                    return clientCallback(callback, user, order, dish, clientStates, tag);
                } else {
                    return null;
                }
            }
        }
        else {
            return null;
        }
    }

    private BotApiMethod<?> employeeCallback(CallbackQuery callback, Order order, Dish dish,
                                             EmployeeStates employeeStates, String tag) {
        switch (employeeStates) {
            case OPERATOR_MAIN_MENU:
                return operatorModule.callbackOperatorMainMenu(callback, order, dish, tag);
            case OPERATOR_CAFE_MENU:
                return orderModule.callbackOrderCafeMenu(callback, order, dish, tag);
            case COURIER_MAIN_MENU:
                return courierModule.callbackCourierMainMenu(callback, order, tag);
            default:
                return answerCallbackQuery(callback, buttonNotWork);
        }
    }

    private BotApiMethod<?> clientCallback(CallbackQuery callback, User user, Order order, Dish dish,
                                           ClientStates clientStates, String tag) {
        switch (clientStates) {
            case ORDER_CAFE_MENU:
                return orderModule.callbackOrderCafeMenu(callback, order, dish, tag);
            case ORDER_CHECK:
                return mainMenuModule.callbackOrderCheck(callback, user, tag);
            default:
                return answerCallbackQuery(callback, buttonNotWork);
        }
    }

    protected AnswerCallbackQuery answerCallbackQuery(CallbackQuery callback, String text) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callback.getId());
        answerCallbackQuery.setText(text);
        answerCallbackQuery.setShowAlert(false);
        return answerCallbackQuery;
    }

}