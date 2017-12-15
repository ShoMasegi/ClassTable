package masegi.sho.mytimetable.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.Observer;
import masegi.sho.mytimetable.data.repository.PrefsRepository;
import masegi.sho.mytimetable.di.contract.SettingsContract;
import masegi.sho.mytimetable.domain.entity.ClassTableEntity;
import masegi.sho.mytimetable.domain.value.DayOfWeek;
import masegi.sho.mytimetable.preferences.ClassTablePreference;

/**
 * Created by masegi on 2017/09/15.
 */

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.Views settingsView;
    private PrefsRepository prefsRepository;

    private int tableId;

    public SettingsPresenter(@NonNull PrefsRepository prefsRepository,
                             @NonNull SettingsContract.Views settingView) {

        this.prefsRepository = prefsRepository;
        this.settingsView = settingView;
        settingView.setPresenter(this);
        tableId = ClassTablePreference.getInstance().getTableId();
    }

    @Override
    public void onClickChooseDays() {
        settingsView.showChooseDaysDialog();
    }

    @Override
    public void onClickSetClassTime() {
        settingsView.showSetClassTimeActivity();
    }

    @Override
    public void onClickChooseTable() {

        settingsView.showChooseTableDialog(tableId, prefsRepository.getTableNames());
    }

    @Override
    public void onClickEditTable(){
        settingsView.showEditTableActivity(tableId);
    }

    @Override
    public void onClickClassesCount() {

        settingsView.showClassesCountDialog();
    }

    @Override
    public void onClickAttendMode() {
        settingsView.showChooseAttendModeDialog();
    }

    @Override
    public void onClickLicenses() {
        settingsView.showLicensesListActivity();
    }

    @Override
    public void onCheckedNotificationSetting(boolean isChecked) {
        prefsRepository.setNotificationFlag(tableId, isChecked);
    }

    @Override
    public void onCheckedAttendSetting(boolean isChecked) {
        prefsRepository.setAttendManagementFlag(tableId, isChecked);
    }

    @Override
    public void onChoseDaysOfWeek(boolean[] isExistDays) {

        prefsRepository.setDaysOfWeek(tableId, isExistDays);
    }

    @Override
    public void onChoseTable(int id) {

        if (this.tableId != id) {

            this.tableId = id;
            ClassTablePreference.getInstance().setTableId(id);
            settingsView.restartView();
        }
    }

    @Override
    public void onSelectedClassesCount(int count) {

        prefsRepository.setCountOfClasses(tableId, count);
    }
}