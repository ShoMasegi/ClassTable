package masegi.sho.mytimetable.util;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import java.util.ArrayList;

import masegi.sho.mytimetable.view.customview.SettingSwitchRowView;

/**
 * Created by masegi on 2017/09/16.
 */

public class DataBindingUtil {

    @BindingAdapter("settingEnabled")
    public static void setSettingEnabled(SettingSwitchRowView view, boolean enabled) {
        view.setEnabled(enabled);
    }

    @BindingAdapter("settingDefaultValue")
    public static void setSettingDefaultValue(SettingSwitchRowView view, boolean defaultValue){
        view.setDefault(defaultValue);
    }

    @BindingAdapter("settingOnCheckedChanged")
    public static void setSettingOnCheckedChanged(
            SettingSwitchRowView view,
            CompoundButton.OnCheckedChangeListener listener) {
        view.setOnCheckedChangeListener(listener);
    }


    @BindingAdapter(value = {"selectedValue", "selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(AppCompatSpinner spinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                newTextAttrChanged.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        if (newSelectedValue != null) {

            int position = ((ArrayAdapter<String>) spinner.getAdapter()).getPosition(newSelectedValue);
            spinner.setSelection(position, true);
        }
    }

    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    public static String captureSelectedValue(AppCompatSpinner spinner) {

        return (String) spinner.getSelectedItem();
    }

    @BindingAdapter(value = {"selectedIntValue", "selectedIntValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerIntData(AppCompatSpinner spinner, int newSelectedValue, final InverseBindingListener newIntAttrChanged) {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                newIntAttrChanged.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        int position = ((ArrayAdapter<Integer>) spinner.getAdapter()).getPosition(newSelectedValue);
        spinner.setSelection(position, true);
    }

    @InverseBindingAdapter(attribute = "selectedIntValue", event = "selectedIntValueAttrChanged")
    public static int captureSelectedIntValue(AppCompatSpinner spinner) {

        return (int) spinner.getSelectedItem();
    }
}
