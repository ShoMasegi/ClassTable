package masegi.sho.mytimetable.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static masegi.sho.mytimetable.domain.entity.ClassTableEntity.*;
import static masegi.sho.mytimetable.domain.entity.PreferenceEntity.*;
import static masegi.sho.mytimetable.domain.entity.PreferenceEntity.ClassTimeEntity.*;
import static masegi.sho.mytimetable.domain.entity.RestoreDataEntity.*;

/**
 * Created by masegi on 2016/08/30.
 */
public class ClassDataHelper extends SQLiteOpenHelper {

    private static ClassDataHelper INSTANCE;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ClassTable.db"; private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";


    /**
     *!!!!!!!!!!---------Warning-------------!!!!!!!!!!!
     * if you change Table column order, absolutely check
     * @see ClassLocalDataResources#cursorToClassObject(Cursor)
     * and change columnINdex.
     */
    private static final String SQL_CREATE_ENTRIES=
            "CREATE TABLE " + CLASSES_TABLE_NAME + " (" +
                    COLUMN_CLASS_TABLE_ID + INTEGER_TYPE + COMMA_SEP +
                    _ID + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_CLASS_NAME + TEXT_TYPE + " NOT NULL," +
                    COLUMN_TEACHER_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_ROOM_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_WEEK + TEXT_TYPE + COMMA_SEP +
                    COLUMN_STARTTIME + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_SECTION + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_ATTEND + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_LATE + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_ABSENT + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_COLOR + INTEGER_TYPE + ");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CLASSES_TABLE_NAME;

    /**
     *!!!!!!!!!!---------Warning-------------!!!!!!!!!!!
     * if you change Table column order, absolutely check
     * @see RestoreLocalDataSource#cursorToTask(Cursor)
     * and change columnINdex.
     */
    private static final String SQL_RESTORE_CREATE_ENTRIES =
            "CREATE TABLE " + RESTORE_TABLE_NAME+ " (" +
                    COLUMN_CLASS_TABLE_ID + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_CLASSNAME_KEY + TEXT_TYPE + " NOT NULL " + COMMA_SEP +
                    COLUMN_MEMO + TEXT_TYPE + COMMA_SEP +
                    COLUMN_TASK_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_DUEDATE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_CREATEDATE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_TASK_CONTENT + TEXT_TYPE + COMMA_SEP +
                    COLUMN_TASK_COLOR + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_IS_COMPLETED + INTEGER_TYPE + ");";

    private static final String SQL_RESTORE_DELETE_ENTRIES =
            "DROP TABLE IF EXIST " + RESTORE_TABLE_NAME;

    private static final String SQL_PREFS_CREATE_ENTRIES =
            "CREATE TABLE " + PREFS_TABLE_NAME + " (" +
                    COLUMN_PREFS_TABLE_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    COLUMN_PREFS_TABLE_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_SUN_ON + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_MON_ON + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_TUE_ON + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_WED_ON + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_THU_ON + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_FRI_ON + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_SAT_ON + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_COUNT_CLASS + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_CARD_VIS + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NOTIF_ON + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_ATT_MAN + INTEGER_TYPE + ");";

    private static final String SQL_PREFS_DELETE_ENTRIES =
            "DROP TABLE IF EXIST " + PREFS_TABLE_NAME;

    private static final String SQL_TIME_PREFS_CREATE_ENTRIES =
            "CREATE TABLE " + TIME_PREFS_TABLE_NAME + " (" +
                    COLUMN_TIME_PREFS_TABLE_ID + INTEGER_TYPE + " NOT NULL" + COMMA_SEP +
                    COLUMN_PERIOD + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_START_TIME_HOUR + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_START_TIME_MIN + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_END_TIME_HOUR + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_END_TIME_MIN + INTEGER_TYPE + ");";

    private static final String SQL_TIME_PREFS_DELETE_ENTRIES =
            "DROP TABLE IF EXIST " + TIME_PREFS_TABLE_NAME;

    private ClassDataHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static ClassDataHelper getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new ClassDataHelper(context);
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(SQL_CREATE_ENTRIES);
            db.execSQL(SQL_RESTORE_CREATE_ENTRIES);
            db.execSQL(SQL_PREFS_CREATE_ENTRIES);
            db.execSQL(SQL_TIME_PREFS_CREATE_ENTRIES);

            insertDefaultData(db);
        }catch (Exception e){

            System.out.print(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_RESTORE_DELETE_ENTRIES);
        db.execSQL(SQL_PREFS_DELETE_ENTRIES);
        db.execSQL(SQL_TIME_PREFS_DELETE_ENTRIES);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }

    private void insertDefaultData(SQLiteDatabase db) {

        ContentValues prefValues = new ContentValues();
        prefValues.put(COLUMN_PREFS_TABLE_ID,0);
        prefValues.put(COLUMN_PREFS_TABLE_NAME, "Default table");
        prefValues.put(COLUMN_SUN_ON, 0);
        prefValues.put(COLUMN_MON_ON, 1);
        prefValues.put(COLUMN_TUE_ON, 1);
        prefValues.put(COLUMN_WED_ON, 1);
        prefValues.put(COLUMN_THU_ON, 1);
        prefValues.put(COLUMN_FRI_ON, 1);
        prefValues.put(COLUMN_SAT_ON, 0);
        prefValues.put(COLUMN_COUNT_CLASS, 5);
        prefValues.put(COLUMN_NOTIF_ON, 0);
        prefValues.put(COLUMN_ATT_MAN, 0);

        db.beginTransaction();
        db.insertOrThrow(PREFS_TABLE_NAME, null, prefValues);
        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
