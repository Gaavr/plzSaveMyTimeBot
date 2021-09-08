import google.WorkWithGoogleSheetAPI;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Bot extends TelegramLongPollingBot {

    private static Bot bot;
    public static Bot getBot() {
        if (bot == null) {
            bot = new Bot();
        }
        return bot;
    }

    Calendar calendar = new GregorianCalendar();

    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();

        if (update.getMessage().getChatId() == 547044275) {
            message.setChatId(update.getMessage().getChatId());
            defineCommand(update, message);

            // Отправить сообщение пользователю
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            while (Data.getData().getNumberOfMinutes() != 0) {
                try {
                    WorkWithGoogleSheetAPI.writeToSheet(Data.getData().getCellID(), Data.getData().getSubject(),
                            Data.getData().getNumberOfMinutes() +
                                    WorkWithGoogleSheetAPI.getCellValue(Data.getData().getSubject(),
                                            Data.getData().getCellID()));
                } catch (Exception e) {
                    System.out.println("Не удалось записать данные в таблицу");
                    e.printStackTrace();
                }
                Data.getData().setNumberOfMinutes(0);
            }
        }
        else {
           message.setChatId(update.getMessage().getChatId());
           System.out.println(update.getMessage().getChatId() + " попытался получить доступ к боту в " + calendar.getTime());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void defineCommand(Update update, SendMessage message) {
        //Получаем текст от пользователя
        String command = getUserCommand(update);

        if (Data.getData().isNumeric(command)) {
            try {
                Data.getData().setNumberOfMinutes(Integer.parseInt(command));
                message.setText(command + " минут/минуты были добавлены к выбранной категории!");
            }
            catch (Exception e) {
                e.printStackTrace();
                message.setText("Введенное число некорректно!");
            }
        }
        else {
            switch (command) {
                case "/java":
                    message.setText("Введите количество минут потраченного на изучение Java");
                    Data.getData().setSubject("C");
                    break;
                case "/english":
                    message.setText("Введите количество минут потраченного на изучение английского языка");
                    Data.getData().setSubject("D");
                    break;
                case "/reading":
                    message.setText("Введите количество минут потраченного на чтение литературы");
                    Data.getData().setSubject("E");
                    break;
                case "/sport":
                    message.setText("Введите количество минут потраченного на занятия спортом");
                    Data.getData().setSubject("F");
                    break;
            }
        }
    }

    public String getUserCommand(Update update) {
        String userCommand =  update.getMessage().getText();
        System.out.println("Пользователем " + update.getMessage().getChatId() + " была введена команда "
                + userCommand + " в " + calendar.getTime());
        return userCommand;
    }
    //bot username
    public String getBotUsername() {
        return "";
    }

    //telegram token
    public String getBotToken() {
        return "";
    }
}
