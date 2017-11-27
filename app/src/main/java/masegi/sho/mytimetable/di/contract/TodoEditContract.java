package masegi.sho.mytimetable.di.contract;

import java.util.Calendar;

import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/07/13.
 */

public interface TodoEditContract {

    interface Views extends BaseView<Presenter>{
        void showSnackBar(int messageId);
        void showTaskData(Task task);
        void savedTask();
        void removedTask();
    }

    interface Presenter extends BasePresenter{
        void saveTask(Task task);
        void updateTask(Task task);
        void removeTask(Task task);
        void prepareTodo(String className,String createDate);
    }
}
