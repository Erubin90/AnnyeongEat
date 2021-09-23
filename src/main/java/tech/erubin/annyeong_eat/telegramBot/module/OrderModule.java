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
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;
import tech.erubin.annyeong_eat.telegramBot.enums.ClientEnum;
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

    public SendMessage choosingCafe(Update update, User user, String sourceText){
        List<String> cafeNames = cafeService.getCafeNameByCity(user.getCity());
        Order order = orderService.getOrderById(user);
        UserState userState = null;
        OrderState orderState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (cafeNames.contains(sourceText)){
            text = hello + " " + sourceText;
            Cafe cafe = cafeService.getCafeByName(sourceText);
            order = orderService.getOrderById(user, cafe);
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
        Order order = orderService.getOrderById(user);
        List<String> typeDishes = replyButtons.typeDishesInCafe(order);
        UserState userState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (typeDishes.contains(sourceText)) {
            text = sourceText + ":";
            List<Dish> dishList = dishService.getDishListByType(sourceText);
            replyKeyboard = inlineButtons.typeDishesMenu(dishList);
        }
        else if (sourceText.equals(replyButtons.getBack())) {
            text = backToChoosingCafe;
            order.setUsing(1);
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
            text = getFullOrder(order);
            replyKeyboard = inlineButtons.getFullOrderButtons(order);
        }
        else {
            text = putButton;
            replyKeyboard = replyButtons.userOrderMenu(order);
        }
        return sendMessage(update, replyKeyboard, text, userState, order);
    }

    public SendMessage deliveryAddress(Update update, User user, String sourceText){
        Order order = orderService.getOrderById(user);
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
                text = nextToPhoneNumber;
                userState = userStatesService.create(user, ClientEnum.DELIVERY_PHONE_NUMBER.getValue());
                order.setAddress(sourceText);
                replyKeyboard = replyButtons.userOrderPhoneNumber(user);
            }
            else {
                replyKeyboard = replyButtons.userOrderAddress(user);
            }
        }
        return sendMessage(update, replyKeyboard, text, userState, order);
    }

    public SendMessage deliveryPhoneNumber(Update update, User user, String sourceText){
        Order order = orderService.getOrderById(user);
        UserState userState = null;
        OrderState orderState = null;
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
        return sendMessage(update, replyKeyboard, text, userState, order, orderState);
    }

    public SendMessage deliveryPaymentMethod(Update update, User user, String sourceText){
        Order order = orderService.getOrderById(user);
        List<String> paymentMethod = replyButtons.paymentMethod();
        UserState userState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (paymentMethod.contains(sourceText)) {
            text = getFullOrder(order);
            order.setPaymentMethod(sourceText);
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
        Order order = orderService.getOrderById(user);
        UserState userState = null;
        OrderState orderState = null;
        String text;
        ReplyKeyboard replyKeyboard;
        if (sourceText.equals(replyButtons.getConfirm())) {
            text = returnMainMenu;
            order.setUsing(0);
            orderState = orderStatesService.create(order, OrderEnum.ORDER_END_REGISTRATION.getValue());
            userState = userStatesService.create(user, ClientEnum.MAIN_MENU.getValue());
            replyKeyboard = replyButtons.userMainMenu();
            if (user.getDepartmentsList() != null) {
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

    public String getFullOrder(Order order) {
        StringBuilder fullOrder = new StringBuilder(order.getOrderName() + ":\n\n");
        List<ChequeDish> chequeDishList = order.getChequeDishList();
        if (chequeDishList != null && !chequeDishList.isEmpty()) {
            double sum = 0.0;
            int count = 1;
            for (ChequeDish chequeDish : chequeDishList) {
                sum += chequeDish.getDishId().getPrice() * chequeDish.getCountDishes();
                Dish dish = chequeDish.getDishId();
                fullOrder.append(count++)
                        .append(". ")
                        .append(dish.getName())
                        .append("\n")
                        .append(chequeDish.getCountDishes())
                        .append(" x ")
                        .append(dish.getPrice())
                        .append(" = ")
                        .append(chequeDish.getCountDishes() * dish.getPrice())
                        .append("₽\n")
                        .append("\n");
                if (chequeDish.getChequeDishOptionallyList() != null) {
                    List<ChequeDishOptionally> dishOptionallyList = chequeDish.getChequeDishOptionallyList();
                    for (ChequeDishOptionally chequeDishOptionally : dishOptionallyList) {
                        sum += chequeDishOptionally.getDishOptionallyId().getPrice() * chequeDishOptionally.getCount();
                        DishOptionally dishOpt = chequeDishOptionally.getDishOptionallyId();
                        fullOrder.append(dishOpt.getName())
                                .append("\n")
                                .append(chequeDishOptionally.getCount())
                                .append(" x ")
                                .append(dishOpt.getPrice())
                                .append(" = ")
                                .append(chequeDishOptionally.getCount() * dishOpt.getPrice())
                                .append("₽\n\n");
                    }
                }
            }
            fullOrder.append("Сумма: ")
                    .append(sum)
                    .append("₽\n");
            if (order.getPhoneNumber() != null) {
                fullOrder.append("Номер телефона: ")
                        .append(order.getPhoneNumber())
                        .append("\n");
            }
            if (order.getAddress() != null) {
                fullOrder.append("Адрес доставки: ")
                        .append(order.getAddress())
                        .append("\n");
            }
            if (order.getPaymentMethod() != null) {
                fullOrder.append("Способ оплаты: ")
                        .append(order.getPaymentMethod())
                        .append("\n");
            }
            List<OrderState> orderStateList = order.getOrderStateList();
            if (orderStateList.size() > 2) {
                fullOrder.append("Статус заказа: ")
                        .append(orderStateList.get(orderStateList.size() - 1).getState())
                        .append("\n");
            }
            return fullOrder.toString();
        }
        else {
            return emptyReceipt;
        }
    }

    private void sendMessageOperator(Order order) {
        List<Department> listOperatorsInCafe = departmentService
                .getEmployeeByCafeIdAndDepartmenName(order.getCafeId(), EmployeeEnum.OPERATOR.getValue());
        if (listOperatorsInCafe != null) {
            webHook.sendMessageDepartment(listOperatorsInCafe, EmployeeEnum.OPERATOR, getFullOrder(order), order);
        }
    }
}
