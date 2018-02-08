package masegi.sho.mytimetable.domain.value;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Calendar;

import masegi.sho.mytimetable.BR;
import masegi.sho.mytimetable.Utils.CalendarUtil;
import masegi.sho.mytimetable.Utils.OrdinalNumberUtil;

/**
 * Created by masegi on 2017/10/20.
 */

public class ClassTime extends BaseObservable {

    private String period;
    private String startTime;
    private String endTime;
    private int periodNum;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;


    public ClassTime(int periodNum, Calendar start, Calendar end) {

        this.periodNum = periodNum;
        if (start != null) {

            this.startHour = start.get(Calendar.HOUR_OF_DAY);
            this.startMin = start.get(Calendar.MINUTE);
        }
        if (end != null) {

            this.endHour = end.get(Calendar.HOUR_OF_DAY);
            this.endMin = end.get(Calendar.MINUTE);
        }
    }

    public ClassTime(int periodNum, int startHour, int startMin, int endHour, int endMin) {

        this.periodNum = periodNum;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
    }

    public ClassTime(int periodNum) {

        this(periodNum, null, null);
    }

    @Bindable
    public String getStartTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, startHour);
        calendar.set(Calendar.MINUTE, startMin);
        return CalendarUtil.calendarToSimpleTime(calendar);
    }

    @Bindable
    public String getEndTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, endHour);
        calendar.set(Calendar.MINUTE, endMin);
        return CalendarUtil.calendarToSimpleTime(calendar);
    }

    @Bindable
    public String getPeriod() {

        String period = OrdinalNumberUtil.ordinalNumberString(periodNum);
        return period + " Period";
    }

    public int getPeriodNum() { return this.periodNum; }
    public int getStartHour() { return this.startHour; }
    public int getStartMin() { return this.startMin; }
    public int getEndHour() { return this.endHour; }
    public int getEndMin() { return this.endMin; }

    public void setStartHour(int hour) {

        this.startHour = hour;
        notifyPropertyChanged(BR.startTime);
    }

    public void setStartMin(int minute) {

        this.startMin = minute;
        notifyPropertyChanged(BR.startTime);
    }

    public void setEndHour(int hour) {

        this.endHour = hour;
        notifyPropertyChanged(BR.endTime);
    }

    public void setEndMin(int minute) {

        this.endMin = minute;
        notifyPropertyChanged(BR.endTime);
    }
}
