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
import tech.erubin.annyeong_eat.telegramBot.module.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.module.ReplyButtons;
import tech.erubin.annyeong_eat.telegramBot.service.entityServiсes.*;
import tech.erubin.annyeong_eat.telegramBot.states.ClientStateEnum;
import tech.erubin.annyeong_eat.telegramBot.states.OrderStateEnum;

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
    private final ChequeDishServiceImpl chequeService;
    private final ClientStatesServiceImpl clientStatesService;
    private final OrderStatesServiceImpl orderStatesService;

    public OrderModule(OrderButtonNames buttonName, OrderTextMessage textMessage, CheckMessage checkMessage,
                       @Lazy AnnyeongEatWebHook webHook, ReplyButtons buttonService,
                       InlineButtons inlineButtonsService, ClientServiceImpl clientService,
                       OrderServiceImpl orderService, CafeServiceImpl cafeService, DishServiceImpl dishService,
                       ChequeDishServiceImpl chequeService, ClientStatesServiceImpl clientStatesService,
                       OrderStatesServiceImpl orderStatesService) {
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
        this.clientStatesService = clientStatesService;
        this.orderStatesService = orderStatesService;
    }

    public BotApiMethod<?> startClient(Update update, Client client, ClientStateEnum clientStateEnum,
                                       ClientState clientState){
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
                    Cafe cafe = cafeService.getCafeByName(sourceText);
                    order = orderService.getOrder(client, cafe);
                    order.setUsing(1);
                    OrderState orderState = orderStatesService.create(order);
                    orderState.setState(OrderStateEnum.ORDER_START_REGISTRATION.getValue());
                    clientState.setState(sourceText);
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderMenu(order));
                    return returnSendMessage(sendMessage, client, clientState, order, orderState, text);
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    clientState.setState(ClientStateEnum.MAIN_MENU.getValue());
                    text = textMessage.getBackToMainMenu();
                    sendMessage.setReplyMarkup(replyButtonsService.clientMainMenu());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, clientState, order, text);
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
                    ChequeDish chequeDish = chequeService.getChequeByOrderAndDish(order, dish);
                    InlineKeyboardMarkup inlineKeyboard = inlineButtonsService.clientCheque(order, sourceText, chequeDish);
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
                    clientState.setState(ClientStateEnum.ORDER_CAFE.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderCafe(client));
                }
                else if (sourceText.equals(buttonName.getNext())) {
                    if (order.getChequeDishList().size() == 0) {
                        text = textMessage.getEmptyReceipt();
                    }
                    else {
                        text = textMessage.getNextToAddress();
                        clientState.setState(ClientStateEnum.DELIVERY_ADDRESS.getValue());
                        sendMessage.setReplyMarkup(replyButtonsService.clientOrderAddress(client));
                    }
                }
                else if (sourceText.contains("\uD83D\uDED2")) {
                    text = textMessage.getFullOrder(order);
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, clientState, order, text);
            case DELIVERY_ADDRESS:
                sendMessage.setReplyMarkup(replyButtonsService.clientOrderAddress(client));
                if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToOrderMenu();
                    clientState.setState(ClientStateEnum.ORDER_CAFE_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderMenu(order));
                }
                else {
                    text = checkMessage.checkAddress(sourceText);
                    if (!text.contains(textMessage.getErrorTrigger())) {
                        text = textMessage.getNextToPhoneNumber();
                        clientState.setState(ClientStateEnum.DELIVERY_PHONE_NUMBER.getValue());
                        order.setAddress(sourceText);
                        sendMessage.setReplyMarkup(replyButtonsService.clientOrderPhoneNumber(client));
                    }
                }
                return returnSendMessage(sendMessage, client, clientState, order, text);
            case DELIVERY_PHONE_NUMBER:
                sendMessage.setReplyMarkup(replyButtonsService.clientOrderPhoneNumber(client));
                if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToAddress();
                    clientState.setState(ClientStateEnum.DELIVERY_ADDRESS.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderAddress(client));
                }
                else {
                    text = textMessage.getNextToPhoneNumber();
                    String checkText = checkMessage.checkPhoneNumber(sourceText);
                    if (!checkText.contains(textMessage.getErrorTrigger())) {
                        text = textMessage.getNextToPaymentMethod();
                        clientState.setState(ClientStateEnum.DELIVERY_PAYMENT_METHOD.getValue());
                        if (sourceText.length() == 12)
                            order.setPhoneNumber("8" + sourceText.substring(2,12));
                        order.setPhoneNumber(sourceText);
                        sendMessage.setReplyMarkup(replyButtonsService.clientOrderPayment());
                    }
                }
                return returnSendMessage(sendMessage, client, clientState, order, text);
            case DELIVERY_PAYMENT_METHOD:
                List<String> paymentMethod = buttonName.getPaymentMethod();
                if (paymentMethod.contains(sourceText)) {
                    text = textMessage.getFullOrder(order);
                    order.setPaymentMethod(sourceText);
                    clientState.setState(ClientStateEnum.DELIVERY_CONFIRMATION.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderConfirmation());
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToPhoneNumber();
                    clientState.setState(ClientStateEnum.DELIVERY_PHONE_NUMBER.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderPhoneNumber(client));
                }
                else {
                    text = textMessage.getNotButton();
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderPayment());
                }
                return returnSendMessage(sendMessage, client, clientState, order, text);
            case DELIVERY_CONFIRMATION:
                sendMessage.setReplyMarkup(replyButtonsService.clientOrderConfirmation());
                if (sourceText.equals(buttonName.getConfirm())) {
                    text = textMessage.getReturnMainMenu();
                    order.setUsing(0);
                    OrderState orderState = orderStatesService.create(order);
                    orderState.setState(OrderStateEnum.ORDER_END_REGISTRATION.getValue());
                    clientState.setState(ClientStateEnum.MAIN_MENU.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientMainMenu());
                    return returnSendMessage(sendMessage, client, clientState, order, orderState, text);
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToPaymentMethod();
                    clientState.setState(ClientStateEnum.DELIVERY_PHONE_NUMBER.getValue());
                    sendMessage.setReplyMarkup(replyButtonsService.clientOrderPayment());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, clientState, order, text);
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
        clientService.saveClient(client);
        String lastClientState = client.getClientStateList().get(client.getClientStateList().size() - 2).getState();
        String newClientState = clientState.getState();
        if (!lastClientState.equals(newClientState)) {
            clientStatesService.save(clientState);
        }
        orderService.save(order);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }
}
