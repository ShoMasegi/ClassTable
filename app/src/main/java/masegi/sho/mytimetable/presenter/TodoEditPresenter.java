package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;


import java.util.Calendar;
import java.util.Date;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.CalendarToString;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.di.contract.TodoEditContract;
import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/07/13.
 */


public class TodoEditPresenter implements TodoEditContract.Presenter {

    private final TodoEditContract.Views view;
    private final RestoreDataRepository restoreDataRepository;

    public TodoEditPresenter(@NonNull RestoreDataRepository restoreDataRepository,
                             @NonNull TodoEditContract.Views todoEditView){
        this.restoreDataRepository = restoreDataRepository;
        this.view = todoEditView;

        view.setPresenter(this);
    }

    @Override
    public void onCreate(String className, Calendar createDate) {

        Task task = new Task(className);
        if (createDate == null) {

            Calendar today = Calendar.getInstance();
            task.setCreateDate(today);
            today.add(Calendar.HOUR, 1);
            task.setDueDate(today);
        }
        else {

            String createDateString = CalendarToString.calendarToString(createDate);
            task = restoreDataRepository.getTask(className, createDateString);
            if (task == null) {

                view.showSnackBar(R.string.error_get_todo);
                Calendar today = Calendar.getInstance();
                task.setCreateDate(today);
                today.add(Calendar.HOUR, 1);
                task.setDueDate(today);
            }
        }
        view.setTask(task);
    }

    @Override
    public void onColorViewClicked() {

        view.showColorPickerDialog();
    }

    @Override
    public void onDueDateButtonClicked() {

        view.showDatePicker();
    }

    @Override
    public void onSaveButtonClicked(Task task) {

        this.saveTask(task);
        view.finishTodoActivity(true);
    }

    @Override
    public void onDeleteButtonClicked(Task task) {

        restoreDataRepository.deleteTask(task);
        view.finishTodoActivity(false);
    }

    private void saveTask(Task task) {

        if (task.getTaskName() == null) {

            view.showSnackBar(R.string.null_todo_taskName);
            return;
        }
        else if (task.getTaskName().isEmpty()) {

            view.showSnackBar(R.string.null_todo_taskName);
            return;
        }
        if (restoreDataRepository.getTask(task.getClassName(), task.getCreateDateString()) == null) {

            restoreDataRepository.saveTask(task);
        }
        else {

            restoreDataRepository.updateTask(task);
        }
    }
}
