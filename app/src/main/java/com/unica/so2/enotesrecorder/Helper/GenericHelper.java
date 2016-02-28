package com.unica.so2.enotesrecorder.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GenericHelper {

    public static Date stringToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "MM dd, yyyy");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return myDate;
    }
}
