package masegi.sho.mytimetable.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.Map;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.Utils.ObserverUtil;
import masegi.sho.mytimetable.data.repository.PrefsRepository;
import masegi.sho.mytimetable.di.contract.SettingsClassTimeContract;
import masegi.sho.mytimetable.domain.value.ClassTime;

/**
 * Created by masegi on 2017/10/23.
 */

public class SettingsClassTimePresenter implements SettingsClassTimeContract.Presenter,
        ObserverUtil.Setting {

    private SettingsClassTimeContract.Views view;
    private PrefsRepository prefsRepository;
    private SharedPreferences sharedPref;
    private Context context;

    private int tableId;
    private boolean shouldReload = false;

    public SettingsClassTimePresenter(@NonNull Context context,
                                      @NonNull PrefsRepository prefsRepository,
                                      @NonNull SettingsClassTimeContract.Views view) {

        this.context = context;
        this.prefsRepository = prefsRepository;
        this.view = view;

        view.setPresenter(this);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        tableId = sharedPref.getInt(context.getString(R.string.settings_pref_table_id_key), 0);
        MyApp.addSettingsObserver(this);
   }

    @Override
    public void onResume() {

        if (shouldReload) {

            tableId = sharedPref.getInt(context.getString(R.string.settings_pref_table_id_key), 0);
            shouldReload = false;
        }
    }

    @Override
    public void onDestroy() {

        MyApp.removeSettingsObserver(this);
    }

    @Override
    public Map<Integer, ClassTime> getClassTimes() {

        return prefsRepository.getClassTimes(tableId);
    }

    @Override
    public void onPause(Map<Integer, ClassTime> classTimeMap) {

        prefsRepository.setClassTimes(tableId, classTimeMap);
    }


    @Override
    public void notifySettingChanged() {

        shouldReload = true;
    }
}
