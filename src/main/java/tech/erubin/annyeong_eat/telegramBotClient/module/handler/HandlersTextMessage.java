package tech.erubin.annyeong_eat.telegramBotClient.module.handler;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class HandlersTextMessage {

    @Value("${handler.message.client.error}")
    private String error;

    @Value("${handler.message.client.addDish}")
    private String addDish;

    @Value("${handler.message.client.subDish}")
    private String subDish;

    @Value("${handler.message.client.emptyDish}")
    private String emptyDish;
}
