package tech.erubin.annyeong_eat.telegramBot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.handler.CallbackQueryHandler;
import tech.erubin.annyeong_eat.telegramBot.handler.MessageHandler;

@Getter
@Setter
@Configuration
@ComponentScan("tech.erubin.annyeong_eat.telegramBot")
@PropertySource("classpath:application.properties")
public class AnnyeongEatWebHookConfig {

    @Value("${telegrambot.botUsername}")
    private String botUsername;

    @Value("${telegrambot.botToken}")
    private String botToken;

    @Value("${telegrambot.botPath}")
    private String botPath;

    private MessageHandler messageHandler;
    private CallbackQueryHandler callbackQueryHandler;

    public AnnyeongEatWebHookConfig(MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler) {
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Bean
    public AnnyeongEatWebHook getAnnyeongEatWebHook() {
        return new AnnyeongEatWebHook(botUsername, botToken, botPath, messageHandler, callbackQueryHandler);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
