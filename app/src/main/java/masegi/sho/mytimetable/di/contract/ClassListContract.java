package masegi.sho.mytimetable.di.contract;

import java.util.HashMap;

import masegi.sho.mytimetable.domain.value.ClassObject;

/**
 * Created by masegi on 2017/07/31.
 */

public interface ClassListContract {

    interface Views extends BaseView<Presenter> {
        void rebuild();
        void startDetailActivity(ClassObject item);
    }

    interface ListViews extends BaseView<Presenter> {
        void setData(ClassObject[] classObjects, HashMap memoMap);
        void update(ClassObject[] classObjects, HashMap memoMap);
    }

    interface Presenter {
        void attachListViews(int position, ListViews listView);
        ClassListContract.ListViews getView(int page);
        void onListViewCreate(int page);
        void onListViewCreateView(int page);
        void onListViewDestroy(int page);
        void onMainViewDestroy();
        void onItemClick(ClassObject item);
    }
}
