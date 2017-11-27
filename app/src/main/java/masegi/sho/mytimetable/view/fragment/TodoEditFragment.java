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

    private String createDate;
    private String dueDate;
    private Calendar createDateCalendar;
    private Calendar dueDateCalendar;
    private Task task;
    private String className;

    public static final int RESULT_SAVED = 10;
    public static final int RESULT_REMOVED = 20;

    private final int ADD_MODE = 1;
    private final int UPDATE_MODE = 2;
    private int EDIT_MODE = ADD_MODE;

    public TodoEditFragment() {
        // Required empty public constructor
    }

    public static TodoEditFragment newInstance(){
        TodoEditFragment fragment = new TodoEditFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        className = getArguments().getString(TODO_CLASSNAME_KEY);
        createDate = getArguments().getString(TODO_CREATE_KEY);

        task = new Task(className);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_todo_edit,container,false);
        ButterKnife.bind(this,root);

        if(createDate == null){
            createDateCalendar = Calendar.getInstance();
            dueDateCalendar = Calendar.getInstance();
            dueDateCalendar.add(Calendar.HOUR,1);
            createDate = CalendarToString.calendarToCreateDate(createDateCalendar);
            dueDate = CalendarToString.calendarToDueDate(dueDateCalendar);
        }else{
            presenter.prepareTodo(className,createDate);
        }
        setupView();
        return root;
    }


    private void setupView(){
        todoSub.setText(className);
        createTextView.setText(createDate);
        dueTextView.setText(dueDate);

        dueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EDIT_MODE == ADD_MODE){
                    presenter.saveTask(outputToTask(task));
                }else if(EDIT_MODE == UPDATE_MODE){
                    presenter.updateTask(outputToTask(task));
                }
            }
        });


        if (EDIT_MODE == ADD_MODE) negativeBtn.setVisibility(View.GONE);

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.removeTask(outputToTask(task));
            }
        });

        setColorView(colorView, task.getThemeColor());
        colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createColorPickerDialog(ThemeColor.getColorResIdArray());
            }
        });
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

    private void createColorPickerDialog(ArrayList colorResIdArray){

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
            public void onDismiss() {
            }
        });
        colorPickerDialog.show(getChildFragmentManager(),"todoEdit_coloPickerFragment");
    }

    /**
     * get data from Views,and set task instance.
     * --------!!!!     Warning     !!!!---------------
     * if add new data resource,
     * you must add setter sentence too.
     * @param task
     * @return task
     */
    private Task outputToTask(Task task){
        task.setTaskName(taskNameEdit.getText().toString());
        task.setDueDate(dueDateCalendar);
        task.setCreateDate(createDateCalendar);
        task.setTaskContent(contentEdit.getText().toString());

        return task;
    }

    /**
     * Override TodoEditContract.View
     * @see TodoEditContract.Views
     * Here method make change the View of this Activity when Presenter instruct
     */
    @Override
    public void setPresenter(@NonNull TodoEditContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void showDatePicker() {
        DateTimePickerFragment dateTimePickerFragment
                = new DateTimePickerFragment();
        dateTimePickerFragment.setCallback(new DateTimePickerFragment.GetCalendarCallback() {
            @Override
            public void onGetCalendar(Calendar calendar) {
                dueDateCalendar = calendar;
                dueTextView.setText(CalendarToString.calendarToDueDate(dueDateCalendar));
            }
        }).show(getFragmentManager(),"dateTimePickerDialog");
    }

    /**
     * show snack bar
     * @param messageId
     * Resource id
     */
    @Override
    public void showSnackBar(int messageId) {
        Snackbar.make(layout,getString(messageId),Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showTaskData(Task task) {
        this.task = task;
        taskNameEdit.setText(task.getTaskName());
        contentEdit.setText(task.getTaskContent());
        setColorView(colorView, task.getThemeColor());
        this.createDateCalendar = task.getCreateDate();
        this.createDate = task.getCreateDateString();
        this.dueDateCalendar = task.getDueDate();
        this.dueDate = task.getDueDateString();
        this.EDIT_MODE = UPDATE_MODE;
    }

    /**
     * setResult(),resultCode
     * RESULT_OK saved
     * RESULT_CANCEL remove
     */
    @Override
    public void savedTask() {
        Intent intent = new Intent();
        getActivity().setResult(RESULT_SAVED,intent);
        getActivity().finish();
    }
    @Override
    public void removedTask() {
        Intent intent = new Intent();
        getActivity().setResult(RESULT_REMOVED,intent);
        getActivity().finish();
    }
}
