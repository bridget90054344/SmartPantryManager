package com.example.smartpantry.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

public class ExpiryChecker {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final int SOON_THRESHOLD_DAYS = 3;

    public enum Status{
        NONE,
        OK,
        EXPIRING_SOON,
        EXPIRED
    }

    public static Status check(String expiryDate){
        if(expiryDate == null || expiryDate.trim().isEmpty()){
            return Status.NONE;
        }

        Date expiry = parse(expiryDate.trim());
        if(expiry == null){
            return Status.NONE;
        }

        Date today = stripTime(new Date());
        long millisPerDay = 24L * 60 * 60 * 1000;
        long daysUntilExpiry = (expiry.getTime() - today.getTime())/millisPerDay;

        if(daysUntilExpiry < 0) return Status.EXPIRED;
        if(daysUntilExpiry <= SOON_THRESHOLD_DAYS) return Status.EXPIRING_SOON;
        return Status.OK;
    }

    private static Date parse(String value){
        try{
            SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN,Locale.US);
            format.setLenient(false);
            return stripTime(format.parse(value));
        }catch(ParseException e){
            return null;
        }
    }

    private static Date stripTime(Date date){
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.set(Calendar.HOUR_OF_DAY,0);
        calender.set(Calendar.MINUTE,0);
        calender.set(Calendar.SECOND,0);
        calender.set(Calendar.MILLISECOND,0);
        return calender.getTime();
    }
}
