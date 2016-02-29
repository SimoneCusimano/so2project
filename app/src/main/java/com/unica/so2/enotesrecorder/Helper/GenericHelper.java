package com.unica.so2.enotesrecorder.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GenericHelper {
    public static String DATE_TIME_FORMAT = "dd MM dd yyyy - HH:mm:ss";

    /**
     * Convert the string, passed as parameter, into a Date.
     *
     *  @return Date the date converted, throws an exception otherwise
     */
    public static Date stringToDate(String date) {
        SimpleDateFormat dateFormat =  new SimpleDateFormat(DATE_TIME_FORMAT, Locale.ITALY);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return myDate;
    }

    public static String getStringFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.ITALY);
        return dateFormat.format(date);
    }
}
