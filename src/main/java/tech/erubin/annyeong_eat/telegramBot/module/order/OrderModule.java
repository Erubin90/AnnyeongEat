package tech.erubin.annyeong_eat.telegramBot.module.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.entity.*;
import tech.erubin.annyeong_eat.telegramBot.module.buttons.ButtonNames;
import tech.erubin.annyeong_eat.telegramBot.module.buttons.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.module.buttons.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.module.handler.CheckMessage;
import tech.erubin.annyeong_eat.service.*;
import tech.erubin.annyeong_eat.telegramBot.states.UserStateEnum;
import tech.erubin.annyeong_eat.telegramBot.states.OrderStateEnum;

import java.util.List;


@Component
@AllArgsConstructor
public class OrderModule {
    private final ButtonNames buttonNames;
    private final OrderTextMessage textMessage;

    private final CheckMessage checkMessage;

    private final ReplyButtons replyButtons;
    private final InlineButtons inlineButtons;

    private final UserServiceImpl clientService;
    private final OrderServiceImpl orderService;
    private final CafeServiceImpl cafeService;
    private final UserStatesServiceImpl clientStatesService;
    private final OrderStatesServiceImpl orderStatesService;

    public BotApiMethod<?> startClient(Update update, User user, UserStateEnum userStateEnum){
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = textMessage.getError();
        Order order = orderService.getOrder(user);
        UserState userState = null;
        OrderState orderState = null;
        switch (userStateEnum) {
            case ORDER_CAFE:
                List<String> cafeName = cafeService.getCafeNameByCity(user.getCity());
                sendMessage.setReplyMarkup(replyButtons.clientOrderCafe(user));
                if (cafeName.contains(sourceText)){
                    text = textMessage.getHello() + " " + sourceText;
                    Cafe cafe = cafeService.getCafeByName(sourceText);
                    order = orderService.getOrder(user, cafe);
                    order.setUsing(1);
                    orderState = orderStatesService.create(order, OrderStateEnum.ORDER_START_REGISTRATION.getValue());
                    userState = clientStatesService.create(user, sourceText);
                    sendMessage.setReplyMarkup(replyButtons.clientOrderMenu(order));
                }
                else if (sourceText.equals(buttonNames.getBack())) {
                    userState = clientStatesService.create(user, UserStateEnum.MAIN_MENU.getValue());
                    text = textMessage.getBackToMainMenu();
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, user, userState, order, orderState, text);
            case ORDER_CAFE_MENU:
                sendMessage.setReplyMarkup(replyButtons.clientOrderMenu(order));
                List<String> typeDishes = buttonNames.getTypeDishesInCafe(order);
                if (typeDishes.contains(sourceText)) {
                    text = sourceText + ":";
                    sendMessage.setReplyMarkup(inlineButtons.typeDishesMenu(sourceText));
                }
                else if (sourceText.equals(buttonNames.getBack())) {
                    text = textMessage.getBackToChoosingCafe();
                    order.setUsing(0);
                    userState = clientStatesService.create(user, UserStateEnum.ORDER_CAFE.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderCafe(user));
                }
                else if (sourceText.equals(buttonNames.getNext())) {
                    if (order.getChequeDishList().size() == 0) {
                        text = textMessage.getEmptyReceipt();
                    }
                    else {
                        text = textMessage.getNextToAddress();
                        userState = clientStatesService.create(user, UserStateEnum.DELIVERY_ADDRESS.getValue());
                        sendMessage.setReplyMarkup(replyButtons.clientOrderAddress(user));
                    }
                }
                else if (sourceText.equals("\uD83D\uDED2")) {
                    text = textMessage.getFullOrder(order);
                    sendMessage.setReplyMarkup(inlineButtons.getFullOrder(order));
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, user, userState, order, text);
            case DELIVERY_ADDRESS:
                sendMessage.setReplyMarkup(replyButtons.clientOrderAddress(user));
                if (sourceText.equals(buttonNames.getBack())) {
                    text = textMessage.getBackToOrderMenu();
                    userState = clientStatesService.create(user, UserStateEnum.ORDER_CAFE_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderMenu(order));
                }
                else {
                    text = checkMessage.checkAddress(sourceText);
                    if (!text.contains(textMessage.getErrorTrigger())) {
                        text = textMessage.getNextToPhoneNumber();
                        userState =
                                clientStatesService.create(user, UserStateEnum.DELIVERY_PHONE_NUMBER.getValue());
                        order.setAddress(sourceText);
                        sendMessage.setReplyMarkup(replyButtons.clientOrderPhoneNumber(user));
                    }
                }
                return returnSendMessage(sendMessage, user, userState, order, text);
            case DELIVERY_PHONE_NUMBER:
                sendMessage.setReplyMarkup(replyButtons.clientOrderPhoneNumber(user));
                if (sourceText.equals(buttonNames.getBack())) {
                    text = textMessage.getBackToAddress();
                    userState = clientStatesService.create(user, UserStateEnum.DELIVERY_ADDRESS.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderAddress(user));
                }
                else {
                    text = textMessage.getNextToPhoneNumber();
                    String checkText = checkMessage.checkPhoneNumber(sourceText);
                    if (!checkText.contains(textMessage.getErrorTrigger())) {
                        text = textMessage.getNextToPaymentMethod();
                        userState =
                                clientStatesService.create(user, UserStateEnum.DELIVERY_PAYMENT_METHOD.getValue());
                        if (sourceText.length() == 12) {
                            order.setPhoneNumber("8" + sourceText.substring(2, 12));
                        }
                        order.setPhoneNumber(sourceText);
                        sendMessage.setReplyMarkup(replyButtons.clientOrderPayment());
                    }
                }
                return returnSendMessage(sendMessage, user, userState, order, text);
            case DELIVERY_PAYMENT_METHOD:
                List<String> paymentMethod = buttonNames.getPaymentMethod();
                if (paymentMethod.contains(sourceText)) {
                    text = textMessage.getFullOrder(order);
                    order.setPaymentMethod(sourceText);
                    userState =
                            clientStatesService.create(user, UserStateEnum.DELIVERY_CONFIRMATION.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderConfirmation());
                }
                else if (sourceText.equals(buttonNames.getBack())) {
                    text = textMessage.getBackToPhoneNumber();
                    userState = clientStatesService.create(user, UserStateEnum.DELIVERY_PHONE_NUMBER.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderPhoneNumber(user));
                }
                else {
                    text = textMessage.getNotButton();
                    sendMessage.setReplyMarkup(replyButtons.clientOrderPayment());
                }
                return returnSendMessage(sendMessage, user, userState, order, text);
            case DELIVERY_CONFIRMATION:
                sendMessage.setReplyMarkup(replyButtons.clientOrderConfirmation());
                if (sourceText.equals(buttonNames.getConfirm())) {
                    text = textMessage.getReturnMainMenu();
                    order.setUsing(0);
                    orderState = orderStatesService.create(order, OrderStateEnum.ORDER_END_REGISTRATION.getValue());
                    userState = clientStatesService.create(user, UserStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                else if (sourceText.equals(buttonNames.getBack())) {
                    text = textMessage.getBackToPaymentMethod();
                    userState = clientStatesService.create(user, UserStateEnum.DELIVERY_PAYMENT_METHOD.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderPayment());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, user, userState, order, orderState, text);
        }
        return returnSendMessage(sendMessage, text);
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, User user, UserState userState,
                                           Order order, OrderState orderState, String text) {
        returnSendMessage(sendMessage, user, userState, order, text);
        orderStatesService.save(orderState);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, User user, UserState userState,
                                           Order order, String text) {
        sendMessage.setText(text);
        clientService.save(user);
        clientStatesService.save(userState);
        orderService.save(order);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }
}
