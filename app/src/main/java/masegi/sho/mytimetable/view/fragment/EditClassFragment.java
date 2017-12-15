package masegi.sho.mytimetable.view.fragment;


import android.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.databinding.FragEditBinding;
import masegi.sho.mytimetable.di.contract.EditClassContract;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.entity.ErrorMessageEntity;
import masegi.sho.mytimetable.domain.value.DayOfWeek;
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.preferences.ClassTablePreference;
import masegi.sho.mytimetable.view.ColorPickerDialog.ColorPickerDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditClassFragment extends Fragment implements EditClassContract.Views{

    private enum AttendType {

        ATTEND, LATE, ABSENT
    }

    private FragEditBinding binding;

    private EditClassContract.Presenter editPresenter;
    private ClassObject object;

    public EditClassFragment() {
        // Required empty public constructor
    }

    public static EditClassFragment newInstance() {

        EditClassFragment fragment = new EditClassFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.frag_edit, container, false);
        View root = binding.getRoot();
        binding.setObject(this.object);
        binding.setPresenter(this.editPresenter);
        setupView();
        return root;
    }

    private void setupView() {

        binding.attendTextView.setTag(AttendType.ATTEND);
        binding.lateTextView.setTag(AttendType.LATE);
        binding.absentTextView.setTag(AttendType.ABSENT);
        setSpinners();
        setColorView(binding.colorView, object.getThemeColor());
    }

    private void setSpinners() {

        ClassTablePreference preference = ClassTablePreference.getInstance();
        final ArrayAdapter<String> adapter =
                new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        for(DayOfWeek week : preference.getDaysOfWeek()) {

            adapter.add(week.getWeekName());
        }
        binding.weekSpinner.setAdapter(adapter);

        final ArrayAdapter<Integer> intAdapter
                = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);
        for(int i = 1; i <= preference.getCountOfClasses(); i++) {

            intAdapter.add(i);
        }
        binding.startTimeSpinner.setAdapter(intAdapter);
        binding.sectionSpinner.setAdapter(intAdapter);
    }

    private void setColorView(View view, ThemeColor themeColor) {

        int colorResId = themeColor.getPrimaryColorResId();
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int scale = (int)(getResources().getDisplayMetrics().density * 1.0f);
        drawable.setStroke(scale,ContextCompat.getColor(getContext(), R.color.colorBorder));
        drawable.setColor(ContextCompat.getColor(getContext(), colorResId));
        view.setBackground(drawable);
    }


    @Override
    public void setPresenter(@NonNull EditClassContract.Presenter presenter) {

        this.editPresenter = presenter;
    }

    @Override
    public void showError(int msgNum) {

        String errorMsg;
        switch (msgNum) {
            case ErrorMessageEntity.NO_CLASSNAME:
                errorMsg = getContext().getString(R.string.error_no_classname);
                break;
            case ErrorMessageEntity.OVERFLOW:
                errorMsg = getContext().getString(R.string.error_overflow);
                break;
            case ErrorMessageEntity.ALREADY_EXIST:
                errorMsg = getContext().getString(R.string.error_already_exist);
                break;
            case ErrorMessageEntity.CLASSNAME_USED:
                errorMsg = getContext().getString(R.string.error_classname_used);
                break;
            default:
                errorMsg = getContext().getString(R.string.error_default);
                break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error")
                .setPositiveButton("OK", null)
                .setMessage(errorMsg)
                .create().show();
    }

    @Override
    public void showNumberPickerDialog(final View view, final ClassObject object) {

        String title = "";
        int defValue = 0;
        NumberPickerDialogFragment.NumberPickerCallback callback = null;

        switch ((AttendType)view.getTag()) {

            case ATTEND:
                title = "Attend";
                defValue = object.getAtt();
                callback = new NumberPickerDialogFragment.NumberPickerCallback() {
                    @Override
                    public void callback(int number) {

                        object.setAtt(number);
                    }
                };
                break;
            case LATE:
                title = "Late";
                defValue = object.getLate();
                callback = new NumberPickerDialogFragment.NumberPickerCallback() {
                    @Override
                    public void callback(int number) {

                        object.setLate(number);
                    }
                };
                break;
            case ABSENT:
                title = "Absent";
                defValue = object.getAbs();
                callback = new NumberPickerDialogFragment.NumberPickerCallback() {
                    @Override
                    public void callback(int number) {

                        object.setAbs(number);
                    }
                };
                break;
        }
        title += " times";

        NumberPickerDialogFragment dialogFragment
                = new NumberPickerDialogFragment().createDialog(callback, title, 0, 15, defValue);

        dialogFragment.show(getChildFragmentManager(), title);
    }

    @Override
    public void setData(ClassObject classObject) {
        this.object = classObject;
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void showColorPickerDialog() {

        ArrayList colorList = ThemeColor.getColorResIdArray();
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstance(
                ColorPickerDialog.SELECTION_SINGLE,
                        colorList,
                        4,
                        ColorPickerDialog.SIZE_SMALL
                );
        colorPickerDialog.setOnDialogButtonListener(new ColorPickerDialog.OnDialogButtonListener() {
            @Override
            public void onDonePressed(ArrayList<Integer> mSelectedColors) {

                if (mSelectedColors.size() > 0) {

                    int colorResId = mSelectedColors.get(0);
                    ThemeColor themeColor = ThemeColor.getThemeColorByColorResId(colorResId);
                    object.setThemeColor(themeColor);
                    setColorView(binding.colorView, themeColor);
                }
            }

            @Override
            public void onDismiss() { }
        });
        colorPickerDialog.show(getChildFragmentManager(),"coloPickerFragment");
    }
}
