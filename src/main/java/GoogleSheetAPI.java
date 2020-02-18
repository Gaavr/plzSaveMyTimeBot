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

    private static GoogleSheetAPI googleSheetAPI = null;
    public static GoogleSheetAPI getGoogleSheetAPI() {
        if (googleSheetAPI == null) {
            googleSheetAPI = new GoogleSheetAPI();
        }
        return googleSheetAPI;
    }



    public static Sheets sheetsService;
    private static String APPLICATION_NAME = "plzSaveMyTimeBot";
    private static String SPREADSHEET_ID = "";

    private static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = GoogleSheetAPI.class.getResourceAsStream("credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new InputStreamReader(in));

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("C:\\Users\\Gavrp" +
                        "\\Desktop\\Проекты\\plzSaveMyTimeBot")))
                .setAccessType("offline")
                .build();

        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver())
                .authorize("");

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
    public static void writeToSheet(int todayRow, String subjectOfLearning, int amountOfMinutes) throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();
        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(Arrays.asList(amountOfMinutes)));
        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, subjectOfLearning + todayRow, body)
                .setValueInputOption("RAW")
                .execute();
    }



    public String getCellValue(String subjectOfLearning, int todayRow) throws IOException, GeneralSecurityException {
        // The ID of the spreadsheet to retrieve data from.
        String spreadsheetId = ""; // TODO: Update placeholder value.

        // The A1 notation of the values to retrieve.
        String range = subjectOfLearning + todayRow; // TODO: Update placeholder value.

        // How values should be represented in the output.
        // The default render option is ValueRenderOption.FORMATTED_VALUE.
        String valueRenderOption = "FORMATTED_VALUE"; // TODO: Update placeholder value.

        // How dates, times, and durations should be represented in the output.
        // This is ignored if value_render_option is
        // FORMATTED_VALUE.
        // The default dateTime render option is [DateTimeRenderOption.SERIAL_NUMBER].
        String dateTimeRenderOption = null; // TODO: Update placeholder value.

        sheetsService = getSheetsService();
        Sheets.Spreadsheets.Values.Get request =
                sheetsService.spreadsheets().values().get(spreadsheetId, range);
        request.setValueRenderOption(valueRenderOption);
       // request.setDateTimeRenderOption(dateTimeRenderOption);

        ValueRange response = request.execute();

        // TODO: Change code below to process the `response` object:
        //System.out.println(response).;
        return response.toString();
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
