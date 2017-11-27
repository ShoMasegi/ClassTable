package masegi.sho.mytimetable;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import masegi.sho.mytimetable.api.Observer;
import masegi.sho.mytimetable.data.repository.PrefsRepository;
import masegi.sho.mytimetable.domain.entity.ClassTableEntity;
import masegi.sho.mytimetable.domain.value.DayOfWeek;

/**
 * Created by masegi on 2017/08/23.
 */

public class MyApp extends Application {

    private static List<Observer.Restore> restores = new CopyOnWriteArrayList<>();
    private static List<Observer.Class> classes = new CopyOnWriteArrayList<>();
    private static List<Observer.Setting> settings = new CopyOnWriteArrayList<>();

    public static void notifyRestoreObservers(){

        for (Observer.Restore observer : restores) {
            observer.notifyRestoreObjectChanged();
        }
    }
    public static void notifyClassObservers(DayOfWeek day){

        if (day == null) return;
        for (Observer.Class observer : classes) {
            observer.notifyClassObjectChanged(day);
        }
    }
    public static void notifySettingsObservers() {

        for (Observer.Setting observer : settings) {
            observer.notifySettingChanged();
        }
    }

    public static void addRestoreObserver(Observer.Restore observer){
        restores.add(observer);
    }
    public static void addClassObserver(Observer.Class observer) {
        classes.add(observer);
    }
    public static void addSettingsObserver(Observer.Setting observer) {
        settings.add(observer);
    }
    public static void removeRestoreObserver(Observer.Restore observer){
        restores.remove(observer);
    }
    public static void removeClassObserver(Observer.Class observer){
        classes.remove(observer);
    }
    public static void removeSettingsObserver(Observer.Setting observer) {
        settings.remove(observer);
    }


    private static MyApp instance = null;

    public static MyApp getInstance() {

        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
    }


}
