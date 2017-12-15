package masegi.sho.mytimetable.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.preference.PreferenceManager;

import java.util.Map;

import masegi.sho.mytimetable.BR;
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

public class ClassTablePreference extends BaseObservable implements Observer.Setting {

    private static ClassTablePreference instance = new ClassTablePreference();

    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final PrefsRepository repository;

    private int tableId = 0;
    private String tableName;
    private DayOfWeek[] weeks = { MON, TUE, WED, THU, FRI };
    private int countOfClasses = 5;
    private Map<Integer, ClassTime> classTimeMap;
    private boolean cardVisible = false;
    private boolean shouldNotify = false;
    private boolean attendManage = false;
    private String attendModeString = "Notification";

    private ClassTablePreference() {

        this.context = MyApp.getInstance().getApplicationContext();
        this.repository = PrefsRepository.getInstance(context);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.fetch();
        MyApp.addSettingsObserver(this);
    }

    public static ClassTablePreference getInstance() {

        if (instance == null) {

            instance = new ClassTablePreference();
        }
        return instance;
    }

    public int getTableId() {

        return this.tableId;
    }

    @Bindable
    public String getTableName() {

        return this.tableName;
    }

    public DayOfWeek[] getDaysOfWeek() {

        return this.weeks;
    }

    @Bindable
    public int getCountOfClasses() {

        return this.countOfClasses;
    }

    public Map<Integer, ClassTime> getClassTimeMap() {

        return this.classTimeMap;
    }

    public boolean getCardVisible() {

        return this.cardVisible;
    }

    @Bindable
    public boolean getShouldNotify() {

        return this.shouldNotify;
    }

    @Bindable
    public boolean getAttendManage() {

        return this.attendManage;
    }

    @Bindable
    public String getAttendModeString() {

        return this.attendModeString;
    }

    public void setTableId(int tableId) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.settings_pref_table_id_key), tableId);
        editor.commit();
        MyApp.notifySettingsObservers();
    }

    private void fetch() {

        this.tableId = sharedPreferences.getInt(context.getString(R.string.settings_pref_table_id_key), 0);
        this.tableName = repository.getTableNames().get(tableId);
        this.countOfClasses = repository.getCountOfClasses(tableId);
        this.weeks = repository.getDaysOfWeek(tableId);
        this.classTimeMap = repository.getClassTimes(tableId);
        this.shouldNotify = repository.getNotificationFlag(tableId);
        this.attendManage = repository.getAttendManagementFlag(tableId);
    }

    @Override
    public void notifySettingChanged() {

        this.fetch();
        notifyPropertyChanged(BR.countOfClasses);
        notifyPropertyChanged(BR.shouldNotify);
        notifyPropertyChanged(BR.attendManage);
        notifyPropertyChanged(BR.tableName);
        notifyPropertyChanged(BR.attendModeString);
    }
}
