package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.api.Observer;
import masegi.sho.mytimetable.data.repository.ClassObjectsRepository;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.di.ClassDataResources;
import masegi.sho.mytimetable.di.contract.ClassListContract;
import masegi.sho.mytimetable.domain.entity.ClassTableEntity;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.DayOfWeek;
import masegi.sho.mytimetable.preferences.ClassTablePreference;

/**
 * Created by masegi on 2017/07/31.
 */

public class ClassListPresenter implements ClassListContract.Presenter,
        Observer.Setting, Observer.Class, Observer.Restore {

    private final ClassListContract.Views todayViews;
    private HashMap<Integer, ClassListContract.ListViews> map;
    private HashSet<Integer> set;
    private final ClassObjectsRepository classObjectsRepository;
    private final RestoreDataRepository restoreDataRepository;
    private DayOfWeek[] days;
    private boolean shouldReload = false;


    public ClassListPresenter(@NonNull ClassObjectsRepository classObjectsRepository,
                              @NonNull RestoreDataRepository restoreDataRepository,
                              @NonNull ClassListContract.Views todayViews){

        this.classObjectsRepository = classObjectsRepository;
        this.restoreDataRepository = restoreDataRepository;
        this.todayViews = todayViews;

        map = new HashMap<>();
        set = new HashSet<>();
        days = ClassTablePreference.getInstance().getDaysOfWeek();
        todayViews.setPresenter(this);

        MyApp.addClassObserver(this);
        MyApp.addRestoreObserver(this);
        MyApp.addSettingsObserver(this);
    }


    @Override
    public void start() {}

    @Override
    public void attachListViews(int position,ClassListContract.ListViews listView) {

        listView.setPresenter(this);
        map.put(position, listView);
    }

    @Override
    public ClassListContract.ListViews getView(int page) {

        ClassListContract.ListViews view = map.get(page);
        return view;
    }

    @Override
    public void prepare(final int page) {

        classObjectsRepository.getWeekClasses(
                days[page],
                new ClassDataResources.GetClassesCallback() {
                    @Override
                    public void onClassesLoaded(ClassObject[] classObjects) {

                        HashMap memoMap = getMemoMap(classObjects);
                        map.get(page).prepareData(classObjects, memoMap);
                    }

                    @Override
                    public void onDataNotAvailable() {

                        ClassListContract.ListViews view = map.get(page);
                        if (view != null) {

                            map.get(page).prepareData(null, null);
                            map.get(page).showNoData();
                        }
                    }
                }
        );
    }

    @Override
    public void notifyClassObjectChanged(DayOfWeek day) {

        for (int i = 0; i < days.length; i++) {

            if (days[i] == day) {

                set.add(i);
                return;
            }
        }
    }

    @Override
    public void notifyRestoreObjectChanged() {

        for (int i = 0; i < days.length; i++) {
            set.add(i);
        }
    }

    @Override
    public void notifySettingChanged() {

        days = ClassTablePreference.getInstance().getDaysOfWeek();
        shouldReload = true;
    }

    @Override
    public void onResume() {

        if (shouldReload) {

            todayViews.rebuild();
            for (int i = 0; i < days.length; i++) {

                ClassListContract.ListViews view = map.get(i);
                if (view != null) view.update();
            }
            set.clear();
            shouldReload = false;
        } else {

            if (!(set.size() > 0)) return;
            for (int i : set) {
                ClassListContract.ListViews view = map.get(i);
                if (view != null) view.update();
            }
            set.clear();
        }
    }

    @Override
    public void onDestroy() {

        MyApp.removeClassObserver(this);
        MyApp.removeRestoreObserver(this);
    }

    @Override
    public void onItemClick(ClassObject item) {

        todayViews.startDetailActivity(item);
    }


    private HashMap getMemoMap(ClassObject[] classObjects){

        HashMap<String,String> memoMap = new HashMap<>();
        for (ClassObject object: classObjects) {

            String className = object.getClassName();
            String memo = restoreDataRepository.getMemo(className);
            memoMap.put(className,memo);
        }
        return memoMap;
    }
}
