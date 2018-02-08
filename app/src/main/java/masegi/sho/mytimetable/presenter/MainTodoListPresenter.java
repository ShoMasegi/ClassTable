package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.Utils.ObserverUtil;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.di.RestoreDataSource;
import masegi.sho.mytimetable.di.contract.MainTodoListContract;
import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/08/16.
 */

public class MainTodoListPresenter implements MainTodoListContract.Presenter, ObserverUtil.Restore {

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
    public void onCreate() {

        restoreDataRepository.getAllTask(new RestoreDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(ArrayList<Task> tasksList) {

                todoListViews.setData(tasksList);
            }

            @Override
            public void onDataNotAvailable() { }
        });
    }



    @Override
    public void onPause() {

        if (removeList.size() > 0) {

            this.deleteData();
            this.shouldReload = true;
        }
    }

    @Override
    public void onResume() {

        if (this.shouldReload) {

            refreshData();
            shouldReload = false;
        }
    }

    @Override
    public void onDestroy() {

        MyApp.removeRestoreObserver(this);
    }

    @Override
    public void onSwipedToRemove(Task item, int position) {

        removeList.add(item);
        todoListViews.showDeletedTaskSnackBar(item, position);
    }

    @Override
    public void cancelDeleteTask(Task item) {

        removeList.remove(item);
    }

    @Override
    public void onItemClicked(Task item) {

        todoListViews.startTodoEditActivity(item);
    }

    private void deleteData() {

        for (Task task : removeList) {

            restoreDataRepository.deleteTask(task);
        }
        removeList.clear();
    }

    private void refreshData() {

        restoreDataRepository.getAllTask(new RestoreDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(ArrayList<Task> tasksList) {

                todoListViews.setData(tasksList);
                todoListViews.refreshViews();
            }

            @Override
            public void onDataNotAvailable() { }
        });
    }


    @Override
    public void notifyRestoreObjectChanged() {

        shouldReload = true;
    }
}
