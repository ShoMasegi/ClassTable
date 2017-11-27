package masegi.sho.mytimetable.view.fragment;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import masegi.sho.mytimetable.R;
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

    @BindView(R.id.square_view ) View colorView;
    @BindView(R.id.edit_classname) EditText classNameEditView;
    @BindView(R.id.edit_teacher_name) EditText teacherNameEditView;
    @BindView(R.id.edit_room_name) EditText roomNameEditView;
    @BindView(R.id.day_of_week) Spinner weekSpinner;
    @BindView(R.id.spinner_time) Spinner startSpinner;
    @BindView(R.id.spinner_section) Spinner sectionSpinner;
    @BindView(R.id.times_attend) TextView attView;
    @BindView(R.id.times_late) TextView lateView;
    @BindView(R.id.times_abs) TextView absView;
    @BindView(R.id.save_btn) Button saveBtn;
    @BindView(R.id.cancel_btn) Button cancelBtn;

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

        View root = inflater.inflate(R.layout.frag_edit,container,false);
        ButterKnife.bind(this,root);

        setupView();
        return root;
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
    public void prepareData(ClassObject classObject) {
        this.object = classObject;
    }

    @Override
    public void savedClassObject() {
        getActivity().finish();
    }

    @Override
    public void canceled() {
        getActivity().finish();
    }

    private void setupView(){

        setEditTextViewsValue();
        setSpinners();
        setAttendanceStateValue();
        setOnCLickListener();
        setColorView(colorView, object.getThemeColor());
    }

    private void setEditTextViewsValue() {

        if (object != null) {

            if (object.getClassName() != null) classNameEditView.setText(object.getClassName());
            if (object.getRoomName() != null) roomNameEditView.setText(object.getRoomName());
            if (object.getTeacherName() != null)
                teacherNameEditView.setText(object.getTeacherName());
        }
    }

    private void setSpinners(){

        ClassTablePreference preference = ClassTablePreference.getInstance();
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item);
        for(DayOfWeek week: preference.getDaysOfWeek()) {

            adapter.add(week.getWeekName());
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSpinner.setAdapter(adapter);

        ArrayAdapter<Integer> intAdapter = new ArrayAdapter<Integer>(getContext(),android.R.layout.simple_spinner_item);
        for(int i = 1; i <= preference.getCountOfSection(); i++) intAdapter.add(i);
        intAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpinner.setAdapter(intAdapter);
        sectionSpinner.setAdapter(intAdapter);

        if (object == null) return;

        String week = object.getWeek().getWeekName();
        weekSpinner.setSelection(adapter.getPosition(week));
        int number = object.getStart();
        if (number>0) startSpinner.setSelection(intAdapter.getPosition(number));
        number = object.getSection();
        if (number>0) sectionSpinner.setSelection(intAdapter.getPosition(number));
    }

    private void setAttendanceStateValue() {
        if (object != null) {
            attView.setText(String.valueOf(object.getAtt()));
            lateView.setText(String.valueOf(object.getLate()));
            absView.setText(String.valueOf(object.getAbs()));
        }
    }

    private void setOnCLickListener(){

        attView.setOnLongClickListener(new OnMyLongClickListener("Attend",attView));
        lateView.setOnLongClickListener(new OnMyLongClickListener("Late",lateView));
        absView.setOnLongClickListener(new OnMyLongClickListener("Absent",absView));
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editPresenter.saveClassObject(outputToClassObject(object));
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editPresenter.cancelBtnClick();
            }
        });
        colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createColorPickerDialog(ThemeColor.getColorResIdArray());
            }
        });
    }

    private void setColorView(View view, ThemeColor themeColor) {

        int colorResId = themeColor.getPrimaryColorResId();
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int scale = (int)(getResources().getDisplayMetrics().density*1.5f);
        drawable.setStroke(scale,ContextCompat.getColor(getContext(),R.color.colorBorder));
        drawable.setColor(ContextCompat.getColor(getContext(),colorResId));
        view.setBackground(drawable);
    }


    private void createColorPickerDialog(ArrayList colorList){

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
                    setColorView(colorView, themeColor);
                }
            }

            @Override
            public void onDismiss() {
            }
        });
        colorPickerDialog.show(getChildFragmentManager(),"coloPickerFragment");
    }


    private ClassObject outputToClassObject(ClassObject co){

        co.setClassName(classNameEditView.getText().toString());
        co.setTeacherName(teacherNameEditView.getText().toString());
        co.setRoomName(roomNameEditView.getText().toString());
        co.setWeek(DayOfWeek.getWeek(weekSpinner.getSelectedItem().toString()));
        co.setStart(Integer.parseInt(startSpinner.getSelectedItem().toString()));
        co.setSection(Integer.parseInt(sectionSpinner.getSelectedItem().toString()));
        co.setAtt(Integer.parseInt(attView.getText().toString()));
        co.setLate(Integer.parseInt(lateView.getText().toString()));
        co.setAbs(Integer.parseInt(absView.getText().toString()));
        return co;
    }

    private class OnMyLongClickListener implements View.OnLongClickListener,
            NumberPickerDialogFragment.NumberPickerCallback{

        private String title;
        private TextView textView;

        OnMyLongClickListener(String title,TextView textView){
            this.title = title;
            this.textView = textView;
        }

        @Override
        public boolean onLongClick(View view) {

            NumberPickerDialogFragment dialogFragment
                    = new NumberPickerDialogFragment().createDialog(this, title + " times", 0, 15, 0);
            dialogFragment.show(getChildFragmentManager(),title + "_picker");
            return true;
        }

        @Override
        public void callback(int number) {
            textView.setText(String.valueOf(number));
        }
    }

}
