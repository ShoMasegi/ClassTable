package masegi.sho.mytimetable;

import android.app.Application;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import masegi.sho.mytimetable.Utils.ObserverUtil;
import masegi.sho.mytimetable.domain.value.DayOfWeek;

/**
 * Created by masegi on 2017/08/23.
 */

public class MyApp extends Application {

    private static List<ObserverUtil.Restore> restores = new CopyOnWriteArrayList<>();
    private static List<ObserverUtil.Class> classes = new CopyOnWriteArrayList<>();
    private static List<ObserverUtil.Setting> settings = new CopyOnWriteArrayList<>();

    public static void notifyRestoreObservers(){

        for (ObserverUtil.Restore observer : restores) {
            observer.notifyRestoreObjectChanged();
        }
    }
    public static void notifyClassObservers(DayOfWeek day){

        if (day == null) return;
        for (ObserverUtil.Class observer : classes) {
            observer.notifyClassObjectChanged(day);
        }
    }
    public static void notifySettingsObservers() {

        for (ObserverUtil.Setting observer : settings) {
            observer.notifySettingChanged();
        }
    }

    public static void addRestoreObserver(ObserverUtil.Restore observer){
        restores.add(observer);
    }
    public static void addClassObserver(ObserverUtil.Class observer) {
        classes.add(observer);
    }
    public static void addSettingsObserver(ObserverUtil.Setting observer) {
        settings.add(observer);
    }
    public static void removeRestoreObserver(ObserverUtil.Restore observer){
        restores.remove(observer);
    }
    public static void removeClassObserver(ObserverUtil.Class observer){
        classes.remove(observer);
    }
    public static void removeSettingsObserver(ObserverUtil.Setting observer) {
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
