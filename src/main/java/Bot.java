import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
                    GoogleSheetAPI.writeToSheet(Data.getData().getCellID(), Data.getData().getSubject(),
                            Data.getData().getNumberOfMinutes() +
                                    GoogleSheetAPI.getCellValue(Data.getData().getSubject(),
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
           message.setText("Данный бот работает только на своего создателя! (@gaavr)");
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
                    message.setText("Введите количество минут инвестированных в изучение Java");
                    Data.getData().setSubject("C");
                    break;
                case "/english":
                    message.setText("Введите количество минут инвестированных в изучение английскго языка");
                    Data.getData().setSubject("D");
                    break;
                case "/reading":
                    message.setText("Введите количество минут инвестированных в чтение литературы");
                    Data.getData().setSubject("E");
                    break;
                case "/sport":
                    message.setText("Введите количетво минут инвестирвоанных в занятия спортом");
                    Data.getData().setSubject("F"); // press "F" to pay respects for this subject
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

    public String getBotUsername() {
        return "@plzSaveMyTimeBot";
    }

    public String getBotToken() {
        return "853299646:AAHSPc0FmTTy8_LhYE-sH8mvWlSk9lNOQ-M";
    }
}
