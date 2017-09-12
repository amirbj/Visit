package com.andc.slidingmenu.Utility;

/**
 * Created by win on 6/1/2015.
 */

import android.text.format.DateFormat;

import java.util.Calendar;


public class SolarCalendar{


    ConvertorNumberToPersian convertorNumberToPersian;
    private int year;
    private int month;
    private int date;
    private int weekDay;
    private int GMTHour = 0;
    private int GMTMinute = 0;
    private Calendar calendar;


    public SolarCalendar() {
        this.calendar = Calendar.getInstance();
        convertorNumberToPersian = new ConvertorNumberToPersian();
        calSolarCalendar();
    }

    public SolarCalendar(Calendar calendar) {
        this.calendar = calendar;
        calSolarCalendar();
    }

    private void calSolarCalendar() {
        int ld;
        int georgianYear = calendar.get(Calendar.YEAR);
        int georgianMonth = calendar.get(Calendar.MONTH) + 1;
        int georgianDate = calendar.get(Calendar.DATE);
        weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int[] buf1 = new int[]{0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
        int[] buf2 = new int[]{0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};
        if ((georgianYear % 4) != 0) {
            date = buf1[georgianMonth - 1] + georgianDate;
            if (date > 79) {
                date = date - 79;
                if (date <= 186) {
                    switch (date % 31) {
                        case 0:
                            month = date / 31;
                            date = 31;
                            break;
                        default:
                            month = (date / 31) + 1;
                            date = (date % 31);
                            break;
                    }
                    year = georgianYear - 621;
                } else {
                    date = date - 186;
                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 6;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 7;
                            date = (date % 30);
                            break;
                    }
                    year = georgianYear - 621;
                }
            } else {
                if ((georgianYear > 1996) && (georgianYear % 4) == 1) {
                    ld = 11;
                } else {
                    ld = 10;
                }
                date = date + ld;
                switch (date % 30) {
                    case 0:
                        month = (date / 30) + 9;
                        date = 30;
                        break;
                    default:
                        month = (date / 30) + 10;
                        date = (date % 30);
                        break;
                }
                year = georgianYear - 622;
            }
        } else {
            date = buf2[georgianMonth - 1] + georgianDate;
            if (georgianYear >= 1996) {
                ld = 79;
            } else {
                ld = 80;
            }
            if (date > ld) {
                date = date - ld;
                if (date <= 186) {
                    switch (date % 31) {
                        case 0:
                            month = (date / 31);
                            date = 31;
                            break;
                        default:
                            month = (date / 31) + 1;
                            date = (date % 31);
                            break;
                    }
                    year = georgianYear - 621;
                } else {
                    date = date - 186;
                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 6;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 7;
                            date = (date % 30);
                            break;
                    }
                    year = georgianYear - 621;
                }
            } else {
                date = date + 10;
                switch (date % 30) {
                    case 0:
                        month = (date / 30) + 9;
                        date = 30;
                        break;
                    default:
                        month = (date / 30) + 10;
                        date = (date % 30);
                        break;
                }
                year = georgianYear - 622;
            }
        }
    }

    public String getWeekDay() {
        String strWeekDay = "";
        switch (weekDay) {
            case 0:
                strWeekDay = "يکشنبه";
                break;
            case 1:
                strWeekDay = "دوشنبه";
                break;
            case 2:
                strWeekDay = "سه شنبه";
                break;
            case 3:
                strWeekDay = "چهارشنبه";
                break;
            case 4:
                strWeekDay = "پنج شنبه";
                break;
            case 5:
                strWeekDay = "جمعه";
                break;
            case 6:
                strWeekDay = "شنبه";
                break;
        }
        return strWeekDay;
    }

    public String getMonth() {
        String strMonth = "";
        switch (month) {
            case 1:
                strMonth = "فروردين";
                break;
            case 2:
                strMonth = "ارديبهشت";
                break;
            case 3:
                strMonth = "خرداد";
                break;
            case 4:
                strMonth = "تير";
                break;
            case 5:
                strMonth = "مرداد";
                break;
            case 6:
                strMonth = "شهريور";
                break;
            case 7:
                strMonth = "مهر";
                break;
            case 8:
                strMonth = "آبان";
                break;
            case 9:
                strMonth = "آذر";
                break;
            case 10:
                strMonth = "دي";
                break;
            case 11:
                strMonth = "بهمن";
                break;
            case 12:
                strMonth = "اسفند";
                break;
        }
        return strMonth;
    }

    public String getDescribedDateFormat() {
        StringBuilder describedDateFormat = new StringBuilder();
        describedDateFormat.append(getWeekDay())
                .append(" ")
                .append(convertorNumberToPersian.toPersianNumber(String.valueOf(date)))
                .append(" ")
                .append(getMonth())
                .append(" ")
                .append(convertorNumberToPersian.toPersianNumber(String.valueOf(year)))
                .append(" ساعت ")
                .append(" ")
                .append(convertorNumberToPersian.toPersianNumber(String.valueOf(getTime())));

        return String.valueOf(describedDateFormat);
    }

    public String getTime() {
        int h, m, s;
//        s = calendar.get(Calendar.SECOND);
        m = calendar.get(Calendar.MINUTE)+ GMTMinute;
        h = calendar.get(Calendar.HOUR_OF_DAY)+ GMTHour;
        if(m>=60){
            m = m-60;
            h++;
        }

        StringBuilder time = new StringBuilder();
        time.append(h < 10 ? "0" + h : h)
                .append(":")
                .append(m < 10 ? "0" + m : m);
//                .append(":")
//                .append(s < 10 ? "0" + s : s);
        return String.valueOf(time);
    }

    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }


    @Override
    public String toString() {
        return getDescribedDateFormat();
    }

    public String getNumericDateFormat() {
        StringBuilder numericDateFormat = new StringBuilder();
        numericDateFormat.append(String.valueOf(year))
                .append("/")
                .append(String.valueOf(month))
                .append("/")
                .append(String.valueOf(date))
                .append(" ")
                .append(getTime());
        return String.valueOf(numericDateFormat);
    }

/*
    public static void main(String[] args) {
        SolarCalendar solarCalendar = new SolarCalendar();
        System.out.println(solarCalendar.getDescribedDateFormat());
        System.out.println(solarCalendar.getNumericDateFormat());
        System.out.println(solarCalendar.getTime());
        System.out.println(solarCalendar.getMonth());
        System.out.println(solarCalendar.getWeekDay());
        System.out.println(solarCalendar.getTimeInMillis() );
    }
*/

}