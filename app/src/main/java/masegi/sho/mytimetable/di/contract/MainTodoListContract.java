package masegi.sho.mytimetable.di.contract;

import java.util.ArrayList;

import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/08/16.
 */

public interface MainTodoListContract {

    interface Views extends BaseView<Presenter>{
        void setData(ArrayList todoList);
        void showDeletedTaskSnackBar(Task item, int position);
        void refreshViews();
        void startTodoEditActivity(Task item);
    }

    interface Presenter extends BasePresenter{
        void onPause();
        void onResume();
        void onDestroy();
        void onSwipedToRemove(Task item, int position);
        void cancelDeleteTask(Task item);
        void onItemClicked(Task item);
    }
}
