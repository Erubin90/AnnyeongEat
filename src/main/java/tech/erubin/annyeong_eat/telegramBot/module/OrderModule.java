package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.buttons.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.UserEnum;
import tech.erubin.annyeong_eat.telegramBot.handler.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Module;

import java.util.List;


@Component
public class OrderModule extends Module {
    private final AnnyeongEatWebHook webHook;

    private final CafeServiceImpl cafeService;
    private final DishServiceImpl dishService;
    private final DepartmentServiceImpl departmentService;

    private final ReplyButtons replyButtons;
    private final InlineButtons inlineButtons;

    private final CheckMessage checkMessage;

    public OrderModule(OrderServiceImpl orderService, UserServiceImpl userService,
                       UserStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                       @Lazy AnnyeongEatWebHook webHook, CafeServiceImpl cafeService, DishServiceImpl dishService,
                       DepartmentServiceImpl departmentService, ReplyButtons replyButtons, InlineButtons inlineButtons,
                       CheckMessage checkMessage) {
        super(orderService, userService, userStatesService, orderStatesService);
        this.webHook = webHook;
        this.cafeService = cafeService;
        this.dishService = dishService;
        this.departmentService = departmentService;
        this.replyButtons = replyButtons;
        this.inlineButtons = inlineButtons;
        this.checkMessage = checkMessage;
    }

