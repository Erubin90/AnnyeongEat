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

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class TextMessageOrderModule {
    DishOptionallyServiceImpl dishOptionallyService;
    DishServiceImpl dishService;

    @Value("${regular.errorTrigger}")
    private String errorTrigger;

    public TextMessageOrderModule(DishOptionallyServiceImpl dishOptionallyService, DishServiceImpl dishService) {
        this.dishOptionallyService = dishOptionallyService;
        this.dishService = dishService;
    }

    public String getDishesByTypeInTargetMenu(String type) {
        List<Dish> dishByType = dishService.getDishByType(type);
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

    public String getTextDishesByTag(String tag) {
        Dish dishByTag = dishService.getDishByTag(tag);
        String dishName = dishByTag.getName();
        int dishPrice = (int) dishByTag.getPrice();
        String dishComment = dishByTag.getComment();
        return String.format("%s %s\n%s\n", dishName, dishPrice, dishComment);
    }

    public String getFullOrder(Order order) {
        StringBuilder fullOrder = new StringBuilder(order.getOrderName() + "\n\n");
        List<Cheque> chequeList = order.getChequeList();
        if (!chequeList.isEmpty()) {
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

            return "Вы ничего не выбрали из блюд";
        }
    }
}
