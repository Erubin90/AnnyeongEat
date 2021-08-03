package tech.erubin.annyeong_eat.telegramBot.module;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tech.erubin.annyeong_eat.telegramBot.entity.Cheque;
import tech.erubin.annyeong_eat.telegramBot.entity.DishOptionally;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ChequeServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class InlineButtons {
    ChequeServiceImpl chequeService;

    public InlineKeyboardMarkup clientCheque(Order order, String tag, Cheque cheque) {
        String count;
        if (cheque == null) {
            count = "0";
        }
        else {
            count = String.valueOf(cheque.getCountDishes());
        }

        List<InlineKeyboardButton> row1 = getSubCountAddButton(count, tag);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardButton getInlineButton(String tag, String nameButton){
        InlineKeyboardButton subDish = new InlineKeyboardButton(nameButton);
        subDish.setCallbackData(tag + nameButton);
        return subDish;
    }

    private List<InlineKeyboardButton> getSubCountAddButton (String count, String tag) {
        InlineKeyboardButton subDish = getInlineButton(tag, "-");
        InlineKeyboardButton countDish = new InlineKeyboardButton(count);
        countDish.setCallbackData(tag + "=");
        InlineKeyboardButton addDish = getInlineButton(tag, "+");
        return List.of(subDish, countDish, addDish);
    }

    private InlineKeyboardButton getDishOptionallyButton(DishOptionally dishOp){
        String text = String.format("%s  %s₽", dishOp.getName(), dishOp.getPrice());
        String callbackDate = String.valueOf(dishOp.getId());
        InlineKeyboardButton dishOptionallyButton = new InlineKeyboardButton();
        dishOptionallyButton.setText(text);
        dishOptionallyButton.setCallbackData(callbackDate);
        return dishOptionallyButton;
    }
}
