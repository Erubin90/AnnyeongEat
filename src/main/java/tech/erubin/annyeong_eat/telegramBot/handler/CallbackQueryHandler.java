package tech.erubin.annyeong_eat.telegramBot.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.DepartmentEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.module.CourierModule;
import tech.erubin.annyeong_eat.telegramBot.module.OperatorModule;
import tech.erubin.annyeong_eat.telegramBot.module.OrderModule;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractHandler;

@Component
@AllArgsConstructor
public class CallbackQueryHandler extends AbstractHandler {
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
        User user = clientService.getUser(userId);
        String tag = idList[2];
        DepartmentEnum department = DepartmentEnum.GET.department(user);
        if (tag.equals(tagInfo)) {
            return answerCallbackQuery(callback, messageInfo);
        }
        if (department != DepartmentEnum.NO_CORRECT_DEPARTMENT) {
            if (department != DepartmentEnum.CLIENT) {
                EmployeeState employeeState = employeeStateService.getState(user);
                EmployeeEnum employeeEnum = EmployeeEnum.GET.employeeState(department, employeeState);
                if (employeeEnum != EmployeeEnum.NO_CORRECT_STATE) {
                    return employeeCallback(callback, order, dish, employeeEnum, tag);
                } else {
                    return null;
                }
            }
            else {
                ClientState clientState = clientStateService.getState(user);
                ClientEnum clientEnum = ClientEnum.GET.userState(clientState);
                if (clientEnum != ClientEnum.NO_CORRECT_STATE) {
                    return clientCallback(callback, order, dish, clientEnum, tag);
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
                                             EmployeeEnum employeeEnum, String tag) {
        switch (employeeEnum) {
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

    private BotApiMethod<?> clientCallback(CallbackQuery callback, Order order, Dish dish,
                                           ClientEnum clientEnum, String tag) {
        switch (clientEnum) {
            case ORDER_CAFE_MENU:
                return orderModule.callbackOrderCafeMenu(callback, order, dish, tag);
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