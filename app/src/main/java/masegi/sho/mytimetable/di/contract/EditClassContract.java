package masegi.sho.mytimetable.di.contract;

import masegi.sho.mytimetable.domain.value.ClassObject;

/**
 * Created by masegi on 2017/06/23.
 */

public interface EditClassContract {

    interface Views extends BaseView<Presenter>{
        void showError(int msgNum);
        void prepareData(ClassObject classObject);
        void savedClassObject();
        void canceled();
    }


    interface Presenter extends BasePresenter{
        void saveClassObject(ClassObject classObject);
        void cancelBtnClick();
        void prepare(String className, int[] position);
    }
}
