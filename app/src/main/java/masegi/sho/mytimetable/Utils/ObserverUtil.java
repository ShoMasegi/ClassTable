package masegi.sho.mytimetable.Utils;

import masegi.sho.mytimetable.domain.value.DayOfWeek;

/**
 * Created by masegi on 2017/08/23.
 */

public interface ObserverUtil {

    interface Setting {
        void notifySettingChanged();
    }

    interface Class {
        void notifyClassObjectChanged(DayOfWeek day);
    }

    interface Restore {
        void notifyRestoreObjectChanged();
    }
}
