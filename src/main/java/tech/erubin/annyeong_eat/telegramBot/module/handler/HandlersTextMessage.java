package tech.erubin.annyeong_eat.telegramBot.module.handler;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.entity.Dish;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class HandlersTextMessage {

    @Value("${handler.message.error}")
    private String error;

    @Value("${handler.message.addDish}")
    private String addDish;

    @Value("${handler.message.subDish}")
    private String subDish;

    @Value("${handler.message.emptyDish}")
    private String emptyDish;

    @Value("${handler.message.notWork}")
    private String notWork;

    public String getTextDish(Dish dish) {
        String dishName = dish.getName();
        double dishPrice = dish.getPrice();
        String dishComment = dish.getComment();
        return String.format("%s %s₽\n%s", dishName, dishPrice, dishComment);
    }
}
