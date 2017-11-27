package masegi.sho.mytimetable.view.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.domain.value.ClassTime;

/**
 * Created by masegi on 2017/10/31.
 */

public class TimePickerFragment extends DialogFragment {

    private GetTimeCallback callback;
    private SetTimeCallback startCallback;
    private SetTimeCallback endCallback;

    private ClassTime time;

    public TimePickerFragment() {}

    public TimePickerFragment setTime(ClassTime time) {

        this.time = time;
        return this;
    }

    public TimePickerFragment setCallback(GetTimeCallback callback) {

        this.callback = callback;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (time == null) time = new ClassTime(0);
        TimePickerDialog.OnTimeSetListener startListener
                = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                time.setStartHour(hourOfDay);
                time.setStartMin(minute);
                startCallback.setTimeCallback();
            }
        };
        TimePickerDialog.OnTimeSetListener endListener
                = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                time.setEndHour(hourOfDay);
                time.setEndMin(minute);
                endCallback.setTimeCallback();
                if (callback != null) {
                    callback.onGetClassTime(time);
                }
            }
        };
        final TimePickerDialog startTimePickerDialog = new TimePickerDialog(
                getContext(),
                R.style.DialogTheme,
                startListener,
                time.getStartHour(),
                time.getStartMin(),
                true
        );
        startTimePickerDialog.setTitle("Start");
        final TimePickerDialog endTimePickerDialog = new TimePickerDialog(
                getContext(),
                R.style.DialogTheme,
                endListener,
                time.getEndHour(),
                time.getEndMin(),
                true
        );
        endTimePickerDialog.setTitle("End");
        startCallback = new SetTimeCallback() {
            @Override
            public void setTimeCallback() {
                startTimePickerDialog.hide();
                endTimePickerDialog.show();
            }
        };
        endCallback = new SetTimeCallback() {
            @Override
            public void setTimeCallback() {
                endTimePickerDialog.hide();
                startTimePickerDialog.dismiss();
                endTimePickerDialog.dismiss();
            }
        };
        return startTimePickerDialog;
    }

    public interface GetTimeCallback {
        void onGetClassTime(ClassTime time);
    }

    public interface SetTimeCallback {
        void setTimeCallback();
    }
}