    public BotApiMethod<?> start(Update update, User user, UserEnum userEnum){
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = error;
        Order order = orderService.getOrderById(user);
        UserState userState = null;
        OrderState orderState = null;
        List<String> cafeNames = cafeService.getCafeNameByCity(user.getCity());
        switch (userEnum) {
            case ORDER_CAFE:
                if (cafeNames.contains(sourceText)){
                    text = hello + " " + sourceText;
                    Cafe cafe = cafeService.getCafeByName(sourceText);
                    order = orderService.getOrderById(user, cafe);
                    order.setUsing(1);
                    orderState = orderStatesService.create(order, OrderEnum.ORDER_START_REGISTRATION.getValue());
                    userState = userStatesService.create(user, sourceText);
                    sendMessage.setReplyMarkup(replyButtons.userOrderMenu(order));
                }
                else if (sourceText.equals(replyButtons.getBack())) {
                    text = backToMainMenu;
                    userState = userStatesService.create(user, UserEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userMainMenu());
                }
                else {
                    text = putButton;
                    sendMessage.setReplyMarkup(replyButtons.userOrderCafe(cafeNames));
                }
                break;
            case ORDER_CAFE_MENU:
                sendMessage.setReplyMarkup(replyButtons.userOrderMenu(order));
                List<String> typeDishes = replyButtons.typeDishesInCafe(order);
                if (typeDishes.contains(sourceText)) {
                    text = sourceText + ":";
                    List<Dish> dishList = dishService.getDishListByType(sourceText);
                    sendMessage.setReplyMarkup(inlineButtons.typeDishesMenu(dishList));
                }
                else if (sourceText.equals(replyButtons.getBack())) {
                    text = backToChoosingCafe;
                    order.setUsing(1);
                    userState = userStatesService.create(user, UserEnum.ORDER_CAFE.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userOrderCafe(cafeNames));
                }
                else if (sourceText.equals(replyButtons.getNext())) {
                    if (order.getChequeDishList().size() == 0) {
                        text = emptyReceipt;
                    }
                    else {
                        text = nextToAddress;
                        userState = userStatesService.create(user, UserEnum.DELIVERY_ADDRESS.getValue());
                        sendMessage.setReplyMarkup(replyButtons.userOrderAddress(user));
                    }
                }
                else if (sourceText.equals("\uD83D\uDED2")) {
                    text = getFullOrder(order);
                    sendMessage.setReplyMarkup(inlineButtons.getFullOrderButtons(order));
                }
                else {
                    text = putButton;
                }
                break;
            case DELIVERY_ADDRESS:
                sendMessage.setReplyMarkup(replyButtons.userOrderAddress(user));
                if (sourceText.equals(replyButtons.getBack())) {
                    text = backToOrderMenu;
                    userState = userStatesService.create(user, UserEnum.ORDER_CAFE_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userOrderMenu(order));
                }
                else {
                    text = checkMessage.checkAddress(sourceText);
                    if (!text.contains(errorTrigger)) {
                        text = nextToPhoneNumber;
                        userState =
                                userStatesService.create(user, UserEnum.DELIVERY_PHONE_NUMBER.getValue());
                        order.setAddress(sourceText);
                        sendMessage.setReplyMarkup(replyButtons.userOrderPhoneNumber(user));
                    }
                }
                break;
            case DELIVERY_PHONE_NUMBER:
                sendMessage.setReplyMarkup(replyButtons.userOrderPhoneNumber(user));
                if (sourceText.equals(replyButtons.getBack())) {
                    text = backToAddress;
                    userState = userStatesService.create(user, UserEnum.DELIVERY_ADDRESS.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userOrderAddress(user));
                }
                else {
                    text = nextToPhoneNumber;
                    String checkText = checkMessage.checkPhoneNumber(sourceText);
                    if (!checkText.contains(errorTrigger)) {
                        text = nextToPaymentMethod;
                        userState =
                                userStatesService.create(user, UserEnum.DELIVERY_PAYMENT_METHOD.getValue());
                        if (sourceText.length() == 12) {
                            order.setPhoneNumber("8" + sourceText.substring(2, 12));
                        }
                        order.setPhoneNumber(sourceText);
                        sendMessage.setReplyMarkup(replyButtons.userOrderPayment());
                    }
                }
                break;
            case DELIVERY_PAYMENT_METHOD:
                List<String> paymentMethod = replyButtons.paymentMethod();
                if (paymentMethod.contains(sourceText)) {
                    text = getFullOrder(order);
                    order.setPaymentMethod(sourceText);
                    userState =
                            userStatesService.create(user, UserEnum.DELIVERY_CONFIRMATION.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userOrderConfirmation());
                }
                else if (sourceText.equals(replyButtons.getBack())) {
                    text = backToPhoneNumber;
                    userState = userStatesService.create(user, UserEnum.DELIVERY_PHONE_NUMBER.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userOrderPhoneNumber(user));
                }
                else {
                    text = putButton;
                    sendMessage.setReplyMarkup(replyButtons.userOrderPayment());
                }
                break;
            case DELIVERY_CONFIRMATION:
                if (sourceText.equals(replyButtons.getConfirm())) {
                    text = returnMainMenu;
                    order.setUsing(0);
                    orderState = orderStatesService.create(order, OrderEnum.ORDER_END_REGISTRATION.getValue());
                    userState = userStatesService.create(user, UserEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userMainMenu());
                    if (user.getDepartmentsList() != null) {
                        sendMessageOperator(order);
                    }
                }
                else if (sourceText.equals(replyButtons.getBack())) {
                    text = backToPaymentMethod;
                    userState = userStatesService.create(user, UserEnum.DELIVERY_PAYMENT_METHOD.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userOrderPayment());
                }
                else {
                    text = putButton;
                    sendMessage.setReplyMarkup(replyButtons.userOrderConfirmation());
                }
                break;
        }
        return sendMessage(sendMessage,userState, order, orderState, text);
    }

    private void sendMessageOperator(Order order) {
        List<Department> listOperatorsInCafe = departmentService
                        .getEmployeeByCafeIdAndDepartmenName(order.getCafeId(), EmployeeEnum.OPERATOR.getValue());
        if (listOperatorsInCafe != null) {
            webHook.sendMessageDepartment(listOperatorsInCafe, EmployeeEnum.OPERATOR, getFullOrder(order), order);
        }
    }
}
