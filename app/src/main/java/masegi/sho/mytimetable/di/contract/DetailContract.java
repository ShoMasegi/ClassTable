package masegi.sho.mytimetable.di.contract;

import java.util.ArrayList;

import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/07/06.
 */

public interface DetailContract {

    interface Views extends BaseView<Presenter>{
        void setClassObject(ClassObject classObject);
        void setTask(ArrayList<Task> tasks);
        void setMemo(String memo);
        void showTasks(ArrayList<Task> tasks);
        void showSnackBar(int messageId);
        void startMemoEditActivity(String memo);
        void startTodoEditActivity(Task task);
        void startTodoListActivity();
    }

    interface Presenter {
        void onCreate(ClassObject classObject);
        void onMemoClicked(String memo);
        void onTaskClicked(Task task);
        void onTodoMoreViewClicked();
        void saveMemoAndRefresh(String memo);
        void saveTodoAndRefresh();
        void removeTodoAndRefresh();
        void addNewTask();
    }
}
