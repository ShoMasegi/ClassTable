package masegi.sho.mytimetable.view.fragment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.CalendarToString;
import masegi.sho.mytimetable.di.contract.TodoEditContract;
import masegi.sho.mytimetable.domain.value.Task;
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.view.ColorPickerDialog.ColorPickerDialog;

import static masegi.sho.mytimetable.view.activity.TodoEditActivity.TODO_CLASSNAME_KEY;
import static masegi.sho.mytimetable.view.activity.TodoEditActivity.TODO_CREATE_KEY;


public class TodoEditFragment extends Fragment implements TodoEditContract.Views{

    @BindView(R.id.editTodo_parent) CoordinatorLayout layout;
    @BindView(R.id.editTodo_square) View colorView;
    @BindView(R.id.editTodo_sub) TextView todoSub;
    @BindView(R.id.editTodo_taskName_edit) EditText taskNameEdit;
    @BindView(R.id.editTodo_due)TextView dueTextView;
    @BindView(R.id.editTodo_create) TextView createTextView;
    @BindView(R.id.editTodo_edit_text) EditText contentEdit;
    @BindView(R.id.editTodo_due_btn) Button dueDateBtn;
    @BindView(R.id.editTodo_positive_btn) Button positiveBtn;
    @BindView(R.id.editTodo_negative_btn) Button negativeBtn;

    private TodoEditContract.Presenter presenter;

    private Task task;
    private String className;

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
                createDateString != null ? CalendarToString.stringToCalendar(createDateString) : null;
        presenter.onCreate(className, createDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_todo_edit,container,false);
        ButterKnife.bind(this,root);
        setupView();
        return root;
    }


    private void setupView() {

        todoSub.setText(task.getClassName());
        createTextView.setText(CalendarToString.calendarToCreateDate(task.getCreateDate()));
        dueTextView.setText(CalendarToString.calendarToDueDate(task.getDueDate()));
        taskNameEdit.setText(task.getTaskName());
        contentEdit.setText(task.getTaskContent());

        dueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePicker();
            }
        });
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                task.setTaskName(taskNameEdit.getText().toString());
                task.setTaskContent(contentEdit.getText().toString());
                presenter.onSaveButtonClicked(task);
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.onDeleteButtonClicked(task);
            }
        });
        colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showColorPickerDialog();
            }
        });

        if (task.getTaskName() == null) {

            negativeBtn.setVisibility(View.GONE);
        }
        setColorView(colorView, task.getThemeColor());
    }

    private void setColorView(View view, ThemeColor themeColor) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int scale = (int)(getResources().getDisplayMetrics().density*1.5f);
        drawable.setStroke(scale,getResources().getColor(R.color.colorBorder));
        int colorResId = themeColor.getPrimaryColorResId();
        drawable.setColor(ContextCompat.getColor(getContext(),colorResId));
        view.setBackground(drawable);
    }

    private void showColorPickerDialog() {

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
                    setColorView(colorView, themeColor);
                }
            }

            @Override
            public void onDismiss() { }
        });
        colorPickerDialog.show(getChildFragmentManager(),"todoEdit_coloPickerFragment");
    }

    private void showDatePicker() {

        DateTimePickerFragment dateTimePickerFragment
                = new DateTimePickerFragment();
        dateTimePickerFragment.setCallback(new DateTimePickerFragment.GetCalendarCallback() {
            @Override
            public void onGetCalendar(Calendar calendar) {

                task.setDueDate(calendar);
                dueTextView.setText(task.getDueDateString());
            }
        }).show(getFragmentManager(),"dateTimePickerDialog");
    }


    @Override
    public void setPresenter(@NonNull TodoEditContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void showSnackBar(int messageId) {

        Snackbar.make(layout,getString(messageId),Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void finishTodoActivity(boolean isSaved) {

        Intent intent = new Intent();
        if (isSaved) {

            getActivity().setResult(RESULT_SAVED, intent);
        }
        else {

            getActivity().setResult(RESULT_REMOVED, intent);
        }
        getActivity().finish();
    }

    @Override
    public void setTask(Task task) {

        this.task = task;
    }
}
