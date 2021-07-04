package tech.erubin.annyeong_eat.bot.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import tech.erubin.annyeong_eat.bot.AnnyeongEatWebHook;

@Getter
@Configuration
@ComponentScan("tech.erubin.annyeong_eat.bot")
@PropertySource("classpath:application.properties")
public class ConfigAnnyeongEatWebHook {

    @Value("${telegrambot.botUsername}")
    private String botUsername;

    @Value("${telegrambot.botToken}")
    private String botToken;

    @Value("${telegrambot.botPath}")
    private String botPath;

    @Bean
    public AnnyeongEatWebHook getAnnyeongEatWebHook() {
        return new AnnyeongEatWebHook(botUsername, botToken, botPath);
    }

}
