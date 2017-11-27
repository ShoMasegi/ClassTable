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
        void startTodoEditActivity(Task item);
    }

    interface Presenter extends BasePresenter {
        void refreshData();
        void onPause();
        void onResume();
        void onDestroy();
        void addToRemoveList(Task item);
        void deleteFromRemoveList(Task item);
        void onItemClicked(Task item);
        void onFABClicked();
    }
}
