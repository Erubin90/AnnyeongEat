package tech.erubin.annyeong_eat.telegramBot.module.order;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.entity.*;
import tech.erubin.annyeong_eat.telegramBot.module.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.module.ClientStateEnum;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.*;
import tech.erubin.annyeong_eat.telegramBot.module.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.module.ReplyButtons;

import java.sql.Timestamp;
import java.util.List;


@Component
public class OrderModule {
    private final OrderButtonNames buttonName;
    private final OrderTextMessage textMessage;

    private final CheckMessage checkMessage;
    private final AnnyeongEatWebHook webHook;

    private final ReplyButtons replyButtonsService;
    private final InlineButtons inlineButtonsService;

    private final ClientServiceImpl clientService;
    private final OrderServiceImpl orderService;
    private final CafeServiceImpl cafeService;
    private final DishServiceImpl dishService;
    private final ChequeServiceImpl chequeService;
    private final ClientStatesServiceImpl stateService;

    public OrderModule(OrderButtonNames buttonName, OrderTextMessage textMessage, CheckMessage checkMessage,
                       @Lazy AnnyeongEatWebHook webHook, ReplyButtons buttonService,
                       InlineButtons inlineButtonsService, ClientServiceImpl clientService,
                       OrderServiceImpl orderService, CafeServiceImpl cafeService, DishServiceImpl dishService,
                       ChequeServiceImpl chequeService, ClientStatesServiceImpl stateService) {
        this.buttonName = buttonName;
        this.textMessage = textMessage;
        this.checkMessage = checkMessage;
        this.webHook = webHook;
        this.replyButtonsService = buttonService;
        this.inlineButtonsService = inlineButtonsService;
        this.clientService = clientService;
        this.orderService = orderService;
        this.cafeService = cafeService;
        this.dishService = dishService;
        this.chequeService = chequeService;
        this.stateService = stateService;
    }

