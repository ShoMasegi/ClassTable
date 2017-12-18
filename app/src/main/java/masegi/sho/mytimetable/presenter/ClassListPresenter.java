package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.api.Observer;
import masegi.sho.mytimetable.data.repository.ClassObjectsRepository;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.di.ClassDataResources;
import masegi.sho.mytimetable.di.contract.ClassListContract;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.DayOfWeek;
import masegi.sho.mytimetable.preferences.ClassTablePreference;

/**
 * Created by masegi on 2017/07/31.
 */

public class ClassListPresenter implements ClassListContract.Presenter,
        Observer.Setting, Observer.Class, Observer.Restore {

    private final ClassListContract.Views mainView;
    private HashMap<Integer, ClassListContract.ListViews> childViewsMap = new HashMap<>();
    private HashMap<Integer, Boolean> shouldReloadMap = new HashMap<>();
    private final ClassObjectsRepository classObjectsRepository;
    private final RestoreDataRepository restoreDataRepository;
    private DayOfWeek[] days;


    public ClassListPresenter(@NonNull ClassObjectsRepository classObjectsRepository,
                              @NonNull RestoreDataRepository restoreDataRepository,
                              @NonNull ClassListContract.Views mainView) {

        this.classObjectsRepository = classObjectsRepository;
        this.restoreDataRepository = restoreDataRepository;
        this.mainView = mainView;

        days = ClassTablePreference.getInstance().getDaysOfWeek();
        mainView.setPresenter(this);

        MyApp.addClassObserver(this);
        MyApp.addRestoreObserver(this);
        MyApp.addSettingsObserver(this);
    }

    @Override
    public void attachListViews(int position,ClassListContract.ListViews listView) {

        listView.setPresenter(this);
        childViewsMap.put(position, listView);
    }

    @Override
    public ClassListContract.ListViews getView(int page) {

        ClassListContract.ListViews view = childViewsMap.get(page);
        this.shouldReloadMap.put(page, true);
        return view;
    }

    @Override
    public void onListViewCreate(final int page) {

        this.fetchData(page);
    }

    @Override
    public void onListViewCreateView(int page) {

        if (this.shouldReloadMap.get(page)) {

            this.fetchData(page);
        }
    }

    @Override
    public void onListViewDestroy(int page) {

        this.childViewsMap.remove(page);
        this.shouldReloadMap.remove(page);
    }

    @Override
    public void onMainViewDestroy() {

        this.childViewsMap.clear();
        this.shouldReloadMap.clear();
        MyApp.removeSettingsObserver(this);
        MyApp.removeClassObserver(this);
        MyApp.removeRestoreObserver(this);
    }

    @Override
    public void onItemClick(ClassObject item) {

        mainView.startDetailActivity(item);
    }


    @Override
    public void notifyClassObjectChanged(DayOfWeek day) {

        for (int i = 0; i < days.length; i++) {

            if (days[i] == day) {

                this.shouldReloadMap.put(i, true);
                return;
            }
        }
    }

    @Override
    public void notifyRestoreObjectChanged() {

        for (Map.Entry<Integer, Boolean> entry : this.shouldReloadMap.entrySet()) {

            this.shouldReloadMap.put(entry.getKey(), true);
        }
    }

    @Override
    public void notifySettingChanged() {

        this.days = ClassTablePreference.getInstance().getDaysOfWeek();
        for (Map.Entry<Integer, Boolean> entry : this.shouldReloadMap.entrySet()) {

            this.shouldReloadMap.put(entry.getKey(), true);
        }
        this.mainView.rebuild();
    }


    private void fetchData(int page) {

        final ClassListContract.ListViews view = this.childViewsMap.get(page);
        if (view == null) {

            return;
        }
        this.classObjectsRepository.getWeekClasses(
                this.days[page],
                new ClassDataResources.GetClassesCallback() {
                    @Override
                    public void onClassesLoaded(ClassObject[] classObjects) {

                        HashMap memoMap = getMemoMap(classObjects);
                        view.update(classObjects, memoMap);
                    }

                    @Override
                    public void onDataNotAvailable() {

                        view.update(null, null);
                    }
                }
        );
        this.shouldReloadMap.put(page, false);
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
