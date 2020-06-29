import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class GoogleSheetAPI {

    public static Sheets sheetsService;
    private static String APPLICATION_NAME = "plzsavemytimebot";
    private static String SPREADSHEET_ID = "1KgvfFbUuCmnYwb2f8PpYQENBdXAZPWAhxEbwGs_tx-M";

    private static Credential authorize() throws IOException, GeneralSecurityException {

        InputStream in = GoogleSheetAPI.class.getResourceAsStream("/plzsavemytimebot-04be3dd7b70c.json");
        if (in == null) throw new NullPointerException("Файл не найден!");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new InputStreamReader(in));

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("C:\\Projects\\SaveMyTimeBot\\plzSaveMyTimeBot")))
                .setAccessType("offline")
                .build();

        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver())
                .authorize("gavrplay@gmail.com");

        return credential;
    }

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


/*
    public static void main(String[] args) throws IOException, GeneralSecurityException{
       // sheetsService = getSheetsService();
       // writeToSheet(43, "C",
           //     999);
        getCellValue();
    }
 */







    /*
    Write data to sheet
    // todayRow - параметр отвечающий за текущий день
    // subjectOfLearning - параметр отвечающий за тему, на которое было потрачено время,
    // должен получать букву одного из стобцов
    amountOfMinutes - параметр отвечающий за количество минут, которые передает пользователь
     */
    public static void writeToSheet(int todayRow, String subjectOfLearning, int amountOfMinutes)
            throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();
        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(Arrays.asList(amountOfMinutes)));
        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, subjectOfLearning + todayRow, body)
                .setValueInputOption("RAW")
                .execute();
    }


    // Возвращает значение ячейки из гугл таблицы
    public static Integer getCellValue(String subjectOfLearning, int todayRow) throws IOException, GeneralSecurityException {
        // The ID of the spreadsheet to retrieve data from.
        String spreadsheetId = SPREADSHEET_ID;

        // The A1 notation of the values to retrieve.
        String range = subjectOfLearning + todayRow;

        // How values should be represented in the output.
        // The default render option is ValueRenderOption.FORMATTED_VALUE.
        String valueRenderOption = "FORMATTED_VALUE";

        sheetsService = getSheetsService();
        Sheets.Spreadsheets.Values.Get request =
                sheetsService.spreadsheets().values().get(spreadsheetId, range);
        request.setValueRenderOption(valueRenderOption);

        ValueRange response = request.execute();

        //Значение полученное от гугла, которое содержит лишнюю информацию
        String trash = response.toString();
        //Пробуем получить из строки полной мусора чистое число
        try {
            char[] divideTrash = trash.toCharArray();
            String clearNumberFromTrash = "";

            int t = 0;
            for (int i = 0; i < divideTrash.length; i++) {
                if (divideTrash[i] == '[') {
                    if (divideTrash[i+1] == '"') {
                        t = i + 2;
                        while ( Data.getData().isNumeric(divideTrash[t])) {
                            clearNumberFromTrash += divideTrash[t];
                            t++;
                        }
                    }
                }

            }
            return Integer.parseInt(clearNumberFromTrash);
        }
        catch (NumberFormatException e) {
            return 0;
        }

    }







    // Create new sheet
   /* public static void createSheet() throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties()
                        .setTitle("title"));
        spreadsheet = sheetsService.spreadsheets().create(spreadsheet)
                .setFields("spreadsheetId")
                .execute();
        System.out.println("Spreadsheet ID: " + spreadsheet.getSpreadsheetId());
    }
    */
}