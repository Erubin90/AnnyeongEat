package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.buttons.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientStates;
import tech.erubin.annyeong_eat.telegramBot.enums.Departments;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeStates;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderStates;
import tech.erubin.annyeong_eat.telegramBot.handler.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractModule;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class OrderModule extends AbstractModule {
    private final CafeServiceImpl cafeService;
    private final DishServiceImpl dishService;
    private final ChequeDishServiceImpl chequeDishService;
    private final EmployeeStateServiceImpl employeeStateService;

    private final ReplyButtons replyButtons;
    private final InlineButtons inlineButtons;

    private final CheckMessage checkMessage;

    public OrderModule(OrderServiceImpl orderService, UserServiceImpl userService,
                       ClientStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                       EmployeeServiceImpl employeeService, CafeServiceImpl cafeService,
                       DishServiceImpl dishService, ReplyButtons replyButtons, InlineButtons inlineButtons,
                       CheckMessage checkMessage, @Lazy AnnyeongEatWebHook webHook,
                       ChequeDishServiceImpl chequeDishService, EmployeeStateServiceImpl employeeStateService) {
        super(orderService, userService, userStatesService, orderStatesService, employeeService, webHook);
        this.cafeService = cafeService;
        this.dishService = dishService;
        this.replyButtons = replyButtons;
        this.inlineButtons = inlineButtons;
        this.checkMessage = checkMessage;
        this.chequeDishService = chequeDishService;
        this.employeeStateService = employeeStateService;
    }

    public SendMessage choosingCafe(Update update, User user, String sourceText, boolean isEmployee) {
        String text;
        ReplyKeyboard replyKeyboard;
        List<String> cafeNames;
        if (isEmployee) {
            List<Cafe> cafeIdList = employeeService.getCafeByUserId(user);
            cafeNames = cafeIdList.stream().map(Cafe::getName).collect(Collectors.toList());
        }
        else {
            cafeNames = cafeService.getCafeNameByCity(user.getCity());
        }
        if (cafeNames.contains(sourceText)) {
            Cafe cafe = cafeService.getCafeByName(sourceText);
            Order order = orderService.getOrderByUserIdAndCafeId(user, cafe);
            order.setUsing(1);
            if (isEmployee) {
                text = choosingTable;
                replyKeyboard = replyButtons.operatorChoosingTable(cafe);
                employeeStateService.createAndSave(user, EmployeeStates.CHOOSING_TABLE.getState());
            }
            else {
                text = hello + " " + sourceText;
                replyKeyboard = replyButtons.userOrderMenu(order);
                userStatesService.createAndSave(user, ClientStates.ORDER_CAFE_MENU.getState());
            }
            orderService.save(order);
            orderStatesService.createAndSave(order, OrderStates.START_REGISTRATION.getState());
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToMainMenu;
            if (isEmployee) {
                replyKeyboard = replyButtons.operatorMainMenu();
                employeeStateService.createAndSave(user, EmployeeStates.OPERATOR_MAIN_MENU.getState());
            }
            else {
                replyKeyboard = replyButtons.userMainMenu();
                userStatesService.createAndSave(user, ClientStates.MAIN_MENU.getState());
            }
        }
        else {
            text = putButton;
            List<String> buttonName = cafeService.getCafeNameByCity(user.getCity());
            replyKeyboard = replyButtons.userOrderCafe(buttonName);
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage cafeMenu(Update update, User user, String sourceText, boolean isEmployee) {
        Order order = orderService.getOrderByUser(user);
        List<String> typeDishes = replyButtons.typeDishesInCafe(order.getCafeId());
        String text;
        ReplyKeyboard replyKeyboard;
        if (typeDishes.contains(sourceText)) {
            text = sourceText + ":";
            List<Dish> dishList = dishService.getDishListByType(sourceText);
            replyKeyboard = inlineButtons.typeDishesMenu(order, dishList);
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            order.setUsing(1);
            orderService.save(order);
            if (isEmployee) {
                text = choosingTable;
                Cafe cafe = order.getCafeId();
                replyKeyboard = replyButtons.operatorChoosingTable(cafe);
                employeeStateService.createAndSave(user, EmployeeStates.CHOOSING_TABLE.getState());
            }
            else {
                text = backToChoosingCafe;
                List<String> buttonName = cafeService.getCafeNameByCity(user.getCity());
                replyKeyboard = replyButtons.userOrderCafe(buttonName);
                userStatesService.createAndSave(user, ClientStates.ORDER_CAFE.getState());
            }
        }
        else if (sourceText.equals(replyButtons.getNext())) {
            if (order.getChequeDishList().size() == 0) {
                text = emptyReceipt;
                replyKeyboard = replyButtons.userOrderMenu(order);
            }
            else {
                if (isEmployee) {
                    text = nextToPaymentMethod;
                    replyKeyboard = replyButtons.userOrderPayment();
                    employeeStateService.createAndSave(user, EmployeeStates.PAYMENT_METHOD.getState());
                }
                else {
                    text = nextToObtaining;
                    replyKeyboard = replyButtons.userOrderObtaining();
                    userStatesService.createAndSave(user, ClientStates.ORDER_METHOD_OF_OBTAINING.getState());
                }
            }
        }
        else if (sourceText.equals("\uD83D\uDED2")) {
            text = getChequeText(order, isEmployee);
            replyKeyboard = inlineButtons.fullOrderButtons(order);
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderMenu(order);
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage methodObtaining(Update update, User user, String sourceText) {
        Order order = orderService.getOrderByUser(user);
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getDelivery())) {
            text = nextToAddress;
            replyKeyboard = replyButtons.userOrderAddress(user);
            order.setObtainingMethod(replyButtons.getDelivery());
            orderService.save(order);
            userStatesService.createAndSave(user, ClientStates.DELIVERY_ADDRESS.getState());
        }
        else if (sourceText.equals(replyButtons.getPickup())) {
            text = nextToPhoneNumber;
            replyKeyboard = replyButtons.userOrderPhoneNumber(user);
            order.setPriceDelivery(0);
            order.setObtainingMethod(replyButtons.getPickup());
            orderService.save(order);
            userStatesService.createAndSave(user, ClientStates.DELIVERY_PHONE_NUMBER.getState());
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToOrderMenu;
            replyKeyboard = replyButtons.userOrderMenu(order);
            userStatesService.createAndSave(user, ClientStates.ORDER_CAFE_MENU.getState());
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderObtaining();
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage deliveryAddress(Update update, User user, String sourceText) {
        Order order = orderService.getOrderByUser(user);
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getBack())) {
            order.setAddress("");
            text = backToObtaining;
            replyKeyboard = replyButtons.userOrderObtaining();
            orderService.save(order);
            userStatesService.createAndSave(user, ClientStates.ORDER_METHOD_OF_OBTAINING.getState());
        }
        else {
            text = checkMessage.checkAddress(sourceText);
            if (!text.contains(errorTrigger)) {
                order.setAddress(sourceText);
                text = nextToPhoneNumber;
                replyKeyboard = replyButtons.userOrderPhoneNumber(user);
                orderService.save(order);
                userStatesService.createAndSave(user, ClientStates.DELIVERY_PHONE_NUMBER.getState());
            }
            else {
                replyKeyboard = replyButtons.userOrderAddress(user);
            }
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage deliveryPhoneNumber(Update update, User user, String sourceText) {
        Order order = orderService.getOrderByUser(user);
        String text;
        ReplyKeyboard replyKeyboard = replyButtons.userOrderPhoneNumber(user);
        if (sourceText.equals(replyButtons.getBack())) {
            if (order.getObtainingMethod().equals(replyButtons.getPickup())) {
                text = backToObtaining;
                replyKeyboard = replyButtons.userOrderObtaining();
                userStatesService.createAndSave(user, ClientStates.ORDER_METHOD_OF_OBTAINING.getState());
            }
            else {
                text = backToAddress;
                replyKeyboard = replyButtons.userOrderAddress(user);
                userStatesService.createAndSave(user, ClientStates.DELIVERY_ADDRESS.getState());
            }

        }
        else {
            text = nextToPhoneNumber;
            String checkText = checkMessage.checkPhoneNumber(sourceText);
            if (!checkText.contains(errorTrigger)) {
                text = nextToPaymentMethod;
                if (sourceText.length() == 12) {
                    order.setPhoneNumber("8" + sourceText.substring(2, 12));
                }
                order.setPhoneNumber(sourceText);
                replyKeyboard = replyButtons.userOrderPayment();
                orderService.save(order);
                userStatesService.createAndSave(user, ClientStates.DELIVERY_PAYMENT_METHOD.getState());
            }
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage deliveryPaymentMethod(Update update, User user, String sourceText, boolean isEmployee) {
        Order order = orderService.getOrderByUser(user);
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getCards())) {
            text = nextToComment;
            order.setPaymentMethod(sourceText);
            replyKeyboard = replyButtons.userComment();
            orderService.save(order);
            if (isEmployee) {
                employeeStateService.createAndSave(user, EmployeeStates.COMMENT.getState());
            }
            else {
                userStatesService.createAndSave(user, ClientStates.ORDER_COMMENT.getState());
            }
        }
        else if (sourceText.equals(replyButtons.getCash())) {
            text = nextToComment;
            order.setPaymentMethod(sourceText);
            replyKeyboard = replyButtons.userComment();
            orderService.save(order);
            if (isEmployee) {
                employeeStateService.createAndSave(user, EmployeeStates.COMMENT.getState());
            }
            else {
                userStatesService.createAndSave(user, ClientStates.ORDER_COMMENT.getState());
            }
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToPhoneNumber;
            replyKeyboard = replyButtons.userOrderPhoneNumber(user);
            if (isEmployee) {
                employeeStateService.createAndSave(user, EmployeeStates.CAFE_MENU.getState());
            }
            else {
                userStatesService.createAndSave(user, ClientStates.DELIVERY_PHONE_NUMBER.getState());
            }
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderPayment();
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage comment (Update update, User user, String sourceText, boolean isEmployee) {
        Order order = orderService.getOrderByUser(user);
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getBack())) {
            text = backToPaymentMethod;
            replyKeyboard = replyButtons.userOrderPayment();
            if (isEmployee) {
                employeeStateService.createAndSave(user, EmployeeStates.PAYMENT_METHOD.getState());
            }
            else {
                userStatesService.createAndSave(user, ClientStates.DELIVERY_PAYMENT_METHOD.getState());
            }
        }
        else {
            if (sourceText.length() <= 200) {
                text = getChequeText(order, isEmployee);
                replyKeyboard = replyButtons.userOrderConfirmation();
                if (!sourceText.equals(replyButtons.getNext())) {
                    order.setComment(sourceText);
                    orderService.save(order);
                }
                if (isEmployee) {
                    employeeStateService.createAndSave(user, EmployeeStates.CONFIRMATION.getState());
                } else {
                    userStatesService.createAndSave(user, ClientStates.DELIVERY_CONFIRMATION.getState());
                }
            } else {
                text = errorComment;
                replyKeyboard = replyButtons.userComment();
            }
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage deliveryConfirmation(Update update, User user, String sourceText, boolean isEmployee) {
        Order order = orderService.getOrderByUser(user);
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getConfirm())) {
            order.setUsing(0);
            if (isEmployee) {
                text = operatorMainMenu;
                replyKeyboard = replyButtons.operatorMainMenu();
                employeeStateService.createAndSave(user, EmployeeStates.OPERATOR_MAIN_MENU.getState());
            }
            else {
                text = returnMainMenu;
                replyKeyboard = replyButtons.userMainMenu();
                userStatesService.createAndSave(user, ClientStates.MAIN_MENU.getState());
            }
            sendMessageDepartment(order, Departments.OPERATOR);
            orderService.save(order);
            orderStatesService.createAndSave(order, OrderStates.END_REGISTRATION.getState());
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToComment;
            replyKeyboard = replyButtons.userComment();
            if (isEmployee) {
                employeeStateService.createAndSave(user, EmployeeStates.COMMENT.getState());
            }
            else {
                userStatesService.createAndSave(user, ClientStates.ORDER_COMMENT.getState());
            }
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderConfirmation();
        }
        return message(update, replyKeyboard, text);
    }

    public BotApiMethod<?> callbackOrderCafeMenu(CallbackQuery callback, Order order, Dish dish, String tag) {
        String text = error;
        InlineKeyboardMarkup inlineMarkup;
        ChequeDish chequeDish = chequeDishService.getChequeByOrderAndDish(order, dish);
        int count = chequeDish.getCountDishes();
        if (tag.equals("m+") || tag.equals("b+")) {
            count++;
            text = dish.getName() + " " + addDish;
        }
        else if (tag.equals("m-") || tag.equals("b-")) {
            if (count >= 0) {
                if (count > 0) {
                    count--;
                    text = dish.getName() + " " + subDish;
                }
                else {
                    text = dish.getName() + " " + emptyDish;
                }
            }
        }
        else if (tag.equals("mx") || tag.equals("bx")) {
            text = count + " " + dish.getName() + " " + subDish;
            count = 0;
        }
        chequeDishService.saveOrDeleteChequeDish(chequeDish, count);
        if (tag.equals(String.valueOf(dish.getId()))) {
            text = getDishText(dish);
            inlineMarkup = inlineButtons.clientCheque(chequeDish);
            String chatId = callback.getMessage().getChatId().toString();
            if (!webHook.sendPhoto(chatId, text, dish.getLinkPhoto(), inlineMarkup)) {
                text = error;
            }
        }
        else if (tag.equals("m+") || tag.equals("mx") || tag.equals("m-")) {
            inlineMarkup = inlineButtons.clientCheque(chequeDish);
            EditMessageReplyMarkup editMessageReplyMarkup = editMessageReplyMarkup(callback, inlineMarkup);
            if (!webHook.updateMarkups(editMessageReplyMarkup, count)) {
                text = error;
            }
        }
        else if (tag.equals("b+") || tag.equals("bx") || tag.equals("b-")) {
            inlineMarkup = inlineButtons.fullOrderButtons(order);
            String editText = getChequeText(order, false);
            EditMessageText editMessageText = editMessageText(callback, editText, inlineMarkup);
            if (!webHook.updateText(editMessageText)) {
                text = error;
            }
        }
        return answerCallbackQuery(callback, text);
    }

    private String getDishText(Dish dish) {
        String dishName = dish.getName();
        double dishPrice = dish.getPrice();
        String dishComment = dish.getComment();
        return String.format("%s %s₽\n%s", dishName, dishPrice, dishComment);
    }
}
