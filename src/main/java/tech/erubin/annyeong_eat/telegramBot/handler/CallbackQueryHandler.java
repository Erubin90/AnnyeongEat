package tech.erubin.annyeong_eat.telegramBot.handler;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import tech.erubin.annyeong_eat.telegramBot.AnnyeongEatWebHook;
import tech.erubin.annyeong_eat.telegramBot.entity.*;
import tech.erubin.annyeong_eat.telegramBot.service.*;
import tech.erubin.annyeong_eat.telegramBot.states.ClientStateEnum;
import tech.erubin.annyeong_eat.telegramBot.module.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.module.ReplyButtons;

import java.util.List;

@Component
public class CallbackQueryHandler {
    private final ClientServiceImpl clientService;
    private final OrderServiceImpl orderService;
    private final DishServiceImpl dishService;
    private final ChequeDishServiceImpl chequeService;
    private final ClientStatesServiceImpl stateService;
    private final CafeServiceImpl cafeService;

    private final InlineButtons inlineButtons;
    private final ReplyButtons replyButtons;

    private final AnnyeongEatWebHook webHook;

    public CallbackQueryHandler(ClientServiceImpl clientService, OrderServiceImpl orderService,
                                DishServiceImpl dishService, ChequeDishServiceImpl chequeService,
                                ClientStatesServiceImpl stateService, CafeServiceImpl cafeService,
                                InlineButtons inlineButtons, ReplyButtons replyButtons,
                                @Lazy AnnyeongEatWebHook webHook) {
        this.clientService = clientService;
        this.orderService = orderService;
        this.dishService = dishService;
        this.chequeService = chequeService;
        this.stateService = stateService;
        this.cafeService = cafeService;
        this.inlineButtons = inlineButtons;
        this.replyButtons = replyButtons;
        this.webHook = webHook;
    }

    public BotApiMethod<?> handleUpdate(CallbackQuery callback) {
        BotApiMethod<?> botApiMethod = null;
        String userId = callback.getFrom().getId().toString();
        Client client = clientService.getClientByTelegramUserId(userId);
        ClientState clientState = stateService.getState(client);
        ClientStateEnum clientStateEnum = getClientState(clientState);
        String buttonDate = callback.getData();
        String chatId = callback.getMessage().getChatId().toString();
        switch (clientStateEnum) {
            case ORDER_CAFE_MENU:
                String tag = buttonDate.substring(0, buttonDate.length() - 1);
                Order order = orderService.getOrder(client);
                Dish dish = dishService.getDishByTag(tag);
                ChequeDish chequeDish = chequeService.getChequeByOrderAndDish(order, dish);
                int messageId = callback.getMessage().getMessageId();
                int count = chequeDish.getCountDishes();
                String text = "Непонятно что происходит";
                if (buttonDate.matches("/\\w*[+]")) {
                    count++;
                    text = dish.getName() + " добавлен";
                } else if (buttonDate.matches("/\\w*[-]")) {
                    if (count >= 0) {
                        if (count > 0) {
                            count--;
                            text = dish.getName() + " удален";
                        }
                        else {
                            text = dish.getName() + " нет в корзине";
                        }
                    }
                }
                chequeDish.setCountDishes(count);
                if (count > 0) {
                    chequeService.saveCheque(chequeDish);
                }
                else {
                    chequeService.deleteCheque(chequeDish);
                }
                InlineKeyboardMarkup inlineMarkup = inlineButtons.clientCheque(order, tag, chequeDish);
                ReplyKeyboardMarkup replyMarkup = replyButtons.clientOrderMenu(order);
                webHook.updateMarkups(chatId, messageId, text, inlineMarkup, replyMarkup);
                SendMessage sendMessage = new SendMessage(chatId, text);
                sendMessage.setReplyMarkup(replyMarkup);
                botApiMethod = sendMessage;
            }
        return botApiMethod;
    }

    private ClientStateEnum getClientState(ClientState clientState) {
        List<String> cafeNameList = cafeService.getAllCafeNames();
        if (cafeNameList.contains(clientState.getState())) {
            return ClientStateEnum.ORDER_CAFE_MENU;
        }
        else {
            return ClientStateEnum.GET.clientState(clientState.getState());
        }
    }
}
