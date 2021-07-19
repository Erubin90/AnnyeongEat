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
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.*;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.InlineButtonServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.ReplyButtonServiceImpl;

import java.util.List;


@Component
public class OrderModule {
    private final OrderButtonNames buttonName;
    private final OrderTextMessage textMessage;

    private final CheckMessage checkMessage;
    private final AnnyeongEatWebHook webHook;

    private final ReplyButtonServiceImpl replyButtonService;
    private final InlineButtonServiceImpl inlineButtonService;

    private final ClientServiceImpl clientService;
    private final OrderServiceImpl orderService;
    private final CafeServiceImpl cafeService;
    private final DishServiceImpl dishService;
    private final ChequeServiceImpl chequeService;

    public OrderModule(OrderButtonNames buttonName, OrderTextMessage textMessage, CheckMessage checkMessage,
                       @Lazy AnnyeongEatWebHook webHook, ReplyButtonServiceImpl buttonService,
                       InlineButtonServiceImpl inlineButtonService, ClientServiceImpl clientService, OrderServiceImpl orderService,
                       CafeServiceImpl cafeService, DishServiceImpl dishService, ChequeServiceImpl chequeService) {
        this.buttonName = buttonName;
        this.textMessage = textMessage;
        this.checkMessage = checkMessage;
        this.webHook = webHook;
        this.replyButtonService = buttonService;
        this.inlineButtonService = inlineButtonService;
        this.clientService = clientService;
        this.orderService = orderService;
        this.cafeService = cafeService;
        this.dishService = dishService;
        this.chequeService = chequeService;
    }

    public BotApiMethod<?> startClient(Update update, Client client){
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String sourceText = update.getMessage().getText();
        String sate = client.getState();
        String text = "Команда " + sourceText + " необработана в DishesModule.startClient";
        Order order = orderService.getOrder(client);
        switch (sate) {
            case "выбор кафе":
                List<String> cafeName = cafeService.getCafeNameByCity(client.getCity());
                sendMessage.setReplyMarkup(replyButtonService.clientOrderCafe(client));
                if (cafeName.contains(sourceText)){
                    text = textMessage.getHello() + " " + sourceText;
                    client.setState("меню");
                    Cafe cafe = cafeService.getCafeByName(sourceText);
                    order.setCafeId(cafe);
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderMenu(order));
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    client.setState("главное меню");
                    client.setStatus("главное меню");
                    text = textMessage.getBackToMainMenu();
                    sendMessage.setReplyMarkup(replyButtonService.clientMainMenu());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "меню":
                sendMessage.setReplyMarkup(replyButtonService.clientOrderMenu(order));
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
                    InlineKeyboardMarkup inlineKeyboard = inlineButtonService.clientCheque(order, sourceText, cheque);
                    if (webHook.sendPhoto(chatId, url, text, inlineKeyboard)){
                        text = textMessage.getServerOk();
                    }
                    else {
                        text = textMessage.getServerError();
                    }
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToChoosingCafe();
                    client.setState("выбор кафе");
                    client.setOrderList(null);
                    clientService.saveClient(client);
                    orderService.deleteOrder(order);
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderCafe(client));
                    return returnSendMessage(sendMessage, text);
                }
                else if (sourceText.equals(buttonName.getNext())) {
                    if (order.getChequeList().size() == 0) {
                        text = textMessage.getEmptyReceipt();
                    }
                    else {
                        text = textMessage.getNextToAddress();
                        client.setState("доставка улица");
                        sendMessage.setReplyMarkup(replyButtonService.clientOrderAddress(client));
                    }
                }
                else if (sourceText.contains("\uD83D\uDED2")) {
                    text = textMessage.getFullOrder(order);
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "доставка улица":
                sendMessage.setReplyMarkup(replyButtonService.clientOrderAddress(client));
                if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToOrderMenu();
                    client.setState("меню");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderMenu(order));
                }
                else {
                    text = checkMessage.checkAddress(sourceText);
                    if (!text.contains(textMessage.getErrorTrigger())) {
                        text = textMessage.getNextToPhoneNumber();
                        client.setState("доставка номер");
                        order.setAddress(sourceText);
                        sendMessage.setReplyMarkup(replyButtonService.clientOrderPhoneNumber(client));
                    }
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "доставка номер":
                sendMessage.setReplyMarkup(replyButtonService.clientOrderPhoneNumber(client));
                if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToAddress();
                    client.setState("доставка улица");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderAddress(client));
                }
                else {
                    text = textMessage.getNextToPhoneNumber();
                    String checkText = checkMessage.checkPhoneNumber(sourceText);
                    if (!checkText.contains(textMessage.getErrorTrigger())) {
                        text = textMessage.getNextToPaymentMethod();
                        client.setState("способ оплаты");
                        if (sourceText.length() == 12)
                            order.setPhoneNumber("8" + sourceText.substring(2,12));
                        order.setPhoneNumber(sourceText);
                        sendMessage.setReplyMarkup(replyButtonService.clientOrderPayment());
                    }
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "способ оплаты":
                List<String> paymentMethod = buttonName.getPaymentMethod();
                if (paymentMethod.contains(sourceText)) {
                    text = textMessage.getFullOrder(order);
                    order.setPaymentMethod(sourceText);
                    client.setState("подтверждение заказа");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderConfirmation());
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = textMessage.getBackToPhoneNumber();
                    client.setState("доставка номер");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderPhoneNumber(client));
                }
                else {
                    text = textMessage.getNotButton();
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderPayment());
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "подтверждение заказа":
                sendMessage.setReplyMarkup(replyButtonService.clientOrderConfirmation());
                if (buttonName.getConfirm().equals(sourceText)) {
                    text = textMessage.getReturnMainMenu();
                    order.setOrderStatus("оформлен");
                    client.setState("главное меню");
                    client.setStatus("главное меню");
                    sendMessage.setReplyMarkup(replyButtonService.clientMainMenu());
                }
                else if (buttonName.getBack().equals(sourceText)) {
                    text = textMessage.getBackToPaymentMethod();
                    client.setState("способ оплаты");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderPayment());
                }
                else {
                    text = textMessage.getNotButton();
                }
                return returnSendMessage(sendMessage, client, order, text);
        }
        return returnSendMessage(sendMessage, text);
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, Client client, Order order, String text) {
        sendMessage.setText(text);
        clientService.saveClient(client);
        orderService.saveOrder(order);
        return sendMessage;
    }

    private SendMessage returnSendMessage (SendMessage sendMessage, String text) {
        sendMessage.setText(text);
        return sendMessage;
    }
}
