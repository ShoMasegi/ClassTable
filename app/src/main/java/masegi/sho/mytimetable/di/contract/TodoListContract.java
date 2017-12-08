package masegi.sho.mytimetable.di.contract;

import java.util.ArrayList;

import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/11/17.
 */

public interface TodoListContract {

    interface Views extends BaseView<Presenter> {
        void setData(ArrayList data);
        void showError();
        void showDeletedTaskSnackBar(Task item, int position);
        void startTodoEditActivity(Task item);
    }

    interface Presenter {
        void onCreate(String className);
        void backedFromEditActivity();
        void onPause();
        void onResume();
        void onDestroy();
        void onSwipedToRemove(Task item, int position);
        void cancelDeleteTask(Task item);
        void onItemClicked(Task item);
        void onFABClicked();
    }
}
