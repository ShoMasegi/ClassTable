package masegi.sho.mytimetable.view.helper;

import android.databinding.BindingAdapter;
import android.widget.CompoundButton;

import masegi.sho.mytimetable.view.customview.SettingSwitchRowView;

/**
 * Created by masegi on 2017/09/16.
 */

public class DataBindingHelper {

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
}
