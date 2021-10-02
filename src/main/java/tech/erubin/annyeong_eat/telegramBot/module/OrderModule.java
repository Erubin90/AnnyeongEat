package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.buttons.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;
import tech.erubin.annyeong_eat.telegramBot.handler.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Module;

import java.util.List;


@Component
public class OrderModule extends Module {
    private final CafeServiceImpl cafeService;
    private final DishServiceImpl dishService;

    private final ReplyButtons replyButtons;
    private final InlineButtons inlineButtons;

    private final CheckMessage checkMessage;

    public OrderModule(OrderServiceImpl orderService, UserServiceImpl userService,
                       UserStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                       EmployeeServiceImpl departmentService, CafeServiceImpl cafeService,
                       DishServiceImpl dishService, ReplyButtons replyButtons, InlineButtons inlineButtons,
                       CheckMessage checkMessage, @Lazy AnnyeongEatWebHook webHook) {
        super(orderService, userService, userStatesService, orderStatesService, departmentService, webHook);
        this.cafeService = cafeService;
        this.dishService = dishService;
        this.replyButtons = replyButtons;
        this.inlineButtons = inlineButtons;
        this.checkMessage = checkMessage;
    }

    public SendMessage choosingCafe(Update update, User user, String sourceText){
        List<String> cafeNames = cafeService.getCafeNameByCity(user.getCity());
        Order order = orderService.getOrderByUser(user);
        UserState userState = null;
        OrderState orderState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (cafeNames.contains(sourceText)){
            text = hello + " " + sourceText;
            Cafe cafe = cafeService.getCafeByName(sourceText);
            order = orderService.getOrderByUserIdAndCafeId(user, cafe);
            order.setUsing(1);
            orderState = orderStatesService.create(order, OrderEnum.ORDER_START_REGISTRATION.getValue());
            userState = userStatesService.create(user, sourceText);
            replyKeyboard = replyButtons.userOrderMenu(order);
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToMainMenu;
            userState = userStatesService.create(user, ClientEnum.MAIN_MENU.getValue());
            replyKeyboard = replyButtons.userMainMenu();
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderCafe(user);
        }
        return sendMessage(update, replyKeyboard, text, userState, order, orderState);
    }

    public SendMessage cafeMenu(Update update, User user, String sourceText){
        Order order = orderService.getOrderByUser(user);
        List<String> typeDishes = replyButtons.typeDishesInCafe(order);
        UserState userState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (typeDishes.contains(sourceText)) {
            text = sourceText + ":";
            List<Dish> dishList = dishService.getDishListByType(sourceText);
            replyKeyboard = inlineButtons.typeDishesMenu(order, dishList);
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            order.setUsing(1);
            text = backToChoosingCafe;
            userState = userStatesService.create(user, ClientEnum.ORDER_CAFE.getValue());
            replyKeyboard = replyButtons.userOrderCafe(user);
        }
        else if (sourceText.equals(replyButtons.getNext())) {
            if (order.getChequeDishList().size() == 0) {
                text = emptyReceipt;
                replyKeyboard = replyButtons.userOrderMenu(order);
            }
            else {
                text = nextToAddress;
                userState = userStatesService.create(user, ClientEnum.DELIVERY_ADDRESS.getValue());
                replyKeyboard = replyButtons.userOrderAddress(user);
            }
        }
        else if (sourceText.equals("\uD83D\uDED2")) {
            text = getChequeText(order, false);
            replyKeyboard = inlineButtons.fullOrderButtons(order);
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderMenu(order);
        }
        return sendMessage(update, replyKeyboard, text, userState, order);
    }

    public SendMessage deliveryAddress(Update update, User user, String sourceText){
        Order order = orderService.getOrderByUser(user);
        UserState userState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getBack())) {
            text = backToOrderMenu;
            userState = userStatesService.create(user, ClientEnum.ORDER_CAFE_MENU.getValue());
            replyKeyboard = replyButtons.userOrderMenu(order);
        }
        else {
            text = checkMessage.checkAddress(sourceText);
            if (!text.contains(errorTrigger)) {
                order.setAddress(sourceText);
                text = nextToPhoneNumber;
                userState = userStatesService.create(user, ClientEnum.DELIVERY_PHONE_NUMBER.getValue());
                replyKeyboard = replyButtons.userOrderPhoneNumber(user);
            }
            else {
                replyKeyboard = replyButtons.userOrderAddress(user);
            }
        }
        return sendMessage(update, replyKeyboard, text, userState, order);
    }

    public SendMessage deliveryPhoneNumber(Update update, User user, String sourceText){
        Order order = orderService.getOrderByUser(user);
        UserState userState = null;
        String text;
        ReplyKeyboard replyKeyboard = replyButtons.userOrderPhoneNumber(user);
        if (sourceText.equals(replyButtons.getBack())) {
            text = backToAddress;
            userState = userStatesService.create(user, ClientEnum.DELIVERY_ADDRESS.getValue());
            replyKeyboard = replyButtons.userOrderAddress(user);
        }
        else {
            text = nextToPhoneNumber;
            String checkText = checkMessage.checkPhoneNumber(sourceText);
            if (!checkText.contains(errorTrigger)) {
                text = nextToPaymentMethod;
                userState = userStatesService.create(user, ClientEnum.DELIVERY_PAYMENT_METHOD.getValue());
                if (sourceText.length() == 12) {
                    order.setPhoneNumber("8" + sourceText.substring(2, 12));
                }
                order.setPhoneNumber(sourceText);
                replyKeyboard = replyButtons.userOrderPayment();
            }
        }
        return sendMessage(update, replyKeyboard, text, userState, order);
    }

    public SendMessage deliveryPaymentMethod(Update update, User user, String sourceText){
        Order order = orderService.getOrderByUser(user);
        List<String> paymentMethod = replyButtons.paymentMethod();
        UserState userState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (paymentMethod.contains(sourceText)) {
            order.setPaymentMethod(sourceText);
            text = getChequeText(order, false);
            userState = userStatesService.create(user, ClientEnum.DELIVERY_CONFIRMATION.getValue());
            replyKeyboard = replyButtons.userOrderConfirmation();
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToPhoneNumber;
            userState = userStatesService.create(user, ClientEnum.DELIVERY_PHONE_NUMBER.getValue());
            replyKeyboard = replyButtons.userOrderPhoneNumber(user);
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderPayment();
        }

        return sendMessage(update, replyKeyboard, text, userState, order);
    }

    public SendMessage deliveryConfirmation(Update update, User user, String sourceText){
        Order order = orderService.getOrderByUser(user);
        UserState userState = null;
        OrderState orderState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getConfirm())) {
            order.setUsing(0);
            text = returnMainMenu;
            orderState = orderStatesService.create(order, OrderEnum.ORDER_END_REGISTRATION.getValue());
            userState = userStatesService.create(user, ClientEnum.MAIN_MENU.getValue());
            replyKeyboard = replyButtons.userMainMenu();
            if (user.getEmployeeList() != null) {
                sendMessageOperator(order);
            }
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToPaymentMethod;
            userState = userStatesService.create(user, ClientEnum.DELIVERY_PAYMENT_METHOD.getValue());
            replyKeyboard = replyButtons.userOrderPayment();
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderConfirmation();
        }

        return sendMessage(update, replyKeyboard, text, userState, order, orderState);
    }
}
