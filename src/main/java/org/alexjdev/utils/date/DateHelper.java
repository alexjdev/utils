package org.alexjdev.utils.date;

import java.util.Calendar;
import java.util.Date;

/**
 * Helper for process date
 */
public class DateHelper {
    /**
     * Выставляет дату либо к началу суток, либо к концу суток
     *
     * @param date          исходная дата
     * @param setToStartDay Если true, то дата приведется к началу суток, иначе к 23:59:59
     */
    public static Date setTimeIntoDate(Date date, boolean setToStartDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (setToStartDay) {
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        }
        return calendar.getTime();
    }

    /**
     * Увеличивает дату на один день
     *
     * @param date текущая дата
     * @return дата следующего дня
     */
    public static Date addOneDayToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * Получение года из даты
     *
     * @param date исходная дата
     * @return год
     */
    public static Integer getYearFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static Integer daysBetween(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return 0;
        }
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

}
