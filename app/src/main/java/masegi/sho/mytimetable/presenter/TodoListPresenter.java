package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.api.Observer;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.di.RestoreDataSource;
import masegi.sho.mytimetable.di.contract.TodoListContract;
import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/11/17.
 */

public class TodoListPresenter implements TodoListContract.Presenter, Observer.Restore {

    private RestoreDataSource repository;
    private final TodoListContract.Views view;
    private boolean shouldReload = false;

    private ArrayList<Task> removeList = new ArrayList<>();

    public TodoListPresenter(@NonNull RestoreDataRepository repository,
                             @NonNull final TodoListContract.Views view) {

        this.repository = repository;
        this.view = view;
        MyApp.addRestoreObserver(this);
        view.setPresenter(this);
        this.repository.getAllTask(new RestoreDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(ArrayList<Task> tasksList) {

                view.setData(tasksList);
            }

            @Override
            public void onDataNotAvailable() {}
        });
    }


    @Override
    public void notifyRestoreObjectChanged() {

        this.shouldReload = true;
    }


    @Override
    public void onCreate() { }

    @Override
    public void refreshData() {

        repository.getAllTask(new RestoreDataSource.GetTaskCallback() {

            @Override
            public void onTaskLoaded(ArrayList<Task> tasksList) {

                view.setData(tasksList);
            }

            @Override
            public void onDataNotAvailable() {

                view.showError();
            }
        });
    }

    @Override
    public void onPause() {

        if (removeList.size() > 0) {

            for(Task task : removeList) {

                repository.deleteTask(task);
            }
            removeList.clear();
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
    public void onItemClicked(Task item) {

        view.startTodoEditActivity(item);
    }

    @Override
    public void onFABClicked() {

        view.startTodoEditActivity(null);
    }
}
