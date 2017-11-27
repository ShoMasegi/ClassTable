package masegi.sho.mytimetable.di.contract;

import java.util.ArrayList;

import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/08/16.
 */

public interface MainTodoListContract {

    interface Views extends BaseView<Presenter>{
        void setData(ArrayList todoList);
        void showError();
        void showViews();
        void startTodoEditActivity(Task item);
    }

    interface Presenter extends BasePresenter{
        void refreshData();
        void onPause();
        void onResume();
        void onDestroy();
        void addToRemoveList(Task item);
        void deleteFromRemoveList(Task item);
        void itemClicked(Task item);
    }
}
