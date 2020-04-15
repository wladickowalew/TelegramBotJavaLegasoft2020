import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot{

    public HashMap<Long, String> users = new HashMap<>();

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
            if (users.containsKey(id)) users.remove(id);
            sendMessage(message, "Здравствуйте, введите Ваше имя.");
        } else {
            if (users.containsKey(id)) {
                sendMessage(message, users.get(id) + ", Вы сказали: \"" + text + "\"");
            } else {
                users.put(id, text);
                sendMessage(message, "Приятно познакомиться, " + text);
            }
        }
    }

    private void sendMessage(Message m, String text){
        SendMessage message = new SendMessage();
        message.setChatId(m.getChatId());
        message.setText(text);
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
}
