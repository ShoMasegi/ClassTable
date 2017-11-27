package masegi.sho.mytimetable.domain.entity;

/**
 * Created by masegi on 2017/09/08.
 */

public class PreferenceEntity {

    public static String PREFS_TABLE_NAME = "prefstable";

    public static String COLUMN_PREFS_TABLE_ID = "prefsTableId";
    public static String COLUMN_PREFS_TABLE_NAME = "prefsTableName";

    public static String COLUMN_SUN_ON = "sunOn";
    public static String COLUMN_MON_ON = "monOn";
    public static String COLUMN_TUE_ON = "tueOn";
    public static String COLUMN_WED_ON = "wedOn";
    public static String COLUMN_THU_ON = "thuOn";
    public static String COLUMN_FRI_ON = "friOn";
    public static String COLUMN_SAT_ON = "satOn";

    public static String COLUMN_COUNT_CLASS = "numberOfClasses";

    public static String COLUMN_CARD_VIS = "cardVisible";
    public static String COLUMN_NOTIF_ON = "notificationOn";
    public static String COLUMN_ATT_MAN = "attendanceManagement";


    public static class ClassTimeEntity {

        public static String TIME_PREFS_TABLE_NAME = "timePrefsTable";

        public static String COLUMN_TIME_PREFS_TABLE_ID = "prefsTimeTableId";

        public static String COLUMN_PERIOD = "period";
        public static String COLUMN_START_TIME_HOUR = "startTimeHour";
        public static String COLUMN_START_TIME_MIN = "startTimeMin";
        public static String COLUMN_END_TIME_HOUR = "endTimeHour";
        public static String COLUMN_END_TIME_MIN = "endTimeMin";
    }
}
