package tech.erubin.annyeong_eat.telegramBot.textMessages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;

import java.util.List;

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

    @Value("${operator.message.noCommand}")
    protected String noCommand;

    @Value("${operator.message.noOrderName}")
    protected String noOrderName;

    @Value("${operator.message.noEditingOrder}")
    protected String noEditingOrder;

    @Value("${order.message.noCorrectPriceDelivery}")
    protected String noCorrectPriceDelivery;

    //Introduction message
    @Value("${registration.message.start}")
    protected String startClientRegistration;

    @Value("${registration.message.end}")
    protected String endUserRegistration;

    @Value("${operator.message.priceNotSpecified}")
    protected String priceNotSpecified;

    @Value("${operator.message.priceNotCalculated}")
    protected String priceNotCalculated;

    protected OrderServiceImpl orderService;
    protected UserServiceImpl userService;
    protected UserStatesServiceImpl userStatesService;
    protected OrderStatesServiceImpl orderStatesService;
    protected DepartmentServiceImpl departmentService;


    public Module(OrderServiceImpl orderService, UserServiceImpl userService, UserStatesServiceImpl userStatesService,
                  OrderStatesServiceImpl orderStatesService, DepartmentServiceImpl departmentService) {
        this.orderService = orderService;
        this.userService = userService;
        this.userStatesService = userStatesService;
        this.orderStatesService = orderStatesService;
        this.departmentService = departmentService;
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

    public String getFullOrderText(Order order, boolean isEmployee) {
        List<ChequeDish> chequeDishList = order.getChequeDishList();
        if (chequeDishList != null && !chequeDishList.isEmpty()) {
            String receiptPositions = getReceiptPositions(order);
            double sumCheque = getSumCheque(order);
            int priceDelivery = order.getPriceDelivery();
            StringBuilder fullOrder = new StringBuilder(order.getOrderName() + ":\n\n");
            if (isEmployee) {
                fullOrder.append("Имя: ")
                        .append(order.getUserId().getName())
                        .append("\n")
                        .append("Адрес: ")
                        .append(order.getAddress())
                        .append("\n")
                        .append("Номер: ")
                        .append(order.getPhoneNumber())
                        .append("\n\n");
            }
            else {
                fullOrder.append(receiptPositions)
                        .append("\n");
            }
            fullOrder.append("Сумма заказа: ")
                        .append(sumCheque)
                        .append("₽\n")
                        .append("Сумма доставки: ");
            if (priceDelivery >= 0) {
                double sumOrder = sumCheque + priceDelivery;
                fullOrder.append(priceDelivery)
                        .append("₽\n")
                        .append("Итоговая сумма заказа: ")
                        .append(sumOrder)
                        .append("₽\n");
            }
            else {
                if (isEmployee) {
                    fullOrder.append(priceNotSpecified).append("\n")
                            .append("Итоговая сумма заказа: ")
                            .append(priceNotCalculated)
                            .append("\n");
                }
                else {
                    fullOrder.append(noCorrectPriceDelivery)
                            .append("\nПредворительная сумма заказа без доставки: ")
                            .append(sumCheque)
                            .append("₽\n");
                }
            }
            if (order.getPaymentMethod() != null) {
                fullOrder.append("Способ оплаты: ")
                        .append(order.getPaymentMethod())
                        .append("\n");
            }
            if (isEmployee) {
                fullOrder.append("Комментарий: ")
                        .append(order.getComment())
                        .append("\n\n")
                        .append("Товары:\n")
                        .append(receiptPositions);
            }
            return fullOrder.toString();
        }
        else {
            return emptyReceipt;
        }
    }

    private String getReceiptPositions(Order order) {
        int count = 1;
        List<ChequeDish> chequeDishList = order.getChequeDishList();
        StringBuilder receiptPositions = new StringBuilder();
        for (ChequeDish chequeDish : chequeDishList) {
            Dish dish = chequeDish.getDishId();
            receiptPositions.append(count++)
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
                    DishOptionally dishOpt = chequeDishOptionally.getDishOptionallyId();
                    receiptPositions.append(dishOpt.getName())
                            .append("\n")
                            .append(chequeDishOptionally.getCount())
                            .append(" x ")
                            .append(dishOpt.getPrice())
                            .append(" = ")
                            .append(chequeDishOptionally.getCount() * dishOpt.getPrice())
                            .append("₽");
                }
            }
        }
        return receiptPositions.toString();
    }

    private double getSumCheque(Order order) {
        double sumCheque = 0.0;
        for (ChequeDish chequeDish : order.getChequeDishList()) {
            sumCheque += chequeDish.getDishId().getPrice() * chequeDish.getCountDishes();
            if (chequeDish.getChequeDishOptionallyList() != null) {
                for (ChequeDishOptionally chequeDishOptionally : chequeDish.getChequeDishOptionallyList()) {
                    sumCheque += chequeDishOptionally.getDishOptionallyId().getPrice() * chequeDishOptionally.getCount();
                }
            }
        }
        return sumCheque;
    }

    protected void sendMessageOperator(Order order, AnnyeongEatWebHook webHook) {
        List<Employee> listOperatorsInCafe = departmentService
                .getEmployeeByCafeIdAndDepartmenName(order.getCafeId(), EmployeeEnum.OPERATOR.getValue());
        if (listOperatorsInCafe != null) {
            webHook.sendMessageDepartment(listOperatorsInCafe, EmployeeEnum.OPERATOR, getFullOrderText(order, true), order);
        }
    }
}
