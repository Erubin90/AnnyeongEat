package tech.erubin.annyeong_eat.telegramBot.module.order;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.entity.*;

import java.util.List;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class OrderTextMessage {

    @Value("${order.message.error}")
    private String error;

    @Value("${regular.errorTrigger}")
    private String errorTrigger;

    @Value("${address.noError}")
    private String AddressNoError;

    @Value("${order.message.emptyReceipt}")
    private String emptyReceipt;

    @Value("${message.error.notButton}")
    private String notButton;

    @Value("${order.message.hello}")
    private String hello;

    @Value("${order.message.backToMainMenu}")
    private String backToMainMenu;

    @Value("${order.message.backToChoosingCafe}")
    private String backToChoosingCafe;

    @Value("${order.message.backToOrderMenu}")
    private String backToOrderMenu;

    @Value("${order.message.backToAddress}")
    private String backToAddress;

    @Value("${order.message.backToPhoneNumber}")
    private String backToPhoneNumber;

    @Value("${order.message.backToPaymentMethod}")
    private String backToPaymentMethod;

    @Value("${order.message.nextToAddress}")
    private String nextToAddress;

    @Value("${order.message.nextToPhoneNumber}")
    private String nextToPhoneNumber;

    @Value("${order.message.nextToPaymentMethod}")
    private String nextToPaymentMethod;

    @Value("${mainMenu.message.returnMainMenu}")
    private String returnMainMenu;

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
            return fullOrder.toString();
        }
        else {
            return emptyReceipt;
        }
    }

}
