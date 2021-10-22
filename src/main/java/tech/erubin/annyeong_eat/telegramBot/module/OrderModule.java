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
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.DepartmentEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;
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
                employeeStateService.createAndSave(user, EmployeeEnum.OPERATOR_CHOOSING_TABLE.getValue());
            }
            else {
                text = hello + " " + sourceText;
                replyKeyboard = replyButtons.userOrderMenu(order);
                userStatesService.createAndSave(user, ClientEnum.ORDER_CAFE_MENU.getValue());
            }
            orderService.save(order);
            orderStatesService.createAndSave(order, OrderEnum.ORDER_START_REGISTRATION.getValue());
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToMainMenu;
            if (isEmployee) {
                replyKeyboard = replyButtons.operatorMainMenu();
                employeeStateService.createAndSave(user, EmployeeEnum.OPERATOR_MAIN_MENU.getValue());
            }
            else {
                replyKeyboard = replyButtons.userMainMenu();
                userStatesService.createAndSave(user, ClientEnum.MAIN_MENU.getValue());
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
                employeeStateService.createAndSave(user, EmployeeEnum.OPERATOR_CHOOSING_TABLE.getValue());
            }
            else {
                text = backToChoosingCafe;
                List<String> buttonName = cafeService.getCafeNameByCity(user.getCity());
                replyKeyboard = replyButtons.userOrderCafe(buttonName);
                userStatesService.createAndSave(user, ClientEnum.ORDER_CAFE.getValue());
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
                    employeeStateService.createAndSave(user, EmployeeEnum.OPERATOR_PAYMENT_METHOD.getValue());
                }
                else {
                    text = nextToObtaining;
                    replyKeyboard = replyButtons.userOrderObtaining();
                    userStatesService.createAndSave(user, ClientEnum.ORDER_METHOD_OF_OBTAINING.getValue());
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
            userStatesService.createAndSave(user, ClientEnum.DELIVERY_ADDRESS.getValue());
        }
        else if (sourceText.equals(replyButtons.getPickup())) {
            text = nextToPhoneNumber;
            replyKeyboard = replyButtons.userOrderPhoneNumber(user);
            order.setPriceDelivery(0);
            order.setObtainingMethod(replyButtons.getPickup());
            orderService.save(order);
            userStatesService.createAndSave(user, ClientEnum.DELIVERY_PHONE_NUMBER.getValue());
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToOrderMenu;
            replyKeyboard = replyButtons.userOrderMenu(order);
            userStatesService.createAndSave(user, ClientEnum.ORDER_CAFE_MENU.getValue());
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
            userStatesService.createAndSave(user, ClientEnum.ORDER_METHOD_OF_OBTAINING.getValue());
        }
        else {
            text = checkMessage.checkAddress(sourceText);
            if (!text.contains(errorTrigger)) {
                order.setAddress(sourceText);
                text = nextToPhoneNumber;
                replyKeyboard = replyButtons.userOrderPhoneNumber(user);
                orderService.save(order);
                userStatesService.createAndSave(user, ClientEnum.DELIVERY_PHONE_NUMBER.getValue());
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
                userStatesService.createAndSave(user, ClientEnum.ORDER_METHOD_OF_OBTAINING.getValue());
            }
            else {
                text = backToAddress;
                replyKeyboard = replyButtons.userOrderAddress(user);
                userStatesService.createAndSave(user, ClientEnum.DELIVERY_ADDRESS.getValue());
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
                userStatesService.createAndSave(user, ClientEnum.DELIVERY_PAYMENT_METHOD.getValue());
            }
        }
        return message(update, replyKeyboard, text);
    }

    public SendMessage deliveryPaymentMethod(Update update, User user, String sourceText, boolean isEmployee) {
        Order order = orderService.getOrderByUser(user);
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getCards())) {
            text = getChequeText(order, isEmployee);
            order.setPaymentMethod(sourceText);
            replyKeyboard = replyButtons.userOrderConfirmation();
            orderService.save(order);
            if (isEmployee) {
                employeeStateService.createAndSave(user, EmployeeEnum.OPERATOR_CONFIRMATION.getValue());
            }
            else {
                userStatesService.createAndSave(user, ClientEnum.DELIVERY_CONFIRMATION.getValue());
            }
        }
        else if (sourceText.equals(replyButtons.getCash())) {
            text = getChequeText(order, isEmployee);
            order.setPaymentMethod(sourceText);
            replyKeyboard = replyButtons.userOrderConfirmation();
            orderService.save(order);
            if (isEmployee) {
                employeeStateService.createAndSave(user, EmployeeEnum.OPERATOR_CONFIRMATION.getValue());
            }
            else {
                userStatesService.createAndSave(user, ClientEnum.DELIVERY_CONFIRMATION.getValue());
            }
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToPhoneNumber;
            replyKeyboard = replyButtons.userOrderPhoneNumber(user);
            if (isEmployee) {
                employeeStateService.createAndSave(user, EmployeeEnum.OPERATOR_CAFE_MENU.getValue());
            }
            else {
                userStatesService.createAndSave(user, ClientEnum.DELIVERY_PHONE_NUMBER.getValue());
            }
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderPayment();
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
                employeeStateService.createAndSave(user, EmployeeEnum.OPERATOR_MAIN_MENU.getValue());
            }
            else {
                text = returnMainMenu;
                replyKeyboard = replyButtons.userMainMenu();
                userStatesService.createAndSave(user, ClientEnum.MAIN_MENU.getValue());
            }
            sendMessageDepartment(order, DepartmentEnum.OPERATOR);
            orderService.save(order);
            orderStatesService.createAndSave(order, OrderEnum.ORDER_END_REGISTRATION.getValue());
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToPaymentMethod;
            replyKeyboard = replyButtons.userOrderPayment();
            userStatesService.createAndSave(user, ClientEnum.DELIVERY_PAYMENT_METHOD.getValue());
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderConfirmation();
        }
        return message(update, replyKeyboard, text);
    }

    public BotApiMethod<?> callbackOrderCafeMenu(CallbackQuery callback, Order order, Dish dish, String tag) {
            InlineKeyboardMarkup inlineMarkup;
            String callbackText = error;

            ChequeDish chequeDish = chequeDishService.getChequeByOrderAndDish(order, dish);
            int count = chequeDish.getCountDishes();
            if (tag.equals("m+") || tag.equals("b+")) {
                count++;
                callbackText = dish.getName() + " " + addDish;
            }
            else if (tag.equals("m-") || tag.equals("b-")) {
                if (count >= 0) {
                    if (count > 0) {
                        count--;
                        callbackText = dish.getName() + " " + subDish;
                    }
                    else {
                        callbackText = dish.getName() + " " + emptyDish;
                    }
                }
            }
            else if (tag.equals("mx") || tag.equals("bx")) {
                callbackText = count + " " + dish.getName() + " " + subDish;
                count = 0;
            }
            chequeDishService.saveOrDeleteChequeDish(chequeDish, count);
            if (tag.equals(String.valueOf(dish.getId()))) {
                callbackText = getDishText(dish);
                inlineMarkup = inlineButtons.clientCheque(chequeDish);
                if (!webHook.sendPhoto(callback, callbackText, dish.getLinkPhoto(), inlineMarkup)) {
                    callbackText = error;
                }
            }
            else if (tag.equals("m+") || tag.equals("mx") || tag.equals("m-")) {
                inlineMarkup = inlineButtons.clientCheque(chequeDish);
                EditMessageReplyMarkup editMessageReplyMarkup = editMessageReplyMarkup(callback, inlineMarkup);
                if (!webHook.updateMarkups(editMessageReplyMarkup, count)) {
                    callbackText = error;
                }
            }
            else if (tag.equals("b+") || tag.equals("bx") || tag.equals("b-")) {
                inlineMarkup = inlineButtons.fullOrderButtons(order);
                String editText = getChequeText(order, false);
                EditMessageText editMessageText = editMessageText(callback, editText, inlineMarkup);
                if (!webHook.updateText(editMessageText)) {
                    callbackText = error;
                }
            }
            return answerCallbackQuery(callback, callbackText);
        }

    private String getDishText(Dish dish) {
        String dishName = dish.getName();
        double dishPrice = dish.getPrice();
        String dishComment = dish.getComment();
        return String.format("%s %s₽\n%s", dishName, dishPrice, dishComment);
    }
}
