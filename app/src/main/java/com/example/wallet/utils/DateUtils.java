package com.example.wallet.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
}
