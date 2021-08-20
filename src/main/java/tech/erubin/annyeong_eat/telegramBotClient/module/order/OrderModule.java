package tech.erubin.annyeong_eat.telegramBotClient.module.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBotClient.entity.*;
import tech.erubin.annyeong_eat.telegramBotClient.module.handler.CheckMessage;
import tech.erubin.annyeong_eat.telegramBotClient.module.InlineButtons;
import tech.erubin.annyeong_eat.telegramBotClient.module.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBotClient.service.*;
import tech.erubin.annyeong_eat.telegramBotClient.states.ClientStateEnum;
import tech.erubin.annyeong_eat.telegramBotClient.states.OrderStateEnum;

import java.util.List;


@Component
@AllArgsConstructor
public class OrderModule {
    private final OrderButtonNames buttonName;
    private final OrderTextMessage textMessage;

    private final CheckMessage checkMessage;

    private final ReplyButtons replyButtons;
    private final InlineButtons inlineButtons;

    private final ClientServiceImpl clientService;
    private final OrderServiceImpl orderService;
    private final CafeServiceImpl cafeService;
    private final ClientStatesServiceImpl clientStatesService;
    private final OrderStatesServiceImpl orderStatesService;

    public BotApiMethod<?> startClient(Update update, Client client, ClientStateEnum clientStateEnum){
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = textMessage.getError();
        Order order = orderService.getOrder(client);
        ClientState clientState = null;
        OrderState orderState = null;
        switch (clientStateEnum) {
            case ORDER_CAFE:
                List<String> cafeName = cafeService.getCafeNameByCity(client.getCity());
                sendMessage.setReplyMarkup(replyButtons.clientOrderCafe(client));
                if (cafeName.contains(sourceText)){
                    text = textMessage.getHello() + " " + sourceText;
                    Cafe cafe = cafeService.getCafeByName(sourceText);
                    order = orderService.getOrder(client, cafe);
                    order.setUsing(1);
                    orderState = orderStatesService.create(order, OrderStateEnum.ORDER_START_REGISTRATION.getValue());
                    clientState = clientStatesService.create(client, sourceText);
                    sendMessage.setReplyMarkup(replyButtons.clientOrderMenu(order));
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    clientState = clientStatesService.create(client, ClientStateEnum.MAIN_MENU.getValue());
                    text = textMessage.getBackToMainMenu();
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, clientState, order, orderState, text);
            case ORDER_CAFE_MENU:
                sendMessage.setReplyMarkup(replyButtons.clientOrderMenu(order));
                List<String> typeDishes = buttonName.getTypeDishesInCafe(order);
                if (typeDishes.contains(sourceText)) {
                    text = sourceText + ":";
                    sendMessage.setReplyMarkup(inlineButtons.typeDishesMenu(sourceText));
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToChoosingCafe();
                    order.setUsing(0);
                    clientState = clientStatesService.create(client, ClientStateEnum.ORDER_CAFE.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderCafe(client));
                }
                else if (sourceText.equals(buttonName.getNext())) {
                    if (order.getChequeDishList().size() == 0) {
                        text = textMessage.getEmptyReceipt();
                    }
                    else {
                        text = textMessage.getNextToAddress();
                        clientState = clientStatesService.create(client, ClientStateEnum.DELIVERY_ADDRESS.getValue());
                        sendMessage.setReplyMarkup(replyButtons.clientOrderAddress(client));
                    }
                }
                else if (sourceText.equals("\uD83D\uDED2")) {
                    text = textMessage.getFullOrder(order);
                    sendMessage.setReplyMarkup(inlineButtons.getFullOrder(order));
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, clientState, order, text);
            case DELIVERY_ADDRESS:
                sendMessage.setReplyMarkup(replyButtons.clientOrderAddress(client));
                if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToOrderMenu();
                    clientState = clientStatesService.create(client, ClientStateEnum.ORDER_CAFE_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderMenu(order));
                }
                else {
                    text = checkMessage.checkAddress(sourceText);
                    if (!text.contains(textMessage.getErrorTrigger())) {
                        text = textMessage.getNextToPhoneNumber();
                        clientState =
                                clientStatesService.create(client, ClientStateEnum.DELIVERY_PHONE_NUMBER.getValue());
                        order.setAddress(sourceText);
                        sendMessage.setReplyMarkup(replyButtons.clientOrderPhoneNumber(client));
                    }
                }
                return returnSendMessage(sendMessage, client, clientState, order, text);
            case DELIVERY_PHONE_NUMBER:
                sendMessage.setReplyMarkup(replyButtons.clientOrderPhoneNumber(client));
                if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToAddress();
                    clientState = clientStatesService.create(client, ClientStateEnum.DELIVERY_ADDRESS.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderAddress(client));
                }
                else {
                    text = textMessage.getNextToPhoneNumber();
                    String checkText = checkMessage.checkPhoneNumber(sourceText);
                    if (!checkText.contains(textMessage.getErrorTrigger())) {
                        text = textMessage.getNextToPaymentMethod();
                        clientState =
                                clientStatesService.create(client, ClientStateEnum.DELIVERY_PAYMENT_METHOD.getValue());
                        if (sourceText.length() == 12) {
                            order.setPhoneNumber("8" + sourceText.substring(2, 12));
                        }
                        order.setPhoneNumber(sourceText);
                        sendMessage.setReplyMarkup(replyButtons.clientOrderPayment());
                    }
                }
                return returnSendMessage(sendMessage, client, clientState, order, text);
            case DELIVERY_PAYMENT_METHOD:
                List<String> paymentMethod = buttonName.getPaymentMethod();
                if (paymentMethod.contains(sourceText)) {
                    text = textMessage.getFullOrder(order);
                    order.setPaymentMethod(sourceText);
                    clientState =
                            clientStatesService.create(client, ClientStateEnum.DELIVERY_CONFIRMATION.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderConfirmation());
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToPhoneNumber();
                    clientState = clientStatesService.create(client, ClientStateEnum.DELIVERY_PHONE_NUMBER.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderPhoneNumber(client));
                }
                else {
                    text = textMessage.getNotButton();
                    sendMessage.setReplyMarkup(replyButtons.clientOrderPayment());
                }
                return returnSendMessage(sendMessage, client, clientState, order, text);
            case DELIVERY_CONFIRMATION:
                sendMessage.setReplyMarkup(replyButtons.clientOrderConfirmation());
                if (sourceText.equals(buttonName.getConfirm())) {
                    text = textMessage.getReturnMainMenu();
                    order.setUsing(0);
                    orderState = orderStatesService.create(order, OrderStateEnum.ORDER_END_REGISTRATION.getValue());
                    clientState = clientStatesService.create(client, ClientStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientMainMenu());
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToPaymentMethod();
                    clientState = clientStatesService.create(client, ClientStateEnum.DELIVERY_PHONE_NUMBER.getValue());
                    sendMessage.setReplyMarkup(replyButtons.clientOrderPayment());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, clientState, order, orderState, text);
        }
        return returnSendMessage(sendMessage, text);
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, Client client, ClientState clientState,
                                           Order order, OrderState orderState, String text) {
        returnSendMessage(sendMessage, client, clientState, order, text);
        orderStatesService.save(orderState);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, Client client, ClientState clientState,
                                           Order order, String text) {
        sendMessage.setText(text);
        clientService.save(client);
        clientStatesService.save(clientState);
        orderService.save(order);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }
}
