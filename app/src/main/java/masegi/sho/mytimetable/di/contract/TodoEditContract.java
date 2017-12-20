package masegi.sho.mytimetable.di.contract;

import java.util.Calendar;

import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/07/13.
 */

public interface TodoEditContract {

    interface Views extends BaseView<Presenter>{
        void showDatePicker();
        void showColorPickerDialog();
        void showSnackBar(int messageId);
        void finishTodoActivity(boolean isSaved);
        void setTask(Task task);
    }

    interface Presenter {
        void onCreate(String className, Calendar createDate);
        void onColorViewClicked();
        void onDueDateButtonClicked();
        void onSaveButtonClicked(Task task);
        void onDeleteButtonClicked(Task task);
    }
}