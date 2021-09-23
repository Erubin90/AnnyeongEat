package tech.erubin.annyeong_eat.telegramBot.textMessages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.entity.OrderState;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.service.OrderServiceImpl;
import tech.erubin.annyeong_eat.service.OrderStatesServiceImpl;
import tech.erubin.annyeong_eat.service.UserServiceImpl;
import tech.erubin.annyeong_eat.service.UserStatesServiceImpl;

@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public abstract class Module{

    @Value("${module.message.error}")
    protected String error;

    @Value("${message.error.putButton}")
    protected String putButton;

    @Value("${mainMenu.message.help}")
    protected String help;

    @Value("${mainMenu.message.returnMainMenu}")
    protected String returnMainMenu;

    @Value("${mainMenu.message.choosingCafe}")
    protected String choosingCafe;

    @Value("${address.noError}")
    protected String addressNoError;

    @Value("${order.message.emptyReceipt}")
    protected String emptyReceipt;

    @Value("${order.message.hello}")
    protected String hello;

    @Value("${order.message.mainMenu}")
    protected String backToMainMenu;

    @Value("${order.message.backToChoosingCafe}")
    protected String backToChoosingCafe;

    @Value("${order.message.backToOrderMenu}")
    protected String backToOrderMenu;

    @Value("${order.message.backToAddress}")
    protected String backToAddress;

    @Value("${order.message.backToPhoneNumber}")
    protected String backToPhoneNumber;

    @Value("${order.message.backToPaymentMethod}")
    protected String backToPaymentMethod;

    @Value("${order.message.nextToAddress}")
    protected String nextToAddress;

    @Value("${order.message.nextToPhoneNumber}")
    protected String nextToPhoneNumber;

    @Value("${order.message.nextToPaymentMethod}")
    protected String nextToPaymentMethod;

    @Value("${regular.errorTrigger}")
    protected String errorTrigger;

    @Value("${message.error.nameCity}")
    protected String errorNameCity;

    //noError Message's
    @Value("${registration.message.name}")
    protected String nameNoError;

    @Value("${registration.message.surname}")
    protected String surnameNoError;

    @Value("${registration.message.phoneNumber}")
    protected String phoneNumberNoError;

    @Value("${registration.message.city}")
    protected String cityNoError;

    //Introduction message
    @Value("${registration.message.start}")
    protected String startClientRegistration;

    @Value("${registration.message.end}")
    protected String endUserRegistration;

    protected OrderServiceImpl orderService;
    protected UserServiceImpl userService;
    protected UserStatesServiceImpl userStatesService;
    protected OrderStatesServiceImpl orderStatesService;

    public Module(OrderServiceImpl orderService, UserServiceImpl userService, UserStatesServiceImpl userStatesService,
                  OrderStatesServiceImpl orderStatesService) {
        this.orderService = orderService;
        this.userService = userService;
        this.userStatesService = userStatesService;
        this.orderStatesService = orderStatesService;
    }

    protected SendMessage sendMessage(Update update, ReplyKeyboard replyKeyboard, String text, UserState userState) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(replyKeyboard);
        userStatesService.save(userState);
        return sendMessage;
    }

    protected SendMessage sendMessage(Update update, ReplyKeyboard replyKeyboard, String text, UserState userState,
                                      Order order, OrderState orderState) {
        SendMessage sendMessage = sendMessage(update, replyKeyboard, text, userState);
        orderService.save(order);
        orderStatesService.save(orderState);
        return sendMessage;
    }

    protected SendMessage sendMessage(Update update, ReplyKeyboard replyKeyboard, String text, UserState userState,
                                      Order order) {
        SendMessage sendMessage = sendMessage(update, replyKeyboard, text, userState);
        orderService.save(order);
        return sendMessage;
    }

    protected SendMessage sendMessage(Update update, ReplyKeyboard replyKeyboard, String text, UserState userState, User user) {
        SendMessage sendMessage = sendMessage(update, replyKeyboard, text, userState);
        userService.save(user);
        return sendMessage;
    }
}
