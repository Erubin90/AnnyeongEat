package tech.erubin.annyeong_eat.telegramBot.module.mainMenu;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.ClientState;
import tech.erubin.annyeong_eat.telegramBot.states.ClientStateEnum;
import tech.erubin.annyeong_eat.telegramBot.module.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.service.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.ClientStatesServiceImpl;

@Component
@AllArgsConstructor
public class MainMenuModule {
    private final ClientServiceImpl clientService;
    private final ClientStatesServiceImpl stateService;

    private final ReplyButtons replyButtons;

    private final MainMenuButtonNames buttonNames;
    private final MainMenuTextMessage textMessage;

    public BotApiMethod<?> startClient(Update update, Client client, ClientStateEnum clientStateEnum, ClientState clientState) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = textMessage.getError();
        switch (clientStateEnum) {
            case MAIN_MENU:
                sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                if (sourceText.equals(buttonNames.getCreateOrder())) {
                    text = textMessage.getChoosingCafe();
                    clientState.setState(ClientStateEnum.ORDER_CAFE.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderCafe(client));
                }
                else if (sourceText.equals(buttonNames.getCheckOrder())) {
                    text = "Просмотр статуса заказа";
                    clientState.setState(ClientStateEnum.ORDER_CHECK.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientCheckOrder());
                }
                else if (sourceText.equals(buttonNames.getHelp())) {
                    text = textMessage.getHelp();
                    clientState.setState(ClientStateEnum.HELP.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientHelp());
                }
                else if (sourceText.equals(buttonNames.getClientInfo())) {
                    text = textMessage.getClientProfile(client);
                    clientState.setState(ClientStateEnum.PROFILE.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientProfileInfo(client));
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, clientState, text);
            case ORDER_CHECK:
                sendMessage.setReplyMarkup(replyButtons.clientCheckOrder());
                if (sourceText.equals(buttonNames.getBack())){
                    text = textMessage.getReturnMainMenu();
                    clientState.setState(ClientStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                return returnSendMessage(sendMessage, client, clientState, text);
            case HELP:
                sendMessage.setReplyMarkup(replyButtons.clientHelp());
                if (sourceText.equals(buttonNames.getBack())) {
                    text = textMessage.getReturnMainMenu();
                    clientState.setState(ClientStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                return returnSendMessage(sendMessage, client, clientState, text);
            case PROFILE:
                sendMessage.setReplyMarkup(replyButtons.clientProfileInfo(client));
                if (sourceText.equals(buttonNames.getBack())) {
                    text = textMessage.getReturnMainMenu();
                    clientState.setState(ClientStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                return returnSendMessage(sendMessage, client, clientState, text);
        }
        return returnSendMessage(sendMessage, text);
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, Client client, ClientState clientState, String text) {
        sendMessage.setText(text);
        clientService.saveClient(client);
        stateService.save(clientState);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }
}
