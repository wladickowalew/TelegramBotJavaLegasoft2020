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
import java.security.PublicKey;
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
        if (condition_dispatcher(message, user)) return;
        if (command_dispatcher(message, user))   return;
        sendMessage(message, user.getName() + ", Вы сказали: \"" + text + "\"");
    }

    public boolean condition_dispatcher(Message message, User user){
        if (user.getCommand().equals("")) return false;
        String text = message.getText();
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
            case "age1":
                try {
                    user.setAge(Integer.parseInt(text));
                    user.setCommand("");
                    sendMessage(message, "Возраст успешно сохранён");
                }catch (Exception e){
                    sendMessage(message, "Вы ввели не целое число. Введите только целое число");
                }
                break;
            default:
                user.setCommand("");
                sendMessage(message, "Неизвестная ошибка");
                break;
        }
        return true;
    }

    public boolean command_dispatcher(Message message, User user){
        String text = message.getText();
        if (text.equals("/set_city")){
            sendMessage(message, "Введите название города.");
            user.setCommand("city1");
            return true;
        }

        if (text.equals("/city")){
            sendMessage(message, "Ваш город: " + user.getCity());
            return true;
        }

        if (text.equals("/set_age")){
            sendMessage(message, "Введите, сколько вам лет.");
            user.setCommand("age1");
            return true;
        }

        if (text.equals("/age")){
            sendMessage(message, "Ваш возраст: " + user.getAge());
            return true;
        }

        if (text.equals("/rename")){
            sendMessage(message, "Введите ваше новое имя: ");
            user.setCommand("name1");
            return true;
        }

        if (text.equals("/info")){
            sendMessage(message, user.getInfo());
            return true;
        }
        return false;
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
        row1.add(new KeyboardButton("/rename"));
        row1.add(new KeyboardButton("/info"));
        keyboardRows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("/set_city"));
        row2.add(new KeyboardButton("/city"));
        keyboardRows.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("/set_age"));
        row3.add(new KeyboardButton("/age"));
        keyboardRows.add(row3);

        keyboard.setKeyboard(keyboardRows);
    }

}
