package tech.erubin.annyeong_eat.telegramBot.module.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.erubin.annyeong_eat.telegramBot.entity.Cafe;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.module.CheckMessage;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.CafeServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.OrderServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.ReplyButtonServiceImpl;

import java.util.List;


@Component
@AllArgsConstructor
public class OrderModule {
    private OrderButtonNames buttonName;
    private OrderTextMessage textMessage;

    private CheckMessage checkMessage;

    private ReplyButtonServiceImpl buttonService;
    private ClientServiceImpl clientService;
    private OrderServiceImpl orderService;
    private CafeServiceImpl cafeService;


    public BotApiMethod<?> startClient(Update update, Client client){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        String sourceText = update.getMessage().getText();
        String sate = client.getState();
        String text = "Команда " + sourceText + " необработана в DishesModule.startClient";
        Order order = orderService.getOrder(client);
        Cafe cafe = order.getCafeId();
        switch (sate) {
            case "выбор кафе":
                List<String> cafeName = cafeService.getCafeNameByCity(client.getCity());
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(buttonService.clientOrderCafe(client));
                if (cafeName.contains(sourceText)){
                    text = "Доро пожаловать в " + sourceText;
                    client.setState("меню");
                    cafe = cafeService.getCafeByName(sourceText);
                    order.setCafeId(cafe);
                    sendMessage.setReplyMarkup(buttonService.clientOrderMenu(order));
                }
                else if (buttonName.getBack().equals(sourceText)) {
                    client.setState("главное меню");
                    client.setStatus("главное меню");
                    text = "Главное меню";
                    sendMessage.setReplyMarkup(buttonService.clientMainMenu());
                }
                else {
                    text = "Воспользуйтесь кнопками выборе кафе";
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "меню":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(buttonService.clientOrderMenu(order));
                List<String> typeDishes = buttonName.getTypeDishesInCafe(order);
                if (typeDishes.contains(sourceText)) {
                    text = textMessage.getDishesByTypeInTargetMenu(sourceText);
                }
                else if (buttonName.getBack().equals(sourceText)) {
                    text = "поменять кафе";
                    client.setState("выбор кафе");
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(buttonService.clientOrderCafe(client));
                }
                else if (buttonName.getNext().equals(sourceText)) {
                    text = "Укажите улицу куда доставить";
                    client.setState("доставка улица");
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(buttonService.clientOrderAddress(client));
                }
                else {
                    text = "Нажата не та кнопка в выборе кафе";
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "доставка улица":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(buttonService.clientOrderAddress(client));
                if (sourceText.equals(buttonName.getBack())) {
                    text = "меню";
                    client.setState("меню");
                    sendMessage.setReplyMarkup(buttonService.clientOrderMenu(order));
                }
                else {
                    text = checkMessage.checkAddress(sourceText);
                    if (!text.contains(textMessage.getErrorTrigger())) {
                        text = "Выберете один из номеров";
                        client.setState("доставка номер");
                        order.setAddress(sourceText);
                        sendMessage.setReplyMarkup(buttonService.clientOrderPhoneNumber(client));
                    }
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "доставка номер":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(buttonService.clientOrderPhoneNumber(client));
                if (buttonName.getBack().equals(sourceText)) {
                    client.setState("доставка улица");
                    sendMessage.setReplyMarkup(buttonService.clientOrderAddress(client));
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
                        sendMessage.setReplyMarkup(buttonService.clientOrderPayment());
                    }
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "способ оплаты":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(buttonService.clientOrderPayment());
                List<String> paymentMethod = buttonName.getPaymentMethod();
                if (paymentMethod.contains(sourceText)) {
                    text = textMessage.getFullOrder(order);
                    order.setPaymentMethod(sourceText);
                    client.setState("подтверждение заказа");
                    sendMessage.setReplyMarkup(buttonService.clientOrderConfirmation());
                }
                else if (sourceText.equals(buttonName.getBack())) {
                    text = "выберите номер";
                    client.setState("доставка номер");
                    sendMessage.setReplyMarkup(buttonService.clientOrderPhoneNumber(client));
                }
                else {
                    text = "Используйте кнопки в меню выбора способа оплаты";
                }
                return returnSendMessage(sendMessage, client, order, text);
            case "подтверждение заказа":
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(buttonService.clientOrderConfirmation());
                if (buttonName.getConfirm().equals(sourceText)) {
                    text = "главное меню";
                    order.setOrderStatus("оформлен");
                    client.setState("главное меню");
                    client.setStatus("главное меню");
                    sendMessage.setReplyMarkup(buttonService.clientMainMenu());
                }
                else if (buttonName.getBack().equals(sourceText)) {
                    text = "Выберите способ оплаты";
                    client.setState("способ оплаты");
                    sendMessage.setReplyMarkup(buttonService.clientOrderPayment());
                }
                else {
                    text = "Используйте кнопку чтоб подтвердить заказ";
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
