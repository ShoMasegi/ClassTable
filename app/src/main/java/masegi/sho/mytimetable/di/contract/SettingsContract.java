package masegi.sho.mytimetable.di.contract;

import java.util.Map;

import masegi.sho.mytimetable.domain.value.DayOfWeek;

/**
 * Created by masegi on 2017/09/15.
 */

public interface SettingsContract {
    interface Views extends BaseView<Presenter> {
        void showChooseDaysDialog();
        void showSetClassTimeActivity();
        void showChooseTableDialog(int presentId);
        void showClassesCountDialog();
        void showChooseAttendModeDialog();
        void showEditTableActivity(int tableId);
        void showLicensesListActivity();
        void restartView();
        void prepareRestartMainActivity();
    }

    interface Presenter {
        void setDaysOfWeekValue(boolean[] isExistDays);
        void setCurrentTableId(int id);
        void setCountOfClasses(int count);
        String getCurrentTableName();
        String getCountOfClassesString();
        Map<Integer, String> getTableNames();
    }
}
