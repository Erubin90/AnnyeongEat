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
import tech.erubin.annyeong_eat.service.DepartmentServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.buttons.InlineButtons;
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
    private DepartmentServiceImpl departmentService;
    private InlineButtons inlineButtons;

    public AnnyeongEatWebHookConfig(MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler,
                                    DepartmentServiceImpl departmentService, InlineButtons inlineButtons) {
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.departmentService = departmentService;
        this.inlineButtons = inlineButtons;
    }

    @Bean
    public AnnyeongEatWebHook getAnnyeongEatWebHook() {
        return new AnnyeongEatWebHook(botUsername, botToken, botPath, messageHandler, callbackQueryHandler,
                departmentService, inlineButtons);
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
