package tech.erubin.annyeong_eat.telegramBot.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.ClientState;
import tech.erubin.annyeong_eat.telegramBot.states.ClientStateEnum;
import tech.erubin.annyeong_eat.telegramBot.module.mainMenu.MainMenuModule;
import tech.erubin.annyeong_eat.telegramBot.module.order.OrderModule;
import tech.erubin.annyeong_eat.telegramBot.module.registration.RegistrationModule;
import tech.erubin.annyeong_eat.telegramBot.service.entityServiсes.CafeServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServiсes.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServiсes.ClientStatesServiceImpl;

import java.util.List;

@Component
@AllArgsConstructor
public class MessageHandler {
    private final ClientServiceImpl clientService;
    private final RegistrationModule registrationModule;
    private final MainMenuModule mainMenuModule;
    private final OrderModule orderModule;
    private final ClientStatesServiceImpl stateService;
    private final CafeServiceImpl cafeService;

    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> botApiMethod = null;

        if (update.getMessage() != null && update.getMessage().hasText()) {
            String userId = update.getMessage().getFrom().getId().toString();
            Client client = clientService.getClientByTelegramUserId(userId);
            if (client == null) {
                client = clientService.createClient(userId);
            }
            botApiMethod = clientActions(update, client);
        }
        return botApiMethod;
    }

    private BotApiMethod<?> clientActions(Update update, Client client) {
        ClientState clientState = stateService.getState(client);
        ClientStateEnum clientStateEnum = getClientState(clientState);

        if (clientStateEnum == ClientStateEnum.REGISTRATION_START ||
                clientStateEnum == ClientStateEnum.REGISTRATION_CITY ||
                clientStateEnum == ClientStateEnum.REGISTRATION_NAME ||
                clientStateEnum == ClientStateEnum.REGISTRATION_SURNAME ||
                clientStateEnum == ClientStateEnum.REGISTRATION_PHONE_NUMBERS) {
            return registrationModule.startClient(update, client, clientStateEnum, clientState);
        }
        else if (clientStateEnum == ClientStateEnum.MAIN_MENU ||
                clientStateEnum == ClientStateEnum.ORDER_CHECK ||
                clientStateEnum == ClientStateEnum.HELP ||
                clientStateEnum == ClientStateEnum.PROFILE) {
            return mainMenuModule.startClient(update, client, clientStateEnum, clientState);
        }
        else if (clientStateEnum == ClientStateEnum.ORDER_CAFE ||
                clientStateEnum == ClientStateEnum.ORDER_CAFE_MENU ||
                clientStateEnum == ClientStateEnum.DELIVERY_ADDRESS ||
                clientStateEnum == ClientStateEnum.DELIVERY_PHONE_NUMBER ||
                clientStateEnum == ClientStateEnum.DELIVERY_PAYMENT_METHOD ||
                clientStateEnum == ClientStateEnum.DELIVERY_CONFIRMATION) {
            return orderModule.startClient(update, client, clientStateEnum, clientState);
        }
        else {
            return new SendMessage(update.getMessage().getChatId().toString(), "Что-то пошло не так");
        }
    }

    private ClientStateEnum getClientState(ClientState clientState) {
        List<String> cafeNameList = cafeService.getAllCafeNames();
        if (cafeNameList.contains(clientState.getState())) {
            return ClientStateEnum.ORDER_CAFE_MENU;
        }
        else {
            return ClientStateEnum.GET.clientState(clientState.getState());
        }
    }
}
