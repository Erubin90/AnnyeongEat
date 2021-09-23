package tech.erubin.annyeong_eat.telegramBot.buttons;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.telegramBot.enums.EmployeeEnum;
import tech.erubin.annyeong_eat.telegramBot.textMessages.Buttons;

import java.util.*;

@Component
@NoArgsConstructor
public class ReplyButtons extends Buttons {

    public ReplyKeyboardMarkup userRegistrationCity(Set<String> cafeCits) {
        List<List<String>> buttonNames = getCityListButtons(cafeCits);
        return replyKeyboardMarkup(buttonNames, true);
    }

    public ReplyKeyboardMarkup userMainMenu() {
        return replyKeyboardMarkup(clientMainMenuButtons());
    }

    public ReplyKeyboardMarkup employeeMainMenu(EmployeeEnum employeeStateEnum) {
        return replyKeyboardMarkup(employeeMainMenuButtons(employeeStateEnum));
    }

    public ReplyKeyboardMarkup userCheckOrder() {
        return replyKeyboardMarkup(checkOrderButtons());
    }

    public ReplyKeyboardMarkup userHelp() {
        return replyKeyboardMarkup(clientHelpButtons());
    }

    public ReplyKeyboardMarkup userProfileInfo() {
        return replyKeyboardMarkup(profileInfoButtons());
    }

    public ReplyKeyboardMarkup userOrderCafe(List<String> cafeNames) {
        List<List<String>> buttonName = new ArrayList<>();
        for (String cafeName : cafeNames) {
            buttonName.add(List.of(cafeName));
        }
        buttonName.add(List.of(back));
        return replyKeyboardMarkup(buttonName, false);
    }

    public ReplyKeyboardMarkup userOrderMenu(Order order) {
        return replyKeyboardMarkup(dishesMenuButtons(order), false);
    }

    public ReplyKeyboardMarkup userOrderAddress(User user) {
        Set<String> buttonNames = new HashSet<>();
        List<Order> orderList = user.getOrderList();

        for (Order order : orderList){
            if(order.getAddress() != null){
                buttonNames.add(order.getAddress());
            }
        }

        List<KeyboardRow> keyboardRows = keyboardManyButtonRows(buttonNames);
        keyboardRows.add(keyboardOneButtonRow(this.getBack()));
        return new ReplyKeyboardMarkup(keyboardRows, true, false,
                false, putButton);
    }

    public ReplyKeyboardMarkup userOrderPhoneNumber(User user) {
        String clientPhoneNumber = user.getPhoneNumber();
        List<String> buttonNames = new ArrayList<>();
        buttonNames.add(clientPhoneNumber);
        List<Order> orderList = user.getOrderList();
        for (int i = 1, j = 0; i <= orderList.size() && j < 3; i++) {
            Order order = orderList.get(orderList.size() - i);
            if (order.getPhoneNumber() != null && !buttonNames.contains(order.getPhoneNumber())) {
                j++;
            }
        }
        buttonNames.add(back);
        return replyKeyboardMarkup(buttonNames);
    }

    public ReplyKeyboardMarkup userOrderPayment() {
        return replyKeyboardMarkup(paymentButtons(), false);
    }

    public ReplyKeyboardMarkup userOrderConfirmation() {
        return replyKeyboardMarkup(confirmButtons(), false);
    }

    public ReplyKeyboardMarkup choiceDepartment(List<String> department) {
        return replyKeyboardMarkup(choiceDepartmentButtons(department));
    }

    private ReplyKeyboardMarkup replyKeyboardMarkup(Collection<String> buttonNames) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String buttonName : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(buttonName);
            keyboardRows.add(row);
        }
        return new ReplyKeyboardMarkup(keyboardRows, true, false, false, putButton);
    }

    private ReplyKeyboardMarkup replyKeyboardMarkup(List<List<String>> buttonNames, boolean oneTimeKeyboard) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (List<String> buttonName : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.addAll(buttonName);
            keyboardRows.add(row);
        }
        return new ReplyKeyboardMarkup(keyboardRows, true, oneTimeKeyboard, false, putButton);
    }

    private List<KeyboardRow> keyboardManyButtonRows(Set<String> buttonNames) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String buttonName : buttonNames) {
            KeyboardRow row = new KeyboardRow();
            row.add(buttonName);
            keyboardRows.add(row);
        }
        return keyboardRows;
    }

    private KeyboardRow keyboardOneButtonRow(String buttonName) {
        KeyboardRow row = new KeyboardRow();
        row.add(buttonName);
        return row;
    }
}
