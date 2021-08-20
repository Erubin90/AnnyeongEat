package tech.erubin.annyeong_eat.telegramBotClient.module.mainMenu;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Client;
import tech.erubin.annyeong_eat.telegramBotClient.entity.ClientState;
import tech.erubin.annyeong_eat.telegramBotClient.states.ClientStateEnum;
import tech.erubin.annyeong_eat.telegramBotClient.module.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBotClient.service.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBotClient.service.ClientStatesServiceImpl;

@Component
@AllArgsConstructor
public class MainMenuModule {
    private final ClientServiceImpl clientService;
    private final ClientStatesServiceImpl clientStatesService;

    private final ReplyButtons replyButtons;

    private final MainMenuButtonNames buttonNames;
    private final MainMenuTextMessage textMessage;

    public BotApiMethod<?> startClient(Update update, Client client, ClientStateEnum clientStateEnum) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = textMessage.getError();
        ClientState clientState = null;
        switch (clientStateEnum) {
            case MAIN_MENU:
                sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                if (sourceText.equals(buttonNames.getCreateOrder())) {
                    text = textMessage.getChoosingCafe();
                    clientState = clientStatesService.create(client, ClientStateEnum.ORDER_CAFE.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderCafe(client));
                }
                else if (sourceText.equals(buttonNames.getCheckOrder())) {
                    text = "Просмотр статуса заказа";
                    clientState = clientStatesService.create(client, ClientStateEnum.ORDER_CHECK.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientCheckOrder());
                }
                else if (sourceText.equals(buttonNames.getHelp())) {
                    text = textMessage.getHelp();
                    clientState = clientStatesService.create(client, ClientStateEnum.HELP.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientHelp());
                }
                else if (sourceText.equals(buttonNames.getClientInfo())) {
                    text = textMessage.getClientProfile(client);
                    clientState = clientStatesService.create(client, ClientStateEnum.PROFILE.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientProfileInfo());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, clientState, text);
            case ORDER_CHECK:
                sendMessage.setReplyMarkup(replyButtons.clientCheckOrder());
                if (sourceText.equals(buttonNames.getBack())){
                    text = textMessage.getReturnMainMenu();
                    clientState = clientStatesService.create(client, ClientStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                return returnSendMessage(sendMessage, client, clientState, text);
            case HELP:
                sendMessage.setReplyMarkup(replyButtons.clientHelp());
                if (sourceText.equals(buttonNames.getBack())) {
                    text = textMessage.getReturnMainMenu();
                    clientState = clientStatesService.create(client, ClientStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                return returnSendMessage(sendMessage, client, clientState, text);
            case PROFILE:
                sendMessage.setReplyMarkup(replyButtons.clientProfileInfo());
                if (sourceText.equals(buttonNames.getBack())) {
                    text = textMessage.getReturnMainMenu();
                    clientState = clientStatesService.create(client, ClientStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                return returnSendMessage(sendMessage, client, clientState, text);
        }
        return returnSendMessage(sendMessage, text);
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, Client client, ClientState clientState, String text) {
        sendMessage.setText(text);
        clientService.save(client);
        clientStatesService.save(clientState);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }
}
