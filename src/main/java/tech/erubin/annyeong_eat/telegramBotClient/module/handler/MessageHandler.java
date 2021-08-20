package tech.erubin.annyeong_eat.telegramBotClient.module.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Client;
import tech.erubin.annyeong_eat.telegramBotClient.entity.ClientState;
import tech.erubin.annyeong_eat.telegramBotClient.states.ClientStateEnum;
import tech.erubin.annyeong_eat.telegramBotClient.module.mainMenu.MainMenuModule;
import tech.erubin.annyeong_eat.telegramBotClient.module.order.OrderModule;
import tech.erubin.annyeong_eat.telegramBotClient.module.registration.RegistrationModule;
import tech.erubin.annyeong_eat.telegramBotClient.service.CafeServiceImpl;
import tech.erubin.annyeong_eat.telegramBotClient.service.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBotClient.service.ClientStatesServiceImpl;

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
    private final HandlersTextMessage textMessage;

    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> botApiMethod = null;

        if (update.getMessage() != null && update.getMessage().hasText()) {
            String userId = update.getMessage().getFrom().getId().toString();
            Client client = clientService.getClientByTelegramUserId(userId);
            if (client == null) {
                client = clientService.create(userId);
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
            return registrationModule.startClient(update, client, clientStateEnum);
        }
        else if (clientStateEnum == ClientStateEnum.MAIN_MENU ||
                clientStateEnum == ClientStateEnum.ORDER_CHECK ||
                clientStateEnum == ClientStateEnum.HELP ||
                clientStateEnum == ClientStateEnum.PROFILE) {
            return mainMenuModule.startClient(update, client, clientStateEnum);
        }
        else if (clientStateEnum == ClientStateEnum.ORDER_CAFE ||
                clientStateEnum == ClientStateEnum.ORDER_CAFE_MENU ||
                clientStateEnum == ClientStateEnum.DELIVERY_ADDRESS ||
                clientStateEnum == ClientStateEnum.DELIVERY_PHONE_NUMBER ||
                clientStateEnum == ClientStateEnum.DELIVERY_PAYMENT_METHOD ||
                clientStateEnum == ClientStateEnum.DELIVERY_CONFIRMATION) {
            return orderModule.startClient(update, client, clientStateEnum);
        }
        else {
            return new SendMessage(update.getMessage().getChatId().toString(), textMessage.getError());
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
