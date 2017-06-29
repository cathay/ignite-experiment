package com.mstage.ignite.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    private static final String [] DATE_FORMAT = {
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd HH:mm:ss.SSS'Z'"
    };

    private DateTimeUtils() {

    }

    public static Date parseDate(String date) {
        int i = 0;
        while (i < DATE_FORMAT.length) {
            try {
                return parseDate(date,i);
            } catch (ParseException e) {
                i++;
            }
        }
        return new Date(0);
    }

    private static Date parseDate(String date, int dateFormatIndex) throws ParseException {
        if(dateFormatIndex ==  DATE_FORMAT.length)
            return new Date(0);

        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT[dateFormatIndex]);
        return sdfDate.parse(date);
    }
}