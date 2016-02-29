package com.unica.so2.enotesrecorder.Helper;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GenericHelper {
    private static final String TAG = "GenericHelper";

    /**
     * Convert the string, passed as parameter, into a Date.
     *
     *  @return Date the date converted, throws an exception otherwise
     */
    public static Date stringToDate(String date) {
        SimpleDateFormat dateFormat =  new SimpleDateFormat("dd MM dd yyyy - HH:mm:ss", Locale.ITALY);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return myDate;
    }
}
