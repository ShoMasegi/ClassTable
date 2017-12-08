package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.di.RestoreDataSource;
import masegi.sho.mytimetable.di.contract.DetailContract;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.Task;

import static android.app.Activity.RESULT_OK;
import static masegi.sho.mytimetable.view.activity.MemoEditActivity.MEMO_REQUEST_CODE;
import static masegi.sho.mytimetable.view.activity.TodoEditActivity.TODO_REQUEST_CODE;
import static masegi.sho.mytimetable.view.activity.TodoListActivity.TODOLIST_REQUEST_CODE;
import static masegi.sho.mytimetable.view.fragment.TodoEditFragment.RESULT_REMOVED;
import static masegi.sho.mytimetable.view.fragment.TodoEditFragment.RESULT_SAVED;

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
    public void onActivityResult(int requestCode, int resultCode) {

        switch (requestCode) {

            case (MEMO_REQUEST_CODE):
                if (resultCode == RESULT_OK) {

                    String memo = restoreDataRepository.getMemo(className);
                    detailView.setMemo(memo);
                }
                break;

            case (TODO_REQUEST_CODE):
                if (resultCode == RESULT_SAVED) {

                    this.savedTodo();
                }
                else if (resultCode == RESULT_REMOVED) {

                    this.removedTodo();
                }
                break;

            case (TODOLIST_REQUEST_CODE):
                this.savedTodo();
        }
    }

    @Override
    public void onTodoMoreViewClicked() {

        detailView.startTodoListActivity();
    }

    @Override
    public void onTodoFabClicked() {
        detailView.startTodoEditActivity(null);
    }

    public void savedTodo() {

        restoreDataRepository.getAllTask(className, new RestoreDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(ArrayList<Task> tasksList) {
                detailView.setTask(tasksList);
            }

            @Override
            public void onDataNotAvailable() {
                detailView.showSnackBar(R.string.error_get_todo);
            }
        });
    }

    public void removedTodo() {

        restoreDataRepository.getAllTask(className, new RestoreDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(ArrayList<Task> tasksList) {
                detailView.setTask(tasksList);
                detailView.showSnackBar(R.string.delete_task);
            }

            @Override
            public void onDataNotAvailable() {
                detailView.showSnackBar(R.string.error_get_todo);
            }
        });
    }
}
