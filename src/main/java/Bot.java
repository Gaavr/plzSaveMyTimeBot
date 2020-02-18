import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    private static Bot bot;
    public static Bot getBot() {
        if (bot == null) {
            bot = new Bot();
        }
        return bot;
    }

    public void onUpdateReceived(Update update) {

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());

        defineCommand(update, message);

        // Отправить сообщение пользователю
        try {
            execute(message);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }

        while (Data.getData().getNumberOfMinutes() != 0) {
            try {
                GoogleSheetAPI.getGoogleSheetAPI().writeToSheet(Data.getData().getCellID(), Data.getData().getSubject(),
                        Data.getData().getNumberOfMinutes());
            } catch (Exception e) {
                System.out.println("Не удалось записать данные в таблицу");
                e.printStackTrace();
            }
            Data.getData().setNumberOfMinutes(0);
        }
    }

    public void defineCommand(Update update, SendMessage message) {



        //Получаем текст от пользователя
        String command = getUserCommand(update);

        if (Data.getData().isNumeric(command)) {
            try {
                Data.getData().setNumberOfMinutes(Integer.parseInt(command));
                message.setText(command + " минут/минуты будут добавлены к выбранной категории!");
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
        System.out.println("Пользователем была введена команада " + userCommand);
        return userCommand;
    }

    public String getBotUsername() {
        return "plzSaveMyTimeBot";
    }

    public String getBotToken() {
        return "";
    }
}
