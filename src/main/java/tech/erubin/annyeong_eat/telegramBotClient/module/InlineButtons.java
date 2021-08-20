package tech.erubin.annyeong_eat.telegramBotClient.module;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tech.erubin.annyeong_eat.telegramBotClient.entity.ChequeDish;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Dish;
import tech.erubin.annyeong_eat.telegramBotClient.entity.Order;
import tech.erubin.annyeong_eat.telegramBotClient.service.DishServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InlineButtons {
    private final DishServiceImpl dishService;

    public InlineKeyboardMarkup clientCheque(String tag, ChequeDish chequeDish) {
        String count;
        if (chequeDish == null) {
            count = "0";
        }
        else {
            count = String.valueOf(chequeDish.getCountDishes());
        }

        List<InlineKeyboardButton> row1 = getSubCountAddButton(count, tag);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup typeDishesMenu(String typeDish) {
        List<Dish> dishList = dishService.getDishListByType(typeDish);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (dishList != null && !dishList.isEmpty()) {
            for (Dish dish : dishList) {
                String nameButton = String.format("%s %s₽", dish.getName(), dish.getPrice());
                rows.add(List.of(getInlineButton(nameButton, String.valueOf(dish.getId()))));
            }
        }
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getFullOrder(Order order) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (order.getChequeDishList() != null && !order.getChequeDishList().isEmpty()) {
            List<Dish> dishList = order.getChequeDishList().stream()
                    .map(ChequeDish::getDishId)
                    .collect(Collectors.toList());
            int count = 1;
            for (Dish dish : dishList) {
                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                buttonList.add(getInlineButton(String.valueOf(count++), String.valueOf(dish.getId())));
                buttonList.add(getInlineButton("Удалить", dish.getId() + "b0"));
                buttonList.add(getInlineButton("-", dish.getId() + "b-"));
                buttonList.add(getInlineButton("+", dish.getId() + "b+"));
                rows.add(buttonList);
            }
        }
        return new InlineKeyboardMarkup(rows);
    }

    private List<InlineKeyboardButton> getSubCountAddButton (String count, String id) {
        InlineKeyboardButton subDish = getInlineButton( "-", id + "m-");
        InlineKeyboardButton countDish = getInlineButton(count, id + "m=");
        InlineKeyboardButton addDish = getInlineButton("+", id + "m+");
        return List.of(subDish, countDish, addDish);
    }

    private InlineKeyboardButton getInlineButton(String nameButton, String id){
        InlineKeyboardButton subDish = new InlineKeyboardButton(nameButton);
        subDish.setCallbackData(id);
        return subDish;
    }
}
