package masegi.sho.mytimetable.di.contract;

import java.util.Map;

/**
 * Created by masegi on 2017/09/15.
 */

public interface SettingsContract {
    interface Views extends BaseView<Presenter> {
        void showChooseDaysDialog();
        void showSetClassTimeActivity();
        void showChooseTableDialog(int presentId, Map<Integer, String> tableNameMap);
        void showClassesCountDialog();
        void showChooseAttendModeDialog();
        void showEditTableActivity(int tableId);
        void showLicensesListActivity();
        void restartView();
        void prepareRestartMainActivity();
    }

    interface Presenter {
        void onClickChooseDays();
        void onClickSetClassTime();
        void onClickChooseTable();
        void onClickEditTable();
        void onClickClassesCount();
        void onClickAttendMode();
        void onClickLicenses();
        void onCheckedNotificationSetting(boolean isChecked);
        void onCheckedAttendSetting(boolean isChecked);
        void onChoseDaysOfWeek(boolean[] isExistDays);
        void onChoseTable(int id);
        void onSelectedClassesCount(int count);
    }
}
