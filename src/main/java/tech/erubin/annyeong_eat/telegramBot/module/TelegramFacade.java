package tech.erubin.annyeong_eat.telegramBot.module;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Employee;
import tech.erubin.annyeong_eat.telegramBot.module.mainMenu.MainMenuModule;
import tech.erubin.annyeong_eat.telegramBot.module.order.OrderModule;
import tech.erubin.annyeong_eat.telegramBot.module.registration.RegistrationModule;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.EmployeeServiceImpl;

@Component
@AllArgsConstructor
public class TelegramFacade {
    private EmployeeServiceImpl employeeService;
    private ClientServiceImpl clientService;
    private RegistrationModule registrationModule;
    private MainMenuModule mainMenuModule;
    private OrderModule orderModule;

    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> botApiMethod = null;

        if (update.getMessage() != null && update.getMessage().hasText()) {
            String userId = update.getMessage().getFrom().getId().toString();
            Employee employee = employeeService.getEmployeeByTelegramUserId(userId);
            Client client = clientService.getClientByTelegramUserId(userId);
            if (client == null) {
                client = clientService.createClient(userId);
            }
            botApiMethod = clientActions(update, client);
        }
        return botApiMethod;
    }

    private BotApiMethod<?> clientActions(Update update, Client client) {
        String statusClient = client.getStatus();
        switch (statusClient){
            case "регистрация":
                return registrationModule.startClient(update, client);
            case "главное меню":
                return mainMenuModule.startClient(update, client);
            case "оформление заказа":
                return orderModule.startClient(update, client);
            default:
                return new SendMessage(update.getMessage().getChatId().toString(), "Ошибка clientActions()");
        }
    }
}
