package masegi.sho.mytimetable.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.databinding.ViewSettingSwitchRowBinding;

/**
 * Created by masegi on 2017/09/13.
 */

public class SettingSwitchRowView extends RelativeLayout {

    private ViewSettingSwitchRowBinding binding;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    public SettingSwitchRowView(Context context) {
        this(context,null);
    }

    public SettingSwitchRowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingSwitchRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            inflate(context, R.layout.view_setting_switch_row, this);
            return ;
        }

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_setting_switch_row, this, true);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SettingSwitchRowView);

        String title = a.getString(R.styleable.SettingSwitchRowView_settingTitle);
        String description = a.getString(R.styleable.SettingSwitchRowView_settingDescription);

        binding.settingSwitchTitle.setText(title);
        binding.settingSwitchDescription.setText(description);

        binding.getRoot().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        binding.settingSwitchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
                }
            }
        });

        a.recycle();
    }

    private void toggle(){
        boolean isChecked = binding.settingSwitchSwitch.isChecked();
        binding.settingSwitchSwitch.setChecked(!isChecked);
    }

    public void setChecked(boolean checked) {
        binding.settingSwitchSwitch.setChecked(checked);
    }

    public void setDefault(boolean defaultValue) {
        binding.settingSwitchSwitch.setChecked(defaultValue);
    }

    public void init(CompoundButton.OnCheckedChangeListener listener) {
        setOnCheckedChangeListener(listener);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        binding.getRoot().setEnabled(enabled);
        binding.settingSwitchSwitch.setEnabled(enabled);
        if (enabled) {
            binding.settingSwitchTitle.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
            binding.settingSwitchDescription.setTextColor(ContextCompat.getColor(getContext(), R.color.sub_char_black));
        } else {
            int disabledTextColor = ContextCompat.getColor(getContext(), R.color.black_alpha_30);
            binding.settingSwitchTitle.setTextColor(disabledTextColor);
            binding.settingSwitchDescription.setTextColor(disabledTextColor);
        }

    }
}
