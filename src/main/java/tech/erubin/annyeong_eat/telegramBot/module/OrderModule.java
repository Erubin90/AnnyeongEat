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
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;
import tech.erubin.annyeong_eat.telegramBot.handler.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.abstractClass.AbstractModule;

import java.util.List;


@Component
public class OrderModule extends AbstractModule {
    private final CafeServiceImpl cafeService;
    private final DishServiceImpl dishService;
    private final ChequeDishServiceImpl chequeDishService;

    private final ReplyButtons replyButtons;
    private final InlineButtons inlineButtons;

    private final CheckMessage checkMessage;

    public OrderModule(OrderServiceImpl orderService, UserServiceImpl userService,
                       UserStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                       EmployeeServiceImpl employeeService, CafeServiceImpl cafeService,
                       DishServiceImpl dishService, ReplyButtons replyButtons, InlineButtons inlineButtons,
                       CheckMessage checkMessage, @Lazy AnnyeongEatWebHook webHook, ChequeDishServiceImpl chequeDishService) {
        super(orderService, userService, userStatesService, orderStatesService, employeeService, webHook);
        this.cafeService = cafeService;
        this.dishService = dishService;
        this.replyButtons = replyButtons;
        this.inlineButtons = inlineButtons;
        this.checkMessage = checkMessage;
        this.chequeDishService = chequeDishService;
    }

    public SendMessage choosingCafe(Update update, User user, String sourceText) {
        List<String> cafeNames = cafeService.getCafeNameByCity(user.getCity());
        Order order = orderService.getOrderByUser(user);
        UserState userState = null;
        OrderState orderState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (cafeNames.contains(sourceText)) {
            text = hello + " " + sourceText;
            Cafe cafe = cafeService.getCafeByName(sourceText);
            order = orderService.getOrderByUserIdAndCafeId(user, cafe);
            order.setUsing(1);
            orderState = orderStatesService.create(order, OrderEnum.ORDER_START_REGISTRATION.getValue());
            userState = userStatesService.create(user, ClientEnum.ORDER_CAFE_MENU.getValue());
            replyKeyboard = replyButtons.userOrderMenu(order);
        } else if (sourceText.equals(replyButtons.getBack())) {
            text = backToMainMenu;
            userState = userStatesService.create(user, ClientEnum.MAIN_MENU.getValue());
            replyKeyboard = replyButtons.userMainMenu();
        } else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderCafe(user);
        }
        return message(update, replyKeyboard, text, userState, order, orderState);
    }

    public SendMessage cafeMenu(Update update, User user, String sourceText) {
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
                text = nextToObtaining;
                userState = userStatesService.create(user, ClientEnum.ORDER_METHOD_OF_OBTAINING.getValue());
                replyKeyboard = replyButtons.userOrderObtaining();
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
        return message(update, replyKeyboard, text, userState, order);
    }

    public SendMessage methodObtaining(Update update, User user, String sourceText) {
        Order order = orderService.getOrderByUser(user);
        UserState userState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getDelivery())) {
            text = nextToAddress;
            order.setObtainingMethod(replyButtons.getDelivery());
            userState = userStatesService.create(user, ClientEnum.DELIVERY_ADDRESS.getValue());
            replyKeyboard = replyButtons.userOrderAddress(user);
        }
        else if (sourceText.equals(replyButtons.getPickup())) {
            text = nextToPhoneNumber;
            order.setObtainingMethod(replyButtons.getPickup());
            userState = userStatesService.create(user, ClientEnum.DELIVERY_PHONE_NUMBER.getValue());
            replyKeyboard = replyButtons.userOrderPhoneNumber(user);
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToOrderMenu;
            userState = userStatesService.create(user, ClientEnum.ORDER_CAFE_MENU.getValue());
            replyKeyboard = replyButtons.userOrderMenu(order);
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderObtaining();
        }
        return message(update, replyKeyboard, text, userState, order);
    }

    public SendMessage deliveryAddress(Update update, User user, String sourceText) {
        Order order = orderService.getOrderByUser(user);
        UserState userState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getBack())) {
            text = backToObtaining;
            userState = userStatesService.create(user, ClientEnum.ORDER_CAFE_MENU.getValue());
            replyKeyboard = replyButtons.userOrderMenu(order);
        } else {
            text = checkMessage.checkAddress(sourceText);
            if (!text.contains(errorTrigger)) {
                order.setAddress(sourceText);
                text = nextToPhoneNumber;
                userState = userStatesService.create(user, ClientEnum.DELIVERY_PHONE_NUMBER.getValue());
                replyKeyboard = replyButtons.userOrderPhoneNumber(user);
            } else {
                replyKeyboard = replyButtons.userOrderAddress(user);
            }
        }
        return message(update, replyKeyboard, text, userState, order);
    }

    public SendMessage deliveryPhoneNumber(Update update, User user, String sourceText) {
        Order order = orderService.getOrderByUser(user);
        UserState userState = null;
        String text;
        ReplyKeyboard replyKeyboard = replyButtons.userOrderPhoneNumber(user);
        if (sourceText.equals(replyButtons.getBack())) {
            if (order.getObtainingMethod().equals(replyButtons.getPickup())) {
                text = backToObtaining;
                userState = userStatesService.create(user, ClientEnum.ORDER_METHOD_OF_OBTAINING.getValue());
            }
            else {
                text = backToAddress;
                userState = userStatesService.create(user, ClientEnum.DELIVERY_ADDRESS.getValue());
            }
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
        return message(update, replyKeyboard, text, userState, order);
    }

    public SendMessage deliveryPaymentMethod(Update update, User user, String sourceText) {
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
        } else if (sourceText.equals(replyButtons.getBack())) {
            text = backToPhoneNumber;
            userState = userStatesService.create(user, ClientEnum.DELIVERY_PHONE_NUMBER.getValue());
            replyKeyboard = replyButtons.userOrderPhoneNumber(user);
        } else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderPayment();
        }

        return message(update, replyKeyboard, text, userState, order);
    }

    public SendMessage deliveryConfirmation(Update update, User user, String sourceText) {
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
            sendMessageDepartment(order, DepartmentEnum.OPERATOR);
        } else if (sourceText.equals(replyButtons.getBack())) {
            text = backToPaymentMethod;
            userState = userStatesService.create(user, ClientEnum.DELIVERY_PAYMENT_METHOD.getValue());
            replyKeyboard = replyButtons.userOrderPayment();
        } else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderConfirmation();
        }
        return message(update, replyKeyboard, text, userState, order, orderState);
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
