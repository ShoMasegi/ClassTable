package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.api.Observer;
import masegi.sho.mytimetable.data.ClassDataSource;
import masegi.sho.mytimetable.di.contract.ClassTableContract;
import masegi.sho.mytimetable.data.repository.ClassObjectsRepository;
import masegi.sho.mytimetable.di.ClassDataResources;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.DayOfWeek;

/**
 * Created by masegi on 2017/07/03.
 */

public class ClassTablePresenter implements ClassTableContract.Presenter,
        Observer.Class, Observer.Setting{

    private ClassDataResources classObjectsRepository;
    private final ClassTableContract.Views classTableViews;

    private boolean shouldReload = false;
    private boolean shouldRefresh = false;


    public ClassTablePresenter(@NonNull ClassObjectsRepository classObjectsRepository,
                               @NonNull ClassTableContract.Views views){
        this.classObjectsRepository = classObjectsRepository;
        this.classTableViews = views;

        classTableViews.setPresenter(this);
        MyApp.addClassObserver(this);
        MyApp.addSettingsObserver(this);
    }

    @Override
    public void start() {
        classObjectsRepository.getAllClasses(new ClassDataResources.GetAllClassesCallback() {
            @Override
            public void onClassesLoaded(ClassDataSource classDataSource) {

                classTableViews.setClassDataSource(classDataSource);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void clickTimeTableItem(ClassObject item) {
        if(item.getClassName() != null)
            classTableViews.startDetailActivity(item);
    }

    @Override
    public void longClickTimeTableItem(View view, ClassObject item) {
        classTableViews.showPopupMenu(view,item);
    }

    /**
     * @param item object to save or update
     */
    @Override
    public void addClass(@Nullable ClassObject item) {
        classTableViews.startEditActivity(item);
    }
    @Override
    public void editClass(@NonNull ClassObject item) {
        classTableViews.startEditActivity(item);
    }

    @Override
    public void deleteClass(@NonNull final ClassObject item) {

        if(item != null) {
            classTableViews.showAlertsDialog(item.getClassName(),
                    new ClassTableContract.Views.DeleteClassCallback() {
                        @Override
                        public void onDelete() {
                            classObjectsRepository.delete(item);
                            reloadAllClasses();
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
        }
    }

    @Override
    public void onResume() {

        if (shouldRefresh) {

            refreshClassTableView();
            shouldRefresh = false;
            shouldReload = false;
        } else if (shouldReload) {

            reloadAllClasses();
            shouldReload = false;
        }
    }

    @Override
    public void onDestroy() {

        MyApp.removeClassObserver(this);
        MyApp.removeSettingsObserver(this);
    }

    @Override
    public void notifySettingChanged() {

        shouldRefresh = true;
    }

    @Override
    public void notifyClassObjectChanged(DayOfWeek day) {

        shouldReload = true;
    }

    private void refreshClassTableView() {

        classObjectsRepository.getAllClasses(new ClassDataResources.GetAllClassesCallback() {
            @Override
            public void onClassesLoaded(ClassDataSource classDataSource) {

                classTableViews.refresh(classDataSource);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void reloadAllClasses() {

        classObjectsRepository.getAllClasses(new ClassDataResources.GetAllClassesCallback() {
            @Override
            public void onClassesLoaded(ClassDataSource classDataSource) {

                classTableViews.setClassDataSource(classDataSource);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}
