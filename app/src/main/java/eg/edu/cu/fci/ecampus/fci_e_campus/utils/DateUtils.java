package eg.edu.cu.fci.ecampus.fci_e_campus.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ahmed on 6/28/2018.
 */

public final class DateUtils {
    public static Date convert(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return formatter.parse(dateString);
    }

    public static Date convertSlot(String startTimeString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("k:mm:ss");
        return formatter.parse(startTimeString);
    }

    public static String convertSlot(Date startTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(startTime);
    }

    public static String convert(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d");
        return formatter.format(date);
    }

    public static String convertForumPost(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM");
        return formatter.format(date);
    }

    public static String convertPostComment(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM, h:mm a");
        return formatter.format(date);
    }

    public static String convertCreatedDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String convertDueDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        return formatter.format(date);
    }

    public static String getCurrentTime(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return formatter.format(date);
    }
}
