package tech.erubin.annyeong_eat.telegramBot.module.mainMenu;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.telegramBot.module.buttons.ButtonNames;
import tech.erubin.annyeong_eat.telegramBot.states.UserStateEnum;
import tech.erubin.annyeong_eat.telegramBot.module.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.service.UserServiceImpl;
import tech.erubin.annyeong_eat.service.UserStatesServiceImpl;

@Component
@AllArgsConstructor
public class MainMenuModule {
    private final UserServiceImpl clientService;
    private final UserStatesServiceImpl clientStatesService;

    private final ReplyButtons replyButtons;

    private final ButtonNames buttonNames;
    private final MainMenuTextMessage textMessage;

    public BotApiMethod<?> startClient(Update update, User user, UserStateEnum userStateEnum) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = textMessage.getError();
        UserState userState = null;
        switch (userStateEnum) {
            case MAIN_MENU:
                sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                if (sourceText.equals(buttonNames.getCreateOrder())) {
                    text = textMessage.getChoosingCafe();
                    userState = clientStatesService.create(user, UserStateEnum.ORDER_CAFE.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderCafe(user));
                }
                else if (sourceText.equals(buttonNames.getCheckOrder())) {
                    text = "Просмотр статуса заказа";
                    userState = clientStatesService.create(user, UserStateEnum.ORDER_CHECK.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientCheckOrder());
                }
                else if (sourceText.equals(buttonNames.getHelp())) {
                    text = textMessage.getHelp();
                    userState = clientStatesService.create(user, UserStateEnum.HELP.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientHelp());
                }
                else if (sourceText.equals(buttonNames.getClientInfo())) {
                    text = textMessage.getClientProfile(user);
                    userState = clientStatesService.create(user, UserStateEnum.PROFILE.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientProfileInfo());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, user, userState, text);
            case ORDER_CHECK:
                sendMessage.setReplyMarkup(replyButtons.clientCheckOrder());
                if (sourceText.equals(buttonNames.getBack())){
                    text = textMessage.getReturnMainMenu();
                    userState = clientStatesService.create(user, UserStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                return returnSendMessage(sendMessage, user, userState, text);
            case HELP:
                sendMessage.setReplyMarkup(replyButtons.clientHelp());
                if (sourceText.equals(buttonNames.getBack())) {
                    text = textMessage.getReturnMainMenu();
                    userState = clientStatesService.create(user, UserStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                return returnSendMessage(sendMessage, user, userState, text);
            case PROFILE:
                sendMessage.setReplyMarkup(replyButtons.clientProfileInfo());
                if (sourceText.equals(buttonNames.getBack())) {
                    text = textMessage.getReturnMainMenu();
                    userState = clientStatesService.create(user, UserStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                return returnSendMessage(sendMessage, user, userState, text);
        }
        return returnSendMessage(sendMessage, text);
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, User user, UserState userState, String text) {
        sendMessage.setText(text);
        clientService.save(user);
        clientStatesService.save(userState);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }
}
