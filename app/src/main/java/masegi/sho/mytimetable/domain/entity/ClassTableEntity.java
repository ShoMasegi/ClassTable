package masegi.sho.mytimetable.domain.entity;

import android.provider.BaseColumns;

import masegi.sho.mytimetable.domain.value.DayOfWeek;
import static masegi.sho.mytimetable.domain.value.DayOfWeek.*;

/**
 * Created by masegi on 2017/06/30.
 */

public class ClassTableEntity implements BaseColumns {

    public static final String CLASSES_TABLE_NAME = "classtable";
    public static final String COLUMN_CLASS_TABLE_ID = "classTableId";
    public static final String COLUMN_CLASS_NAME = "className";
    public static final String COLUMN_TEACHER_NAME = "teacherName";
    public static final String COLUMN_ROOM_NAME = "classRoom";
    public static final String COLUMN_WEEK = "week";
    public static final String COLUMN_STARTTIME = "startTime";
    public static final String COLUMN_SECTION = "section";
    public static final String COLUMN_ATTEND = "attend";
    public static final String COLUMN_LATE = "late";
    public static final String COLUMN_ABSENT = "absent";
    public static final String COLUMN_COLOR = "color";
}
