package tech.erubin.annyeong_eat.telegramBot.module.order;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.entity.Cafe;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.module.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.CafeServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.DishServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.OrderServiceImpl;
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

    public OrderModule(OrderButtonNames buttonName, OrderTextMessage textMessage, CheckMessage checkMessage,
                       @Lazy AnnyeongEatWebHook webHook, ReplyButtonServiceImpl buttonService,
                       InlineButtonServiceImpl inlineButtonService, ClientServiceImpl clientService, OrderServiceImpl orderService,
                       CafeServiceImpl cafeService, DishServiceImpl dishService) {
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
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(replyButtonService.clientOrderCafe(client));
                if (cafeName.contains(sourceText)){
                    text = "Доро пожаловать в " + sourceText;
                    client.setState("меню");
                    Cafe cafe = cafeService.getCafeByName(sourceText);
                    order.setCafeId(cafe);
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderMenu(order));
                }
                else if (buttonName.getBack().equals(sourceText)) {
                    client.setState("главное меню");
                    client.setStatus("главное меню");
                    text = "Главное меню";
                    sendMessage.setReplyMarkup(replyButtonService.clientMainMenu());
                }
                else {
                    text = "Воспользуйтесь кнопками выборе кафе";
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "меню":
                sendMessage.enableMarkdown(true);
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
                    InlineKeyboardMarkup inlineKeyboard = inlineButtonService.clientCheque(order, sourceText);
                    if (webHook.sendPhoto(chatId, url, text, inlineKeyboard)){
                        text = "";
                    }
                    else {
                        text = "Ошибка с телеграм веб хуком";
                    }
                }
                else if (buttonName.getBack().equals(sourceText)) {
                    text = "поменять кафе";
                    client.setState("выбор кафе");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderCafe(client));
                }
                else if (buttonName.getNext().equals(sourceText)) {
                    text = "Укажите улицу куда доставить";
                    client.setState("доставка улица");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderAddress(client));
                }
                else {
                    text = "Нажата не та кнопка в выборе кафе";
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "доставка улица":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(replyButtonService.clientOrderAddress(client));
                if (sourceText.equals(buttonName.getBack())) {
                    text = "меню";
                    client.setState("меню");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderMenu(order));
                }
                else {
                    text = checkMessage.checkAddress(sourceText);
                    if (!text.contains(textMessage.getErrorTrigger())) {
                        text = "Выберете один из номеров";
                        client.setState("доставка номер");
                        order.setAddress(sourceText);
                        sendMessage.setReplyMarkup(replyButtonService.clientOrderPhoneNumber(client));
                    }
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "доставка номер":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(replyButtonService.clientOrderPhoneNumber(client));
                if (buttonName.getBack().equals(sourceText)) {
                    client.setState("доставка улица");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderAddress(client));
                }
                else {
                    text = "Используйте кнопки в меню указания номера";
                    String checkText = checkMessage.checkPhoneNumber(sourceText);
                    if (!checkText.contains(textMessage.getErrorTrigger())) {
                        text = "Выбирете способ оплаты";
                        client.setState("способ оплаты");
                        if (sourceText.length() == 12)
                            order.setPhoneNumber("8" + sourceText.substring(2,12));
                        order.setPhoneNumber(sourceText);
                        sendMessage.setReplyMarkup(replyButtonService.clientOrderPayment());
                    }
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "способ оплаты":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(replyButtonService.clientOrderPayment());
                List<String> paymentMethod = buttonName.getPaymentMethod();
                if (paymentMethod.contains(sourceText)) {
                    text = textMessage.getFullOrder(order);
                    order.setPaymentMethod(sourceText);
                    client.setState("подтверждение заказа");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderConfirmation());
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = "выберите номер";
                    client.setState("доставка номер");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderPhoneNumber(client));
                }
                else {
                    text = "Используйте кнопки в меню выбора способа оплаты";
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "подтверждение заказа":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(replyButtonService.clientOrderConfirmation());
                if (buttonName.getConfirm().equals(sourceText)) {
                    text = "главное меню";
                    order.setOrderStatus("оформлен");
                    client.setState("главное меню");
                    client.setStatus("главное меню");
                    sendMessage.setReplyMarkup(replyButtonService.clientMainMenu());
                }
                else if (buttonName.getBack().equals(sourceText)) {
                    text = "Выберите способ оплаты";
                    client.setState("способ оплаты");
                    sendMessage.setReplyMarkup(replyButtonService.clientOrderPayment());
                }
                else {
                    text = "Используйте кнопку чтоб подтвердить заказ";
                }
                return returnSendMessage(sendMessage, client, order, text);
        }
        return returnSendMessage(sendMessage, text);
    }

    public BotApiMethod<?> callBackQuery(){
        BotApiMethod<?> callbackQuery = null;

        return callbackQuery;
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
