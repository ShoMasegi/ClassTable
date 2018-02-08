package masegi.sho.mytimetable.view.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.Utils.CalendarUtil;
import masegi.sho.mytimetable.databinding.FragTodoEditBinding;
import masegi.sho.mytimetable.di.contract.TodoEditContract;
import masegi.sho.mytimetable.domain.value.Task;
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.view.ColorPickerDialog.ColorPickerDialog;

import static masegi.sho.mytimetable.view.activity.TodoEditActivity.TODO_CLASSNAME_KEY;
import static masegi.sho.mytimetable.view.activity.TodoEditActivity.TODO_CREATE_KEY;


public class TodoEditFragment extends Fragment implements TodoEditContract.Views{


    private TodoEditContract.Presenter presenter;

    private Task task;
    private String className;

    private FragTodoEditBinding binding;

    public static final int RESULT_SAVED = 10;
    public static final int RESULT_REMOVED = 20;

    public TodoEditFragment() {
        // Required empty public constructor
    }

    public static TodoEditFragment newInstance() {

        TodoEditFragment fragment = new TodoEditFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        className = getArguments().getString(TODO_CLASSNAME_KEY);
        String createDateString = getArguments().getString(TODO_CREATE_KEY);
        Calendar createDate =
                createDateString != null ? CalendarUtil.stringToCalendar(createDateString) : null;
        presenter.onCreate(className, createDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.frag_todo_edit, container, false);
        View root = binding.getRoot();
        binding.setPresenter(this.presenter);
        binding.setTask(this.task);
        this.setColorView(binding.colorView, task.getThemeColor());
        return root;
    }


    private void setColorView(View view, ThemeColor themeColor) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int scale = (int) (getResources().getDisplayMetrics().density * 1.0f);
        drawable.setStroke(scale, getResources().getColor(themeColor.getPrimaryColorDarkResId()));
        drawable.setColor(ContextCompat.getColor(getContext(), themeColor.getPrimaryColorResId()));
        view.setBackground(drawable);
    }


    @Override
    public void setPresenter(@NonNull TodoEditContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void showDatePicker() {

        DateTimePickerFragment dateTimePickerFragment = new DateTimePickerFragment();
        dateTimePickerFragment.setCallback(new DateTimePickerFragment.GetCalendarCallback() {
            @Override
            public void onGetCalendar(Calendar calendar) {

                task.setDueDate(calendar);
            }
        }).show(getFragmentManager(),"dateTimePickerDialog");
    }

    @Override
    public void showColorPickerDialog() {

        ArrayList colorResIdArray = ThemeColor.getColorResIdArray();
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstance(
                        ColorPickerDialog.SELECTION_SINGLE,
                        colorResIdArray,
                        4,
                        ColorPickerDialog.SIZE_SMALL
                );
        colorPickerDialog.setOnDialogButtonListener(new ColorPickerDialog.OnDialogButtonListener() {
            @Override
            public void onDonePressed(ArrayList<Integer> mSelectedColors) {

                if (mSelectedColors.size() > 0) {

                    ThemeColor themeColor = ThemeColor.getThemeColorByColorResId(mSelectedColors.get(0));
                    task.setThemeColor(themeColor);
                    setColorView(binding.colorView, themeColor);
                }
            }

            @Override
            public void onDismiss() { }
        });
        colorPickerDialog.show(getChildFragmentManager(),"todoEdit_coloPickerFragment");
    }

    @Override
    public void showSnackBar(int messageId) {

        Snackbar.make(binding.parentLayout, getString(messageId), Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void finishTodoActivity(boolean isSaved) {

        if (isSaved) {

            getActivity().setResult(RESULT_SAVED);
        }
        else {

            getActivity().setResult(RESULT_REMOVED);
        }
        getActivity().finish();
    }

    @Override
    public void setTask(Task task) {

        this.task = task;
    }
}
