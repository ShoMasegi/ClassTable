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
                             @NonNull SettingsContract.Views settingView){

        this.prefsRepository = prefsRepository;
        this.settingsView = settingView;

        settingView.setPresenter(this);
        tableId = ClassTablePreference.getInstance().getTableId();
    }

    public void onClickChooseDays() {
        settingsView.showChooseDaysDialog();
    }

    public void onClickSetClassTime() {
        settingsView.showSetClassTimeActivity();
    }

    public void onClickChooseTable() {
        settingsView.showChooseTableDialog(tableId);
    }

    public void onClickEditTable(){
        settingsView.showEditTableActivity(tableId);
    }

    public void onClickClassesCount() {
        settingsView.showClassesCountDialog();
    }

    public void onClickAttendMode() {
        settingsView.showChooseAttendModeDialog();
    }

    public void onClickLicenses() {
        settingsView.showLicensesListActivity();
    }

    public void onCheckedNotificationSetting(boolean isChecked) {
        prefsRepository.setNotificationFlag(tableId, isChecked);
    }

    public void onCheckedAttendSetting(boolean isChecked) {
        prefsRepository.setAttendManagementFlag(tableId, isChecked);
    }

    @Override
    public String getCountOfClassesString(){

        int count = prefsRepository.getCountOfClasses(tableId);
        return String.valueOf(count) + " classes";
    }

    @Override
    public String getCurrentTableName() {
        return prefsRepository.getTableNames().get(tableId);
    }

    public int getShowAttendModeSettingVisibility() {

        if (prefsRepository.getNotificationFlag(tableId)) {
            return View.GONE;
        }
        else {
            return View.VISIBLE;
        }
    }

    public String getAttendModeString() { return "Notification"; }

    public boolean shouldNotify() {
        return prefsRepository.getNotificationFlag(tableId);
    }

    public boolean shouldAttendManagement() {

        return prefsRepository.getAttendManagementFlag(tableId);
    }


    @Override
    public void setDaysOfWeekValue(boolean[] isExistDays) {

        prefsRepository.setDaysOfWeek(tableId, isExistDays);
    }

    @Override
    public void setCurrentTableId(int id) {

        if (this.tableId != id) {

            this.tableId = id;
            ClassTablePreference.getInstance().setTableId(id);
            settingsView.restartView();
        }
    }

    @Override
    public void setCountOfClasses(int count) {

        prefsRepository.setCountOfClasses(tableId, count);
    }

    @Override
    public Map<Integer, String> getTableNames() {
        return prefsRepository.getTableNames();
    }
}