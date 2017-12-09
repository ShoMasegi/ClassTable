package masegi.sho.mytimetable.di.contract;

import android.view.View;

import masegi.sho.mytimetable.domain.value.ClassObject;

/**
 * Created by masegi on 2017/06/23.
 */

public interface EditClassContract {

    interface Views extends BaseView<Presenter>{
        void showError(int msgNum);
        void showNumberPickerDialog(View view, ClassObject object);
        void showColorPickerDialog();
        void setData(ClassObject classObject);
        void finishActivity();
    }


    interface Presenter {
        void onCreate(String className, int[] position);
        void onSaveButtonClicked(ClassObject classObject);
        void onCancelButtonClicked();
        boolean onAttendLongClicked(View view, ClassObject object);
        void onColorViewClicked();
    }
}
