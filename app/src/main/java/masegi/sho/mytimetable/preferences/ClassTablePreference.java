package masegi.sho.mytimetable.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.Observer;
import masegi.sho.mytimetable.data.repository.PrefsRepository;
import masegi.sho.mytimetable.domain.value.ClassTime;
import masegi.sho.mytimetable.domain.value.DayOfWeek;

import static masegi.sho.mytimetable.domain.value.DayOfWeek.*;

/**
 * Created by masegi on 2017/11/01.
 */

public class ClassTablePreference implements Observer.Setting {

    private static ClassTablePreference instance = new ClassTablePreference();

    private ClassTablePreference() {

        this.context = MyApp.getInstance().getApplicationContext();
        this.repository = PrefsRepository.getInstance(context);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.tableId = sharedPreferences.getInt(context.getString(R.string.settings_pref_table_id_key), 0);
        this.sectionCount = repository.getCountOfClasses(tableId);
        this.weeks = repository.getDaysOfWeek(tableId);
        this.classTimeMap = repository.getClassTimes(tableId);
        MyApp.addSettingsObserver(this);
    }

    public static ClassTablePreference getInstance() {

        return instance;
    }

    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final PrefsRepository repository;

    private int tableId = 0;
    private DayOfWeek[] weeks = { MON,TUE,WED,THU,  FRI };
    private int sectionCount = 5;
    private Map<Integer, ClassTime> classTimeMap;

    public int getTableId() {

        return this.tableId;
    }

    public void setTableId(int tableId) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.settings_pref_table_id_key), tableId);
        editor.commit();
        MyApp.notifySettingsObservers();
    }

    public DayOfWeek[] getDaysOfWeek() {

        return this.weeks;
    }

    public int getCountOfSection() {

        return this.sectionCount;
    }

    public Map<Integer, ClassTime> getClassTimeMap() {

        return this.classTimeMap;
    }

    @Override
    public void notifySettingChanged() {

        tableId = sharedPreferences.getInt(context.getString(R.string.settings_pref_table_id_key), 0);
        sectionCount = repository.getCountOfClasses(tableId);
        weeks = repository.getDaysOfWeek(tableId);
        classTimeMap = repository.getClassTimes(tableId);
    }
}
