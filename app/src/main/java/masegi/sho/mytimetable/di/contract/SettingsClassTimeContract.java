package masegi.sho.mytimetable.di.contract;

import java.util.ArrayList;
import java.util.Map;

import masegi.sho.mytimetable.domain.value.ClassTime;

/**
 * Created by masegi on 2017/10/23.
 */

public interface SettingsClassTimeContract {
    interface Views extends BaseView<Presenter> {
    }

    interface Presenter {

        void onResume();
        void onDestroy();
        Map<Integer, ClassTime> getClassTimes();
        void onPause(Map<Integer, ClassTime> classTimeMap);
    }
}
