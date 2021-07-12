package tech.erubin.annyeong_eat.telegramBot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.module.TelegramFacade;

@Getter
@Setter
@Configuration
@ComponentScan("tech.erubin.annyeong_eat.telegramBot")
@PropertySource("classpath:application.properties")
public class ConfigAnnyeongEatWebHook {

    @Value("${telegrambot.botUsername}")
    private String botUsername;

    @Value("${telegrambot.botToken}")
    private String botToken;

    @Value("${telegrambot.botPath}")
    private String botPath;

    @Autowired
    private TelegramFacade telegramFacade;

    @Bean
    public AnnyeongEatWebHook getAnnyeongEatWebHook() {
        return new AnnyeongEatWebHook(botUsername, botToken, botPath, telegramFacade);
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
