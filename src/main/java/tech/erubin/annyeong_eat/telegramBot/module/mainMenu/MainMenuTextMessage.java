package tech.erubin.annyeong_eat.telegramBot.module.mainMenu;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.states.OrderStateEnum;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class MainMenuTextMessage {

    @Value("${mainMenu.message.client.help}")
    private String help;

    @Value("${message.error.notButton}")
    private String notButton;

    @Value("${mainMenu.message.client.returnMainMenu}")
    private String returnMainMenu;

    @Value("${mainMenu.message.client.choosingCafe}")
    private String choosingCafe;

    @Value("${mainMenu.message.client.error}")
    private String error;

    public String getClientProfile(User user) {
        int countOrder = 0;
        for (Order order : user.getOrderList()) {
            var orderStates = order.getOrderStateList();
            String state = orderStates.get(orderStates.size() - 1).getState();
            if(state.equals(OrderStateEnum.ORDER_CONFIRMATION.getValue())) {
                countOrder++;
            }
        }
        return "Имя - " + user.getName() + "\n" +
                "Фамилия - " + user.getSurname() + "\n" +
                "Номер - " + user.getPhoneNumber() + "\n" +
                "Город - " + user.getCity() + "\n" +
                "Количество заказов - " + countOrder;
    }


}
