package tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.module.mainMenu.MainMenuButtonNames;
import tech.erubin.annyeong_eat.telegramBot.module.order.OrderButtonNames;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.CafeServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.OrderServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.serviceInterface.ReplyButtonService;

import java.util.*;

@Service
@AllArgsConstructor
public class ReplyButtonServiceImpl implements ReplyButtonService {
    CafeServiceImpl cafeService;
    OrderServiceImpl orderService;
    MainMenuButtonNames mainMenuButtonNames;
    OrderButtonNames orderButtonNames;

    public ReplyKeyboardMarkup clientRegistrationCity() {
        Set<String> buttonNames = cafeService.getAllCity();
        return getReplyKeyboardMarkup(buttonNames);
    }

    @Override
    public ReplyKeyboardMarkup clientMainMenu() {
        List<String> buttonNames = mainMenuButtonNames.getMainMenuClientButton();
        return getReplyKeyboardMarkup(buttonNames);
    }

    @Override
    public ReplyKeyboardMarkup clientCheckOrder() {
        List<String> buttonNames = mainMenuButtonNames.getCheckOrderButton();
        return getReplyKeyboardMarkup(buttonNames);
    }

    @Override
    public ReplyKeyboardMarkup clientHelp() {
        List<String> buttonNames = mainMenuButtonNames.getHelpClientButton();
        return getReplyKeyboardMarkup(buttonNames);
    }


    @Override
    public ReplyKeyboardMarkup clientProfileInfo(Client client) {
        List<String> buttonNames = mainMenuButtonNames.getProfileInfoButton();
        return getReplyKeyboardMarkup(buttonNames);
    }

    @Override
    public ReplyKeyboardMarkup clientOrderCafe(Client client) {
        List<List<String>> buttonName = new ArrayList<>();
        List<String> cafeNames = cafeService.getCafeNameByCity(client.getCity());
        List<String> back = List.of(orderButtonNames.getBack());
        for (String cafeName : cafeNames) {
            buttonName.add(List.of(cafeName));
        }
        buttonName.add(back);
        return getReplyKeyboardMarkup(buttonName);
    }

    @Override
    public ReplyKeyboardMarkup clientOrderMenu() {
        List<List<String>> buttonNames = orderButtonNames.getMenuRows();
        return getReplyKeyboardMarkup(buttonNames);
    }

    @Override
    public ReplyKeyboardMarkup clientOrderAddress(Client client) {
        Set<String> buttonNames = new HashSet<>();
        List<Order> orderList = client.getOrderList();

        for (Order order : orderList){
            if(order.getAddress() != null){
                buttonNames.add(order.getAddress());
            }
        }

        List<KeyboardRow> keyboardRows = getKeyboardManyButtonRows(buttonNames);
        keyboardRows.add(getKeyboardOneButtonRow(orderButtonNames.getBack()));
        return new ReplyKeyboardMarkup(keyboardRows, true, false, false);
    }

    @Override
    public ReplyKeyboardMarkup clientOrderPhoneNumber(Client client) {
        String clientPhoneNumber = client.getPhoneNumber();
        Set<String> buttonNames = new HashSet<>();
        buttonNames.add(clientPhoneNumber);
        List<Order> orderList = client.getOrderList();

        for (Order order : orderList) {
            if(order.getPhoneNumber() != null){
                buttonNames.add(order.getPhoneNumber());
            }
        }

        List<KeyboardRow> keyboardRows = getKeyboardManyButtonRows(buttonNames);
        keyboardRows.add(getKeyboardOneButtonRow(orderButtonNames.getBack()));
        return new ReplyKeyboardMarkup(keyboardRows, true, false, false);
    }

    @Override
    public ReplyKeyboardMarkup clientOrderPayment() {
        List<List<String>> buttonNames = orderButtonNames.getPaymentRows();
        return getReplyKeyboardMarkup(buttonNames);
    }

    @Override
    public ReplyKeyboardMarkup clientOrderConfirmation() {
        List<List<String>> buttonNames = orderButtonNames.getConfirmRows();
        return getReplyKeyboardMarkup(buttonNames);
    }

    @Override
    public ReplyKeyboardMarkup clientOrderEdition() {
        List<List<String>> buttonNames = orderButtonNames.getEditionRows();
        return getReplyKeyboardMarkup(buttonNames);
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(Collection<String> buttonNames) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String buttonName : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(buttonName);
            keyboardRows.add(row);
        }
        return new ReplyKeyboardMarkup(keyboardRows, true, false, false);
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(List<List<String>> buttonNames) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (List<String> buttonName : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.addAll(buttonName);
            keyboardRows.add(row);
        }
        return new ReplyKeyboardMarkup(keyboardRows, true, false, false);
    }

    private List<KeyboardRow> getKeyboardManyButtonRows(List<String> buttonNames) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String buttonName : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(buttonName);
            keyboardRows.add(row);
        }
        return keyboardRows;
    }

    private List<KeyboardRow> getKeyboardManyButtonRows(Set<String> buttonNames) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String buttonName : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(buttonName);
            keyboardRows.add(row);
        }
        return keyboardRows;
    }

    private KeyboardRow getKeyboardOneButtonRow(String buttonName) {
        KeyboardRow row = new KeyboardRow();
        row.add(buttonName);
        return row;
    }
}
