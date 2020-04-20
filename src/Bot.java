import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bot extends TelegramLongPollingBot{

    public HashMap<Long, User> users = new HashMap<>();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        System.out.println(text);
        long id = message.getChatId();

        if (text.equals("/start")) {
            sendMessage(message, "Здравствуйте, введите Ваше имя.");
            users.put(id, new User());
            return;
        }

        if (!users.containsKey(id)){
            sendMessage(message, "Пользователь не найден, введите /start.");
            return;
        }
        User user = users.get(id);
        if (!user.getCommand().equals("")){
            switch (user.getCommand()){
                case "name1":
                    user.setName(text);
                    user.setCommand("");
                    sendMessage(message, "Приятно познакомиться, " + text);
                    break;
                case "city1":
                    user.setCity(text);
                    user.setCommand("");
                    sendMessage(message, "город успешно сохранён");
                    break;
                default:
                    user.setCommand("");
                    sendMessage(message, "Неизвестная ошибка");
                    break;
            }
            return;
        }


        if (text.equals("/set_city")){
            sendMessage(message, "Введите название города.");
            user.setCommand("city1");
            return;
        }

        if (text.equals("/city")){
            sendMessage(message, "Ваш город: " + user.getCity());
            return;
        }



        sendMessage(message, user.getName() + ", Вы сказали: \"" + text + "\"");
    }



    private void sendMessage(Message m, String text){
        SendMessage message = new SendMessage();
        message.setChatId(m.getChatId());
        message.setText(text);
        message.enableMarkdown(true);
        setButtons(message);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "java_try_bot";
    }

    @Override
    public String getBotToken() {
        return "1270371974:AAG3thXALtUx_pzISpRnq1Hz2F8Nn3MkeIA";
    }

    public synchronized void setButtons(SendMessage message){
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        message.setReplyMarkup(keyboard);
        keyboard.setSelective(true);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);

        List <KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("/start"));
        keyboardRows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("/set_city"));
        row2.add(new KeyboardButton("/city"));
        keyboardRows.add(row2);

        keyboard.setKeyboard(keyboardRows);
    }

}
