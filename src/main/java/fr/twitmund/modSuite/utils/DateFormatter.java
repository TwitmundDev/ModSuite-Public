package fr.twitmund.modSuite.utils;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    /**
     * Format a date from milliseconds to a string in the format "dd MM yyyy"
     * @param millis The date in milliseconds
     * @return The formatted date string
     */
    public static String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
        Date date = new Date(millis);
        return sdf.format(date);
    }

}