    public BotApiMethod<?> startClient(Update update, Client client, ClientStateEnum clientStateEnum,
                                       ClientStates clientStates){
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String text = textMessage.getError();
        Order order = orderService.getOrder(client);
        switch (clientStateEnum) {
            case ORDER_CAFE:
                List<String> cafeName = cafeService.getCafeNameByCity(client.getCity());
                sendMessage.setReplyMarkup(replyButtonsService.clientOrderCafe(client));
                if (cafeName.contains(sourceText)){
                    text = textMessage.getHello() + " " + sourceText;
                    clientStates.setState(sourceText);
                    Cafe cafe = cafeService.getCafeByName(sourceText);
                    order = orderService.getOrder(client, cafe);
                    order.setUsing(1);
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderMenu(order));
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    clientStates.setState(ClientStateEnum.MAIN_MENU.getValue());
                    text = textMessage.getBackToMainMenu();
                    sendMessage.setReplyMarkup(replyButtonsService.clientMainMenu());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, clientStates, order, text);
            case ORDER_CAFE_MENU:
                sendMessage.setReplyMarkup(replyButtonsService.clientOrderMenu(order));
                List<String> typeDishes = buttonName.getTypeDishesInCafe(order);
                List<String> tagDishes = buttonName.getTagDishesInCafe(order);
                if (typeDishes.contains(sourceText)) {
                    text = textMessage.getDishesByTypeInTargetMenu(sourceText);
                }
                else if (tagDishes.contains(sourceText)) {
                    Dish dish = dishService.getDishByTag(sourceText);
                    text = textMessage.getTextDishesByTag(dish);
                    String url = dish.getLinkPhoto();
                    Cheque cheque = chequeService.getChequeByOrderAndDish(order, dish);
                    InlineKeyboardMarkup inlineKeyboard = inlineButtonsService.clientCheque(order, sourceText, cheque);
                    if (webHook.sendPhoto(chatId, url, text, inlineKeyboard)){
                        text = textMessage.getServerOk();
                    }
                    else {
                        text = textMessage.getServerError();
                    }
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToChoosingCafe();
                    order.setUsing(0);
                    clientStates.setState(ClientStateEnum.ORDER_CAFE.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderCafe(client));
                }
                else if (sourceText.equals(buttonName.getNext())) {
                    if (order.getChequeList().size() == 0) {
                        text = textMessage.getEmptyReceipt();
                    }
                    else {
                        text = textMessage.getNextToAddress();
                        clientStates.setState(ClientStateEnum.DELIVERY_ADDRESS.getValue());
                        sendMessage.setReplyMarkup(replyButtonsService.clientOrderAddress(client));
                    }
                }
                else if (sourceText.contains("\uD83D\uDED2")) {
                    text = textMessage.getFullOrder(order);
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, clientStates, order, text);
            case DELIVERY_ADDRESS:
                sendMessage.setReplyMarkup(replyButtonsService.clientOrderAddress(client));
                if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToOrderMenu();
                    clientStates.setState(ClientStateEnum.ORDER_CAFE_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderMenu(order));
                }
                else {
                    text = checkMessage.checkAddress(sourceText);
                    if (!text.contains(textMessage.getErrorTrigger())) {
                        text = textMessage.getNextToPhoneNumber();
                        clientStates.setState(ClientStateEnum.DELIVERY_PHONE_NUMBER.getValue());
                        order.setAddress(sourceText);
                        sendMessage.setReplyMarkup(replyButtonsService.clientOrderPhoneNumber(client));
                    }
                }
                return returnSendMessage(sendMessage, client, clientStates, order, text);
            case DELIVERY_PHONE_NUMBER:
                sendMessage.setReplyMarkup(replyButtonsService.clientOrderPhoneNumber(client));
                if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToAddress();
                    clientStates.setState(ClientStateEnum.DELIVERY_ADDRESS.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderAddress(client));
                }
                else {
                    text = textMessage.getNextToPhoneNumber();
                    String checkText = checkMessage.checkPhoneNumber(sourceText);
                    if (!checkText.contains(textMessage.getErrorTrigger())) {
                        text = textMessage.getNextToPaymentMethod();
                        clientStates.setState(ClientStateEnum.DELIVERY_PAYMENT_METHOD.getValue());
                        if (sourceText.length() == 12)
                            order.setPhoneNumber("8" + sourceText.substring(2,12));
                        order.setPhoneNumber(sourceText);
                        sendMessage.setReplyMarkup(replyButtonsService.clientOrderPayment());
                    }
                }
                return returnSendMessage(sendMessage, client, clientStates, order, text);
            case DELIVERY_PAYMENT_METHOD:
                List<String> paymentMethod = buttonName.getPaymentMethod();
                if (paymentMethod.contains(sourceText)) {
                    text = textMessage.getFullOrder(order);
                    order.setPaymentMethod(sourceText);
                    clientStates.setState(ClientStateEnum.DELIVERY_CONFIRMATION.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderConfirmation());
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToPhoneNumber();
                    clientStates.setState(ClientStateEnum.DELIVERY_PHONE_NUMBER.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderPhoneNumber(client));
                }
                else {
                    text = textMessage.getNotButton();
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderPayment());
                }
                return returnSendMessage(sendMessage, client, clientStates, order, text);
            case DELIVERY_CONFIRMATION:
                sendMessage.setReplyMarkup(replyButtonsService.clientOrderConfirmation());
                if (sourceText.equals(buttonName.getConfirm())) {
                    text = textMessage.getReturnMainMenu();
                    order.setOrderStatus("оформлен");
                    order.setUsing(0);
                    order.setTimeAccept(new Timestamp(System.currentTimeMillis()));
                    clientStates.setState(ClientStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientMainMenu());
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToPaymentMethod();
                    clientStates.setState(ClientStateEnum.DELIVERY_PHONE_NUMBER.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderPayment());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, clientStates, order, text);
        }
        return returnSendMessage(sendMessage, text);
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, Client client, ClientStates clientStates, Order order, String text) {
        sendMessage.setText(text);
        clientService.saveClient(client);
        orderService.saveOrder(order);
        String lastClientState = client.getClientStatesList().get(client.getClientStatesList().size() - 2).getState();
        String clientState = clientStates.getState();
        if (!lastClientState.equals(clientState)) {
            stateService.saveStates(clientStates);
        }
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }
}
