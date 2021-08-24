package tech.erubin.annyeong_eat.telegramBot.module.buttons;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tech.erubin.annyeong_eat.entity.ChequeDish;
import tech.erubin.annyeong_eat.entity.Dish;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.service.DishServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InlineButtons {
    private final ButtonNames buttonNames;
    private final DishServiceImpl dishService;

    public InlineKeyboardMarkup clientCheque(String tag, ChequeDish chequeDish) {
        String count;
        if (chequeDish == null) {
            count = "0";
        }
        else {
            count = String.valueOf(chequeDish.getCountDishes());
        }

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(getSubCountAddButton(count, tag));
        rows.add(List.of(getInlineButton(buttonNames.getDelete(), tag + "m0")));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup typeDishesMenu(String typeDish) {
        List<Dish> dishList = dishService.getDishListByType(typeDish);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        if (dishList != null && !dishList.isEmpty()) {
            for (Dish dish : dishList) {
                String nameButton = dish.getName() + " " + dish.getPrice() + "₽";
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
                buttonList.add(getInlineButton(buttonNames.getDelete(), dish.getId() + "b0"));
                buttonList.add(getInlineButton(buttonNames.getSub(), dish.getId() + "b-"));
                buttonList.add(getInlineButton(buttonNames.getAdd(), dish.getId() + "b+"));
                rows.add(buttonList);
            }
        }
        return new InlineKeyboardMarkup(rows);
    }

    private List<InlineKeyboardButton> getSubCountAddButton (String count, String id) {
        InlineKeyboardButton subDish = getInlineButton( buttonNames.getSub(), id + "m-");
        InlineKeyboardButton countDish = getInlineButton(count, id + "m=");
        InlineKeyboardButton addDish = getInlineButton(buttonNames.getAdd(), id + "m+");
        return List.of(subDish, countDish, addDish);
    }

    private InlineKeyboardButton getInlineButton(String nameButton, String id){
        InlineKeyboardButton subDish = new InlineKeyboardButton(nameButton);
        subDish.setCallbackData(id);
        return subDish;
    }
}
