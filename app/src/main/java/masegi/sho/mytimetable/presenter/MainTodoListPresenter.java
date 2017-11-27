package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.api.Observer;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.di.RestoreDataSource;
import masegi.sho.mytimetable.di.contract.MainTodoListContract;
import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/08/16.
 */

public class MainTodoListPresenter implements MainTodoListContract.Presenter, Observer.Restore {

    private RestoreDataSource restoreDataRepository;
    private final MainTodoListContract.Views todoListViews;

    private ArrayList<Task> removeList;
    private boolean shouldReload = false;

    public MainTodoListPresenter(@NonNull RestoreDataRepository restoreDataRepository,
                                 @NonNull MainTodoListContract.Views todoListViews){
        this.restoreDataRepository = restoreDataRepository;
        this.todoListViews = todoListViews;
        removeList = new ArrayList<>();

        MyApp.addRestoreObserver(this);
        todoListViews.setPresenter(this);
    }

    @Override
    public void start() {}

    @Override
    public void refreshData() {

        restoreDataRepository.getAllTask(new RestoreDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(ArrayList<Task> tasksList) {

                todoListViews.setData(tasksList);
                todoListViews.showViews();
            }

            @Override
            public void onDataNotAvailable() {

                todoListViews.showError();
            }
        });
    }



    @Override
    public void onPause() {

        if (removeList.size() > 0) {

            deleteData();
            this.shouldReload = true;
        }
    }

    @Override
    public void onResume() {

        if (shouldReload) {

            refreshData();
            shouldReload = false;
        }
    }

    @Override
    public void onDestroy() {

        MyApp.removeRestoreObserver(this);
    }

    @Override
    public void addToRemoveList(Task item) {

        removeList.add(item);
    }

    @Override
    public void deleteFromRemoveList(Task item) {

        removeList.remove(item);
    }

    @Override
    public void itemClicked(Task item) {

        todoListViews.startTodoEditActivity(item);
    }

    private void deleteData() {

        for (Task task : removeList) {

            restoreDataRepository.deleteTask(task);
        }
        removeList.clear();
    }

    @Override
    public void notifyRestoreObjectChanged() {

        shouldReload = true;
    }
}
