import java.util.ArrayList;
import java.util.Calendar;

public class Data {

    private static Data data = null;
    public static Data getData() {
        if (data == null) {
            data = new Data();
        }
        return data;
    }


    private Integer numberOfMinutes;

    public Integer getNumberOfMinutes() {
        return numberOfMinutes;
    }

    public void setNumberOfMinutes(Integer numberOfMinutes) {
            this.numberOfMinutes = numberOfMinutes;
    }


    private String subject = "C";

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public int getCellID() {
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        return dayOfYear - 4;
    }

    public int getNumberFromTrash(String getCellValue) {
        String trash = getCellValue;

       String clearNumberFromTrash = "";

        char[] divideTrash = trash.toCharArray();
        int t = 0;
        for (int i = 0; i < divideTrash.length; i++) {
            if (divideTrash[i] == '[') {
                if (divideTrash[i+1] == '"') {
                    t = i + 2;
                    while (isNumeric(divideTrash[t])) {
                        clearNumberFromTrash += divideTrash[t];
                        t++;
                    }
                }
            }

        }
        return Integer.parseInt(clearNumberFromTrash);
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public boolean isNumeric(char ch) {
        try {
            double d = Double.parseDouble(String.valueOf(ch));
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

}
