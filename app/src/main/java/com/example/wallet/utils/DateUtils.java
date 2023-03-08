package com.example.wallet.utils;

import android.util.Log;
import android.widget.TextView;

import com.example.wallet.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class DateUtils {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static List<Date> sortDates(HashMap<String, Double> dateMap){

        List<Date> dateList = new ArrayList<>();

        dateMap.forEach((s, aDouble) -> {
            try {
                dateList.add(simpleDateFormat.parse(s));
            } catch (ParseException e) {
                Log.e("AAA", "Ошибка при парсинге даты");
            }
        });

        for (int i = dateList.size() - 1; i > 0; i--){
            for (int j = 0; j < i; j++){
                if (dateList.get(j).compareTo(dateList.get(j + 1)) < 0){
                    Date temp = dateList.get(j);
                    dateList.set(j, dateList.get(j + 1));
                    dateList.set(j + 1, temp);
                }
            }
        }

        return dateList;
    }

    public static String getDateString(Date date){
        String result;

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);



        String months[] = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        String currentMonth = months[calendar.get(Calendar.MONTH)];

        result = calendar.get(Calendar.DAY_OF_MONTH) + " " + currentMonth + " " + calendar.get(Calendar.YEAR) + " г.";

        return result;
    }
}
