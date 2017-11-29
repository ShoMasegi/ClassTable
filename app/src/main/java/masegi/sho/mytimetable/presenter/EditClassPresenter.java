package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;

import masegi.sho.mytimetable.data.repository.ClassObjectsRepository;
import masegi.sho.mytimetable.di.ClassDataResources;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.di.contract.EditClassContract;
import masegi.sho.mytimetable.domain.value.DayOfWeek;

/**
 * Created by masegi on 2017/06/23.
 */

public class EditClassPresenter implements EditClassContract.Presenter {

    private final EditClassContract.Views editView;
    private final ClassObjectsRepository classObjectsRepository;

    public EditClassPresenter(@NonNull ClassObjectsRepository classObjectsRepository,
                              @NonNull  EditClassContract.Views editView){
        this.classObjectsRepository = classObjectsRepository;
        this.editView = editView;

        editView.setPresenter(this);
    }

    @Override
    public void saveClassObject(ClassObject co) {
        classObjectsRepository.save(co, new ClassDataResources.SaveClassCallback() {
            @Override
            public void onClassSaved() {
                editView.savedClassObject();
            }

            @Override
            public void onClassSaveFailed(int num) {
                editView.showError(num);
            }
        });
    }


    @Override
    public void cancelBtnClick() {
        editView.canceled();
    }


    @Override
    public void prepare(final String className, final int[] position) {

        classObjectsRepository.getClass(className, new ClassDataResources.GetClassCallback() {
            @Override
            public void onClassLoaded(ClassObject classObject) {

                editView.prepareData(classObject);
            }

            @Override
            public void onDataNotAvailable() {

                ClassObject classObject = new ClassObject(
                        null,
                        DayOfWeek.getWeekByOrdinal(position[0]),
                        position[1]
                );
                editView.prepareData(classObject);
            }
        });
    }

    @Override
    public void onCreate() {
    }
}
