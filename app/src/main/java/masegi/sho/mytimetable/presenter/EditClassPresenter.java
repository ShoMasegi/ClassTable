package masegi.sho.mytimetable.presenter;

import android.support.annotation.NonNull;
import android.view.View;

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
    public void onCreate(String className, final int[] position) {

        classObjectsRepository.getClass(className, new ClassDataResources.GetClassCallback() {
            @Override
            public void onClassLoaded(ClassObject classObject) {

                editView.setData(classObject);
            }

            @Override
            public void onDataNotAvailable() {

                ClassObject classObject = new ClassObject(
                        null,
                        DayOfWeek.getWeekByOrdinal(position[0]),
                        position[1]
                );
                editView.setData(classObject);
            }
        });
    }

    @Override
    public void onSaveButtonClicked(ClassObject co) {

        classObjectsRepository.save(co, new ClassDataResources.SaveClassCallback() {
            @Override
            public void onClassSaved() {
                editView.finishActivity();
            }

            @Override
            public void onClassSaveFailed(int num) {
                editView.showError(num);
            }
        });
    }


    @Override
    public void onCancelButtonClicked() {
        editView.finishActivity();
    }

    @Override
    public boolean onAttendLongClicked(View view, ClassObject object) {

        editView.showNumberPickerDialog(view, object);
        return true;
    }

    @Override
    public void onColorViewClicked() {

        editView.showColorPickerDialog();
    }
}
