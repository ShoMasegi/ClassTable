package masegi.sho.mytimetable.di.contract;

import java.util.ArrayList;

import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/07/06.
 */

public interface DetailContract {

    interface Views extends BaseView<Presenter>{
        void prepareData(ClassObject classObject);
        void prepareTask(ArrayList<Task> tasks);
        void prepareMemo(String memo);
        void showTasks(ArrayList<Task> tasks);
        void showMemo(String memo);
        void showSnackBar(int messageId);
        void startMemoEditActivity(String memo);
        void startTodoEditActivity(Task task);
        void startTodoListActivity();
    }

    interface Presenter extends BasePresenter{
        void prepare(ClassObject classObject);
        void clickMemoView(String memo);
        void clickTaskItem(Task task);
        void clickTaskMore();
        void saveMemoAndRefresh(String memo);
        void saveTodoAndRefresh();
        void removeTodoAndRefresh();
        void addNewTask();
    }
}
