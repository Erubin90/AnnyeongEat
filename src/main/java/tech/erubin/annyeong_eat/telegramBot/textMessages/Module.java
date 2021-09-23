package tech.erubin.annyeong_eat.telegramBot.textMessages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.OrderServiceImpl;
import tech.erubin.annyeong_eat.service.OrderStatesServiceImpl;
import tech.erubin.annyeong_eat.service.UserServiceImpl;
import tech.erubin.annyeong_eat.service.UserStatesServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.enums.OrderEnum;

import java.util.List;

@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public abstract class Module{

    @Value("${employee.message.error}")
    protected String error;

    @Value("${message.error.putButton}")
    protected String putButton;

    @Value("${employee.message.nextMainMenu}")
    protected String nextMainMenu;

    @Value("${employee.message.choiceDepartment}")
    protected String choiceDepartment;

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

    //Error Messages
    @Value("${message.error.blank}")
    protected String errorBlank;

    @Value("${message.error.obsceneWord}")
    protected String errorObsceneWord;

    @Value("${message.error.falseTextForm}")
    protected String errorFalseTextForm;

    @Value("${message.error.number}")
    protected String errorNumber;

    @Value("${message.error.noCorrectChar}")
    protected String errorNoCorrectChar;

    @Value("${message.error.bigLength}")
    protected String errorBigLength;

    @Value("${message.error.littleLength}")
    protected String errorLittleLength;

    @Value("${message.error.formatPhoneNumber}")
    protected String errorFormatPhoneNumber;

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

    public String clientProfile(User user) {
        int countOrder = 0;
        for (Order order : user.getOrderList()) {
            List<OrderState> orderStateList = order.getOrderStateList();
            if (orderStateList.size() > 1) {
                String state = orderStateList.get(orderStateList.size() - 1).getState();
                if (OrderEnum.GET.isOrderAccepted(state)) {
                    countOrder++;
                }
            }
        }
        return "Имя - " + user.getName() + "\n" +
                "Фамилия - " + user.getSurname() + "\n" +
                "Номер - " + user.getPhoneNumber() + "\n" +
                "Город - " + user.getCity() + "\n" +
                "Количество заказов - " + countOrder;
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

    protected SendMessage sendMessage(SendMessage sendMessage, UserState userState,
                                    Order order, OrderState orderState, String text) {
        sendMessage.setText(text);
        userStatesService.save(userState);
        orderService.save(order);
        orderStatesService.save(orderState);
        return sendMessage;
    }

    protected SendMessage sendMessage(SendMessage sendMessage, UserState employeeState, String text) {
        sendMessage.setText(text);
        userStatesService.save(employeeState);
        return sendMessage;
    }

    protected SendMessage sendMessage(SendMessage sendMessage, User user, UserState userState, String text) {
        sendMessage.setText(text);
        userStatesService.save(userState);
        userService.save(user);
        return sendMessage;
    }
}
