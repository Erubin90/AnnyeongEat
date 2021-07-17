package tech.erubin.annyeong_eat.telegramBot.module.order;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBot.entity.Cheque;
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
    private DishOptionallyServiceImpl dishOptionallyService;
    private DishServiceImpl dishService;
    private OrderServiceImpl orderService;

    @Value("${regular.errorTrigger}")
    private String errorTrigger = "error";

    @Value("${address.noError}")
    private String AddressNoError = "ok";

    @Value("${order.message.client.emptyReceipt}")
    private String emptyReceipt;

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
            String str = String.format("%s %s ₽ %s\n", dishName.get(i), dishPrice.get(i), dishTag.get(i));
            text.append(str);
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
        List<Cheque> chequeList = order.getChequeList();
        if (chequeList != null) {
            for (Cheque cheque : chequeList) {
                Dish dish = cheque.getDishId();
                fullOrder.append(dish.getName())
                        .append(" ")
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
