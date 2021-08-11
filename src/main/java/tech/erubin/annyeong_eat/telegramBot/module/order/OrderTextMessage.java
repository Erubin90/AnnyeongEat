package tech.erubin.annyeong_eat.telegramBot.module.order;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBot.entity.ChequeDish;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.DishOptionallyServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.DishServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.OrderServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class OrderTextMessage {
    private final DishOptionallyServiceImpl dishOptionallyService;
    private final DishServiceImpl dishService;
    private final OrderServiceImpl orderService;

    @Value("${regular.errorTrigger}")
    private String errorTrigger = "error";

    @Value("${address.noError}")
    private String AddressNoError = "ok";

    @Value("${order.message.client.emptyReceipt}")
    private String emptyReceipt;

    @Value("${message.notButton}")
    private String notButton;

    @Value("${order.message.client.hello}")
    private String hello;

    @Value("${order.message.client.backToMainMenu}")
    private String backToMainMenu;

    @Value("${order.message.client.backToChoosingCafe}")
    private String backToChoosingCafe;

    @Value("${order.message.client.backToOrderMenu}")
    private String backToOrderMenu;

    @Value("${order.message.client.backToAddress}")
    private String backToAddress;

    @Value("${order.message.client.backToPhoneNumber}")
    private String backToPhoneNumber;

    @Value("${order.message.client.backToPaymentMethod}")
    private String backToPaymentMethod;

    @Value("${order.message.client.nextToAddress}")
    private String nextToAddress;

    @Value("${order.message.client.nextToPhoneNumber}")
    private String nextToPhoneNumber;

    @Value("${order.message.client.nextToPaymentMethod}")
    private String nextToPaymentMethod;

    @Value("${mainMenu.message.client.returnMainMenu}")
    private String returnMainMenu;

    @Value("${order.message.client.server.ok}")
    private String serverOk;

    @Value("${order.message.client.server.error}")
    private String serverError;

    @Value("${order.message.client.error}")
    private String error;

    public OrderTextMessage(DishOptionallyServiceImpl dishOptionallyService, DishServiceImpl dishService,
                            OrderServiceImpl orderService) {
        this.dishOptionallyService = dishOptionallyService;
        this.dishService = dishService;
        this.orderService = orderService;
    }

    public String getDishesByTypeInTargetMenu(String type) {
        List<Dish> dishByType = dishService.getDishListByType(type);
        List<String> dishName = dishByType.stream()
                .map(Dish::getName)
                .collect(Collectors.toList());
        List<String> dishTag = dishByType.stream()
                .map(Dish::getTag)
                .collect(Collectors.toList());
        List<Integer> dishPrice = dishByType.stream()
                .map(Dish::getPrice)
                .map(Double::intValue)
                .collect(Collectors.toList());
        StringBuilder text = new StringBuilder(type + ":\n");
        for (int i = 0; i < dishName.size(); i++) {
            text.append(dishName.get(i));
            text.append(" ");
            text.append(dishPrice.get(i))
                    .append("₽\n")
                    .append(dishTag.get(i))
                    .append("\n");
        }
        return text.toString();
    }

    public String getTextDishesByTag(Dish dish) {
        String dishName = dish.getName();
        int dishPrice = (int) dish.getPrice();
        String dishComment = dish.getComment();
        return String.format("%s %s\n%s", dishName, dishPrice, dishComment);
    }

    public String getFullOrder(Order order) {
        StringBuilder fullOrder = new StringBuilder(order.getOrderName() + ":\n\n");
        List<ChequeDish> chequeDishList = order.getChequeDishList();
        if (chequeDishList != null && chequeDishList.size() > 0) {
            for (ChequeDish chequeDish : chequeDishList) {
                Dish dish = chequeDish.getDishId();
                fullOrder.append(dish.getName())
                        .append(" ")
                        .append(chequeDish.getCountDishes())
                        .append(" x ")
                        .append(dish.getPrice())
                        .append(" = ")
                        .append(chequeDish.getCountDishes() * dish.getPrice())
                        .append("₽\n")
                        .append(dish.getTag())
                        .append("\n");
            }
            return fullOrder.toString();
        }
        else {
            return emptyReceipt;
        }
    }
}
