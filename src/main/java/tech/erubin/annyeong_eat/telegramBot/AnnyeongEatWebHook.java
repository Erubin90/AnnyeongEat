package tech.erubin.annyeong_eat.telegramBot;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.erubin.annyeong_eat.entity.Employee;
import tech.erubin.annyeong_eat.entity.Order;
import tech.erubin.annyeong_eat.entity.User;
import tech.erubin.annyeong_eat.service.EmployeeServiceImpl;
import tech.erubin.annyeong_eat.telegramBot.buttons.InlineButtons;
import tech.erubin.annyeong_eat.telegramBot.enums.Departments;
import tech.erubin.annyeong_eat.telegramBot.handler.CallbackQueryHandler;
import tech.erubin.annyeong_eat.telegramBot.handler.MessageHandler;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AnnyeongEatWebHook extends TelegramWebhookBot {
    private final String botUsername;
    private final String botToken;
    private final String botPath;

    private final String posterToken;
    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;

    private final EmployeeServiceImpl departmentService;
    private final InlineButtons inlineButtons;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        BotApiMethod<?> botApiMethod = null;
        try {
            if (update.hasMessage()) {
                botApiMethod = messageHandler.handleUpdate(update, posterToken);
            }
            else if (update.hasCallbackQuery()) {
                botApiMethod = callbackQueryHandler.handleUpdate(update.getCallbackQuery(), posterToken);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            String errorText = e.getMessage() != null ? e.getMessage() : "?????????????????? ???????????? ???? ???????? ???????????? ???? ??????????????. ???????????????? ????????";
            List<Employee> developerList = departmentService.getDeveloperList();
            sendMessageDepartment(developerList, Departments.DEVELOPER, errorText, null);
        }
        return botApiMethod;
    }

    public boolean sendPhoto(String chatId, String text, String imgPath, InlineKeyboardMarkup inlineKeyboardMarkup){
        InputFile inputFile = new InputFile(imgPath);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(text);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendPhoto);
            return true;
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMarkups(EditMessageReplyMarkup editMessageReplyMarkup, int count) {
        try {
            execute(editMessageReplyMarkup);
            return true;
        }
        catch (TelegramApiException e) {
            if (count == 0) {
                return true;
            }
            else {
                e.printStackTrace();
                return false;
            }

        }
    }

    public boolean updateText(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
            return true;
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendMessageDepartment(List<Employee> employeeList, Departments departments,
                                      String text, Order order) {
        List<User> userList = employeeList.stream()
                .map(Employee::getUserId)
                .collect(Collectors.toList());
        InlineKeyboardMarkup inlineKeyboardMarkup = inlineButtons.employeeButtons(departments, order);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        try {
            for (User u : userList) {
                sendMessage.setChatId(u.getTelegramUserId());
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
