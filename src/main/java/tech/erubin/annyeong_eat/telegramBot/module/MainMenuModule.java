package tech.erubin.annyeong_eat.telegramBot.module;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.UserState;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.UserEnum;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Module;

import java.util.List;

@Component
public class MainMenuModule extends Module {
    private final CafeServiceImpl cafeService;
    private final ReplyButtons replyButtons;

    public MainMenuModule(OrderServiceImpl orderService, UserServiceImpl userService,
                          UserStatesServiceImpl userStatesService, OrderStatesServiceImpl orderStatesService,
                          CafeServiceImpl cafeService, ReplyButtons replyButtons) {
        super(orderService, userService, userStatesService, orderStatesService);
        this.cafeService = cafeService;
        this.replyButtons = replyButtons;
    }

    public BotApiMethod<?> start(Update update, User user, UserEnum userEnum) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = error;
        UserState userState = null;
        switch (userEnum) {
            case MAIN_MENU:
                if (sourceText.equals(replyButtons.getCreateOrder())) {
                    text = choosingCafe;
                    userState = userStatesService.create(user, UserEnum.ORDER_CAFE.getValue());
                    List<String> cafeNames = cafeService.getCafeNameByCity(user.getCity());
                    sendMessage.setReplyMarkup(replyButtons.userOrderCafe(cafeNames));
                }
                else if (sourceText.equals(replyButtons.getCheckOrder())) {
                    text = "Просмотр статуса заказа";
                    userState = userStatesService.create(user, UserEnum.ORDER_CHECK.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userCheckOrder());
                }
                else if (sourceText.equals(replyButtons.getHelp())) {
                    text = help;
                    userState = userStatesService.create(user, UserEnum.HELP.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userHelp());
                }
                else if (sourceText.equals(replyButtons.getClientInfo())) {
                    text = clientProfile(user);
                    userState = userStatesService.create(user, UserEnum.PROFILE.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userProfileInfo());
                }
                else {
                    text = putButton;
                    sendMessage.setReplyMarkup(replyButtons.userMainMenu());
                }
                break;
            case ORDER_CHECK:
                sendMessage.setReplyMarkup(replyButtons.userCheckOrder());
                if (sourceText.equals(replyButtons.getBack())){
                    text = returnMainMenu;
                    userState = userStatesService.create(user, UserEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userMainMenu());
                }
                break;
            case HELP:
                sendMessage.setReplyMarkup(replyButtons.userHelp());
                if (sourceText.equals(replyButtons.getBack())) {
                    text = returnMainMenu;
                    userState = userStatesService.create(user, UserEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userMainMenu());
                }
                break;
            case PROFILE:
                sendMessage.setReplyMarkup(replyButtons.userProfileInfo());
                if (sourceText.equals(replyButtons.getBack())) {
                    text = returnMainMenu;
                    userState = userStatesService.create(user, UserEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.userMainMenu());
                }
                break;
        }
        return sendMessage(sendMessage, userState, text);
    }
}
