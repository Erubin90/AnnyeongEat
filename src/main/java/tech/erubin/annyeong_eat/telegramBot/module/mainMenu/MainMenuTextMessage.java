package tech.erubin.annyeong_eat.telegramBot.module.mainMenu;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class MainMenuTextMessage {

    @Value("${mainMenu.message.client.help}")
    private String help;

    @Value("${message.notButton}")
    private String notButton;

    @Value("${mainMenu.message.client.returnMainMenu}")
    private String returnMainMenu;

    @Value("${mainMenu.message.client.emptyOrderInfo}")
    private String emptyOrderInfo;

    public String getClientProfile(Client client) {
        long countOrder = client.getOrderList().stream()
                .filter(x -> x.getOrderStatus().equals("оформлен"))
                .count();
        return "Имя - " + client.getName() + "\n" +
                "Фамилия - " + client.getSurname() + "\n" +
                "Номер - " + client.getPhoneNumber() + "\n" +
                "Город - " + client.getCity() + "\n" +
                "Количество заказов - " + countOrder;
    }


}
