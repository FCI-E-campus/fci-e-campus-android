package eg.edu.cu.fci.ecampus.fci_e_campus.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ahmed on 6/28/2018.
 */

public final class DateUtils {
    public static Date convert(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return formatter.parse(dateString);
    }

    public static String convert(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d");
        return formatter.format(date);
    }
}
