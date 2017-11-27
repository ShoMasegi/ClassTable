package masegi.sho.mytimetable.view.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.CalendarToString;
import masegi.sho.mytimetable.di.RestoreDataSource;


public class DateTimePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    private GetCalendarCallback callback;
    private SetDateCallback dateCallback;
    private SetTimeCallback timeCallback;
    private Calendar time;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;


    public DateTimePickerFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        time = Calendar.getInstance();
        final Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        final int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        final DatePickerDialog datePickerDialog
                = new DatePickerDialog(getActivity(),R.style.DialogTheme,this,year,month,day);
        final TimePickerDialog timePickerDialog
                = new TimePickerDialog(getActivity(),R.style.DialogTheme,this,hour,minute,true);

        dateCallback = new SetDateCallback() {
            @Override
            public void onSetDateCallback() {
                datePickerDialog.hide();
                timePickerDialog.show();
            }
        };

        timeCallback = new SetTimeCallback() {
            @Override
            public void onSetTimeCallback() {
                timePickerDialog.hide();
                datePickerDialog.dismiss();
                timePickerDialog.dismiss();
            }
        };


        return datePickerDialog;
    }

    public DateTimePickerFragment setCallback(GetCalendarCallback callback){
        this.callback = callback;
        return this;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;
        dateCallback.onSetDateCallback();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        timeCallback.onSetTimeCallback();
        time.set(mYear,mMonth,mDay,mHour,mMinute);
        callback.onGetCalendar(time);
    }


    public interface GetCalendarCallback{
        void onGetCalendar(Calendar calendar);
    }

    public interface SetDateCallback{
        void onSetDateCallback();
    }

    public interface SetTimeCallback{
        void onSetTimeCallback();
    }
}
