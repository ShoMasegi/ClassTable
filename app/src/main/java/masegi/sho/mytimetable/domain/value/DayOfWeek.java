package masegi.sho.mytimetable.domain.value;

import android.support.annotation.NonNull;

/**
 * Created by masegi on 2017/06/16.
 */

public enum DayOfWeek {
    SUN("Sunday"),
    MON("Monday"),
    TUE("Tuesday"),
    WED("Wednesday"),
    THU("Thursday"),
    FRI("Friday"),
    SAT("Saturday");

    private String name;
    private static DayOfWeek[] dayOfWeek = DayOfWeek.values();

    DayOfWeek(String name){
        this.name = name;
    }

    public String getWeekName(){
        return name;
    }

    public static DayOfWeek getWeekByOrdinal(int ordinal){
        return dayOfWeek[ordinal];
    }

    public static DayOfWeek getWeek(@NonNull String name){
        String[] weekName = new String[7];
        for(int i=0;i<dayOfWeek.length;i++){
            if(dayOfWeek[i].getWeekName().equals(name)){
                return dayOfWeek[i];
            }
        }
        return null;
    }

    public static String[] getWeekString() {
        String[] weekName = new String[7];
        // limit is dayOfWeek.length -1 because remove NULL
        for (int i =0; i < dayOfWeek.length; i++) {
            weekName[i] = dayOfWeek[i].getWeekName();
        }
        return weekName;
    }

}
