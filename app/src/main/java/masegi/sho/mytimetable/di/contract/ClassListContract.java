package masegi.sho.mytimetable.di.contract;

import java.util.HashMap;

import masegi.sho.mytimetable.domain.value.ClassObject;

/**
 * Created by masegi on 2017/07/31.
 */

public interface ClassListContract {

    interface Views extends BaseView<Presenter>{
        void rebuild();
        void startDetailActivity(ClassObject item);
    }

    interface ListViews extends BaseView<Presenter>{
        void setData(ClassObject[] classObjects, HashMap memoMap);
        void showNoData();
        void update();
    }

    interface Presenter extends BasePresenter{
        void attachListViews(int position, ListViews listView);
        ClassListContract.ListViews getView(int page);
        void prepare(int page);
        void onResume();
        void onDestroy();
        void onItemClick(ClassObject item);
    }
}
