package tech.erubin.annyeong_eat.telegramBot.module.order;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class OrderButtonNames {

    @Value("${button.back}")
    private String back;

    @Value("${button.next}")
    private String next;

    @Value("${order.button.client.snacks}")
    private String snacks;

    @Value("${order.button.client.coups}")
    private String coups;

    @Value("${order.button.client.salads}")
    private String salads;

    @Value("${order.button.client.drinks}")
    private String drinks;

    @Value("${order.button.client.veganMenu}")
    private String veganMenu;

    @Value("${order.button.client.cash}")
    private String Cash;

    @Value("${order.button.client.cards}")
    private String cards;

    @Value("${order.button.client.confirm}")
    private String confirm;

    @Value("${order.button.client.editor}")
    private String edition;

    @Value("${order.button.client.editor.menu}")
    private String editionMenu;

    @Value("${order.button.client.editor.address}")
    private String editionAddress;

    @Value("${order.button.client.editor.phoneNumber}")
    private String editionPhoneNumber;

    @Value("${order.button.client.editor.payment}")
    private String editionPayment;

    public List<String> getBackAndNextButton() {
        return List.of(back, next);
    }

    public List<List<String>> getMenuRows() {
        List<List<String>> orderMenuRows = new ArrayList<>();
        orderMenuRows.add(List.of(snacks, coups));
        orderMenuRows.add(List.of(salads, drinks));
        orderMenuRows.add(List.of(veganMenu));
        orderMenuRows.add(getBackAndNextButton());
        return orderMenuRows;
    }

    public List<List<String>> getPaymentRows() {
        List<List<String>> orderPayment = new ArrayList<>();
        orderPayment.add(List.of(Cash));
        orderPayment.add(List.of(cards));
        orderPayment.add(List.of(back));
        return orderPayment;
    }

    public List<List<String>> getConfirmRows() {
        List<List<String>> orderConfirm = new ArrayList<>();
        orderConfirm.add(List.of(confirm));
        return orderConfirm;
    }

    public List<List<String>> getEditionRows() {
        List<List<String>> orderEdition = new ArrayList<>();
        orderEdition.add(List.of(editionMenu, editionAddress));
        orderEdition.add(List.of(editionPhoneNumber, editionPayment));
        orderEdition.add(List.of(back));
        orderEdition.add(List.of(confirm));
        return orderEdition;
    }
}
