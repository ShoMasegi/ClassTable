package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.di.RestoreDataSource;
import masegi.sho.mytimetable.di.contract.DetailContract;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/07/06.
 */

public class DetailPresenter implements DetailContract.Presenter {

    private final DetailContract.Views detailView;
    private final RestoreDataRepository restoreDataRepository;

    private String className;

    public DetailPresenter(@NonNull RestoreDataRepository restoreDataRepository,
                           @NonNull DetailContract.Views detailView){

        this.restoreDataRepository = restoreDataRepository;
        this.detailView = detailView;
        detailView.setPresenter(this);
    }

    @Override
    public void onCreate(ClassObject classObject) {

        if (classObject == null) { return; }
        this.className = classObject.getClassName();
        detailView.setClassObject(classObject);
        restoreDataRepository.getTasks(
                className,
                false,
                new RestoreDataSource.GetTaskCallback() {
                    @Override
                    public void onTaskLoaded(ArrayList<Task> tasksList) {
                        detailView.setTask(tasksList);
                    }

                    @Override
                    public void onDataNotAvailable() {
                    }
                }
        );
        detailView.setMemo(restoreDataRepository.getMemo(className));
    }

    @Override
    public void onMemoClicked(String memo) {
        detailView.startMemoEditActivity(memo);
    }

    @Override
    public void onTaskClicked(Task task) {
        detailView.startTodoEditActivity(task);
    }

    @Override
    public void onTodoMoreViewClicked() {

        detailView.startTodoListActivity();
    }

    @Override
    public void saveMemoAndRefresh(String memo) {

        restoreDataRepository.saveMemo(className,memo);
        detailView.setMemo(memo);
    }

    @Override
    public void saveTodoAndRefresh() {

        restoreDataRepository.getAllTask(className, new RestoreDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(ArrayList<Task> tasksList) {
                detailView.showTasks(tasksList);
            }

            @Override
            public void onDataNotAvailable() {
                detailView.showSnackBar(R.string.error_get_todo);
            }
        });
    }

    @Override
    public void removeTodoAndRefresh() {

        restoreDataRepository.getAllTask(className, new RestoreDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(ArrayList<Task> tasksList) {
                detailView.showTasks(tasksList);
                detailView.showSnackBar(R.string.delete_task);
            }

            @Override
            public void onDataNotAvailable() {
                detailView.showSnackBar(R.string.error_get_todo);
            }
        });
    }


    @Override
    public void addNewTask() {
        detailView.startTodoEditActivity(null);
    }


}
