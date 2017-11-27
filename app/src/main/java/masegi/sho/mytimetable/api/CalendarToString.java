package masegi.sho.mytimetable.api;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by masegi on 2017/07/07.
 */

public class CalendarToString {


    private static final String DATABASE_CALENDAR_FORMAT = "yyyyMMddHHmmss";
    private static final String DUE_DATE_FORMAT = "EEE, dd MMM,HH:mm";
    private static final String CREATE_DATE_FORMAT = "EEE,dd MMM";
    private static final String SIMPLE_DATE_FORMAT = " MMM";
    private static final String SIMPLE_WEEK_FORMAR = "EEEE ";
    private static final String SIMPLE_TIME_FORMAT = "HH:mm";

    public static String calendarToString(Calendar calendar){
        Locale en = new Locale("en");
        SimpleDateFormat database_sdf = new SimpleDateFormat(DATABASE_CALENDAR_FORMAT,en);
        return database_sdf.format(calendar.getTime());
    }

    public static Calendar stringToCalendar(String stringDate){
        Locale en = new Locale("en");
        SimpleDateFormat dueDate_sdf = new SimpleDateFormat(DATABASE_CALENDAR_FORMAT,en);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        Date date = calendar.getTime();
        try {
            date = dueDate_sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        return calendar;
    }

    public static String calendarToDueDate(Calendar dueDate){
        Locale en = new Locale("en");
        SimpleDateFormat dueDate_sdf = new SimpleDateFormat(DUE_DATE_FORMAT,en);
        return dueDate_sdf.format(dueDate.getTime());
    }

    public static String calendarToCreateDate(Calendar createDate){
        Locale en = new Locale("en");
        SimpleDateFormat createDate_sdf = new SimpleDateFormat(CREATE_DATE_FORMAT,en);
        return createDate_sdf.format(createDate.getTime());
    }

    public static String calendarToSimpleDate(Calendar calendar){
        Locale en = new Locale("en");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT,en);
        int dayOfmonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfmonth+getDayOfMonthSuffix(dayOfmonth)+simpleDateFormat.format(calendar.getTime());
    }

    public static String calendarToTodoDate(Calendar calendar){
        Locale en = new Locale("en");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_WEEK_FORMAR,en);
        return simpleDateFormat.format(calendar.getTime()) + calendarToSimpleDate(calendar);
    }

    public static String calendarToSimpleTime(Calendar calendar){
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(SIMPLE_TIME_FORMAT);
        return simpleTimeFormat.format(calendar.getTime());
    }

    public static String getDayOfMonthSuffix(final int n){
        if(n >= 11 && n<= 13) return "th";
        switch (n % 10){
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default:return "th";
        }
    }
}
