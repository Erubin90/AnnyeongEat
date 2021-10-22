package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractModule;

@Component
public class CourierModule extends AbstractModule {

    public CourierModule(OrderServiceImpl orderService, UserServiceImpl userService,
                         ClientStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                         EmployeeServiceImpl employeeService, @Lazy AnnyeongEatWebHook webHook) {
        super(orderService, userService, userStatesService, orderStatesService, employeeService, webHook);
    }

    public BotApiMethod<?> callbackCourierMainMenu(CallbackQuery callback, Order order, String tag) {
        BotApiMethod<?> botApiMethod;
        return null;
    }
}
