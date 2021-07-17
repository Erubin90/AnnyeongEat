package tech.erubin.annyeong_eat.telegramBot.handler;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.entity.Cheque;
import tech.erubin.annyeong_eat.telegramBot.entity.Client;
import tech.erubin.annyeong_eat.telegramBot.entity.Dish;
import tech.erubin.annyeong_eat.telegramBot.entity.Order;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ChequeServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.ClientServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.DishServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.entityServises.OrderServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.InlineButtonServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.service.telegramBotServices.ReplyButtonServiceImpl;

@Component
public class CallbackQueryHandler {
    private final ClientServiceImpl clientService;
    private final OrderServiceImpl orderService;
    private final DishServiceImpl dishService;
    private final ChequeServiceImpl chequeService;

    private final InlineButtonServiceImpl inlineButtonService;
    private final ReplyButtonServiceImpl replyButtonService;

    private final AnnyeongEatWebHook webHook;

    public CallbackQueryHandler(ClientServiceImpl clientService, OrderServiceImpl orderService,
                                DishServiceImpl dishService, ChequeServiceImpl chequeService,
                                InlineButtonServiceImpl inlineButtonService, ReplyButtonServiceImpl replyButtonService,
                                @Lazy AnnyeongEatWebHook webHook) {
        this.clientService = clientService;
        this.orderService = orderService;
        this.dishService = dishService;
        this.chequeService = chequeService;
        this.inlineButtonService = inlineButtonService;
        this.replyButtonService = replyButtonService;
        this.webHook = webHook;
    }

    public BotApiMethod<?> handleUpdate(CallbackQuery callback) {
        BotApiMethod<?> botApiMethod = null;
        String userId = callback.getFrom().getId().toString();
        Client client = clientService.getClientByTelegramUserId(userId);
        String clientStatus = client.getStatus();
        String clientState = client.getState();
        String buttonDate = callback.getData();
        String chatId = callback.getMessage().getChatId().toString();
        switch (clientStatus) {
            case "главное меню" :
                break;
            case "оформление заказа" :
                switch (clientState) {
                    case "меню" :
                        String tag = buttonDate.substring(0, buttonDate.length() - 1);
                        Order order = orderService.getOrder(client);
                        Dish dish = dishService.getDishByTag(tag);
                        Cheque cheque = chequeService.getChequeByOrderAndDish(order, dish);
                        int messageId = callback.getMessage().getMessageId();
                        int count = cheque.getCountDishes();
                        String text = "Непонятно что происходит";
                        if (buttonDate.matches("/\\w*[+]")) {
                            count++;
                            text = dish.getName() + " добавлен";
                        } else if (buttonDate.matches("/\\w*[-]")) {
                            if (count > 0) {
                                count--;
                                text = dish.getName() + " удален";
                            }
                        }
                        cheque.setCountDishes(count);
                        if (count > 0) {
                            chequeService.saveCheque(cheque);
                        }
                        else {
                            chequeService.deleteCheque(cheque);
                        }
                        InlineKeyboardMarkup inlineMarkup = inlineButtonService.clientCheque(order, tag, cheque);
                        ReplyKeyboardMarkup replyMarkup = replyButtonService.clientOrderMenu(order);
                        webHook.updateMarkups(chatId, messageId, text, inlineMarkup, replyMarkup);
                }
        }
        return botApiMethod;
    }
}
