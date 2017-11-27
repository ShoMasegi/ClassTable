package masegi.sho.mytimetable.domain.entity;


/**
 * Created by masegi on 2017/07/06.
 */

public class RestoreDataEntity {
    public static final String RESTORE_TABLE_NAME = "restoreDataTable";
    public static final String COLUMN_CLASSNAME_KEY = "classNameKey";
    public static final String COLUMN_MEMO = "memo";
    public static final String COLUMN_TASK_NAME = "taskName";
    public static final String COLUMN_DUEDATE = "dueDate";
    public static final String COLUMN_CREATEDATE = "createDate";
    public static final String COLUMN_TASK_CONTENT = "taskContent";
    public static final String COLUMN_TASK_COLOR = "taskColor";
    public static final String COLUMN_IS_COMPLETED = "isCompleted";

    public static final int TASK_COMPLETED = 1;
    public static final int TASK_NOT_COMPLETED = 0;

}
