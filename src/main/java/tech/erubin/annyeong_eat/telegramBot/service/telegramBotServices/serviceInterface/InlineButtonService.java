package tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.serviceInterface;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;

public interface InlineButtonService {

    InlineKeyboardMarkup clientCheque(Order order, String tag);
}
