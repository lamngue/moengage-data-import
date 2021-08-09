package com.cmc.moengagedataimport.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static String getAgeFromBirthday(String strFormat, String birthday) {
        DateFormat dateFormat = new SimpleDateFormat(strFormat);
        Date birthDate = null;
        try {
            birthDate = dateFormat.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(birthDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        LocalDate l1 = LocalDate.of(year, month, date);
        LocalDate present = LocalDate.now();
        Period age = Period.between(l1, present);
        return String.valueOf(age.getYears());
    }
}
