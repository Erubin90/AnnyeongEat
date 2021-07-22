package tech.erubin.annyeong_eat.telegramBot.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.module.mainMenu.MainMenuModule;
import tech.erubin.annyeong_eat.telegramBot.module.order.OrderModule;
import tech.erubin.annyeong_eat.telegramBot.module.registration.RegistrationModule;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.EmployeeServiceImpl;

@Component
@AllArgsConstructor
public class MessageHandler {
    private final EmployeeServiceImpl employeeService;
    private final ClientServiceImpl clientService;
    private final RegistrationModule registrationModule;
    private final MainMenuModule mainMenuModule;
    private final OrderModule orderModule;

    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> botApiMethod = null;

        if (update.getMessage() != null && update.getMessage().hasText()) {
            String userId = update.getMessage().getFrom().getId().toString();
//            Employee employee = employeeService.getEmployeeByTelegramUserId(userId);
            Client client = clientService.getClientByTelegramUserId(userId);
//            if (employee != null) {
//                return employeeAction(update, client, employee);
//            }
//            else {
                if (client == null) {
                    client = clientService.createClient(userId);
                }
//            }
            botApiMethod = clientActions(update, client);
        }
        return botApiMethod;
    }

    private BotApiMethod<?> clientActions(Update update, Client client) {
        String clientStatus = client.getStatus();
        switch (clientStatus){
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

//    private BotApiMethod<?> employeeAction(Update update, Client client, Employee employee) {
//        String employeeRole = employee.getRole();
//        switch (employeeRole) {
//            case "администратор":
//                return administratorStatus(update, client, employee);
//            case "повар":
//                break;
//            case "доставщик":
//                break;
//            case "офицант":
//                break;
//        }
//        return new SendMessage(update.getMessage().getChatId().toString(), "Ошибка employeeAction()");
//    }

//    private BotApiMethod<?> administratorStatus(Update update, Client client, Employee employee) {
//        String employeeStatus = employee.getStatus();
//        switch (employeeStatus) {
//            case "выходной":
//                return registrationModule.startClientOrEmployee(update, client, employee);
//            case "главное меню":
//                break;
//            case "1":
//                break;
//            case "2":
//                break;
//            case "3":
//                break;
//        }
//        return new SendMessage(update.getMessage().getChatId().toString(), "Ошибка administratorStatus()");
//    }
}
