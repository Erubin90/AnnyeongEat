package tech.erubin.annyeong_eat.telegramBot.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import tech.erubin.annyeong_eat.telegramBot.SettingMessage;
import tech.erubin.annyeong_eat.telegramBot.service.botService.RegistrationService;
import tech.erubin.annyeong_eat.telegramBot.service.botService.UpdateService;
import tech.erubin.annyeong_eat.telegramBot.service.entityService.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityService.EmployeeServiceImpl;

@Getter
@Configuration
@ComponentScan("tech.erubin.annyeong_eat.telegramBot")
@PropertySource("classpath:message.properties")
public class ConfigBotService {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private SettingMessage settingMessage;

    @Bean
    public RegistrationService getRegistrationService() {
        RegistrationService registrationService = new RegistrationService();
        registrationService.setClientService(clientService);
        registrationService.setMessageNoError(settingMessage.getNoErrorMessage());
        registrationService.setMessageError(settingMessage.getErrorMessage());
        registrationService.setRegularError(settingMessage.getRegularError());
        registrationService.setFlags(settingMessage.getFlags());
        return registrationService;
    }

    @Bean
    public UpdateService getUpdateService() {
        return new UpdateService(employeeService, clientService, getRegistrationService());
    }
}
