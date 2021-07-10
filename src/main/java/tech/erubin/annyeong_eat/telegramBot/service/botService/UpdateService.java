package tech.erubin.annyeong_eat.telegramBot.service.botService;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Employee;
import tech.erubin.annyeong_eat.telegramBot.service.entityService.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityService.EmployeeServiceImpl;

import java.util.List;

@Service
@NoArgsConstructor
public class UpdateService {
    @Autowired
    private EmployeeServiceImpl employeeService;
    @Autowired
    private ClientServiceImpl clientService;
    @Autowired
    private RegistrationService registrationService;

    @Value("#{'${typeState}'.split(', ')}")
    private List<String> typeStateList;

    public UpdateService(EmployeeServiceImpl employeeService, ClientServiceImpl clientService,
                         RegistrationService registrationService) {
        this.employeeService = employeeService;
        this.clientService = clientService;
        this.registrationService = registrationService;
    }

    public BotApiMethod<Message> handleUpdate(Update update) {
        String userId = update.getMessage().getFrom().getId().toString();
        SendMessage sendMessage = new SendMessage();

        Employee employee = employeeService.getEmployeeByTelegramUserId(userId);
        Client client = clientService.getClientByTelegramUserId(userId);
        if (client == null){
            client = clientService.createClient(userId);
        }

        String stateClientType = getStateTypeClient(client);
        switch (stateClientType){
            case "регистр":
            case "null":
                System.out.println("регистр - null");
                sendMessage = registrationService.startClient(update, client);
                break;
            case "заказ":
                break;
        }
        return sendMessage;
    }

    private String getStateTypeClient(Client client) {
        String stateClient = client.getState();
        for (String typeState: typeStateList) {
            if (stateClient.matches(typeState)) {
                System.out.println("getStateTypeClient - " + typeState);
                return typeState;
            }
        }
        return "null";
    }

}
