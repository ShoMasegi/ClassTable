package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;


import masegi.sho.mytimetable.R;
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
    public void start() {}

    @Override
    public void saveTask(Task task) {
        if (task.getTaskName() == null|task.getTaskName().isEmpty()){
            view.showSnackBar(R.string.null_todo_taskName);
            return;
        }
        restoreDataRepository.saveTask(task);
        view.savedTask();
    }

    @Override
    public void updateTask(Task task) {
        if (task.getTaskName() == null|task.getTaskName().isEmpty()){
            view.showSnackBar(R.string.null_todo_taskName);
            return;
        }
        restoreDataRepository.updateTask(task);
        view.savedTask();
    }

    @Override
    public void removeTask(Task task) {
        restoreDataRepository.deleteTask(task);
        view.removedTask();
    }

    @Override
    public void prepareTodo(String className, String createDate) {
        Task task = restoreDataRepository.getTask(className,createDate);
        if(task != null) view.showTaskData(task);
        else view.showSnackBar(R.string.error_get_todo);
    }
}
