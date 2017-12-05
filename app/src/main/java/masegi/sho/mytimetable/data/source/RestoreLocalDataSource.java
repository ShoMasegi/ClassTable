package masegi.sho.mytimetable.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.CalendarToString;
import masegi.sho.mytimetable.api.Observer;
import masegi.sho.mytimetable.di.RestoreDataSource;
import masegi.sho.mytimetable.domain.value.Task;
import masegi.sho.mytimetable.domain.value.ThemeColor;

import static masegi.sho.mytimetable.domain.entity.ClassTableEntity.COLUMN_CLASS_TABLE_ID;
import static masegi.sho.mytimetable.domain.entity.RestoreDataEntity.*;

/**
 * Created by masegi on 2017/07/06.
 */

public class RestoreLocalDataSource implements RestoreDataSource, Observer.Setting{

    private static RestoreLocalDataSource INSTACE;
    private ClassDataHelper dbHelper;
    private Context context;
    private int tableId;

    private SharedPreferences sharedPreferences;

    private final String[] TASK_COLUMNS = {
            COLUMN_CLASSNAME_KEY,COLUMN_TASK_NAME,
            COLUMN_DUEDATE,COLUMN_CREATEDATE,
            COLUMN_TASK_CONTENT,COLUMN_TASK_COLOR,
            COLUMN_IS_COMPLETED
    };
    private final String WHERECLAUSE = " = ?";
    private final String ANDCLAUSE = " and ";
    private final String MEMO_IS_NOT_NULL = COLUMN_MEMO + " is not null";
    private final String MEMO_IS_NULL = COLUMN_MEMO + " is null";
    private final String TASK_NAME_IS_NULL = COLUMN_TASK_NAME + " is null";

    private RestoreLocalDataSource(@NonNull Context context){

        if(context != null){

            this.context = context;
            dbHelper = ClassDataHelper.getInstance(context);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            tableId = sharedPreferences.getInt(context.getString(R.string.settings_pref_table_id_key), 0);
        }

        MyApp.addSettingsObserver(this);
    }

    public static RestoreLocalDataSource getInstance(@NonNull Context context){

        if(INSTACE == null){

            INSTACE = new RestoreLocalDataSource(context);
        }
        return INSTACE;
    }


    @Override
    public void getAllTask(@NonNull String className, @NonNull GetTaskCallback callback) {

        ArrayList<Task> tasksList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                + ANDCLAUSE + COLUMN_CLASSNAME_KEY + WHERECLAUSE
                + ANDCLAUSE + MEMO_IS_NULL;
        String[] selectionArgs = { String.valueOf(tableId), className };
        String orderBy = COLUMN_DUEDATE + " ASC";
        Cursor c;
        db.beginTransaction();
        try {

            c = db.query(RESTORE_TABLE_NAME,TASK_COLUMNS,
                    selection ,selectionArgs,null,null,orderBy);
            db.setTransactionSuccessful();
        }
        finally {

            db.endTransaction();
        }

        if(c != null) {

            tasksList = new ArrayList<Task>();
            while (c.moveToNext()) tasksList.add(cursorToTask(c));
            c.close();
        }

        db.close();
        if(tasksList == null) {

            callback.onDataNotAvailable();
        }
        else {

            callback.onTaskLoaded(tasksList);
        }
    }

    @Override
    public void getAllTask(@NonNull GetTaskCallback callback) {

        ArrayList<Task> tasksList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                + ANDCLAUSE + MEMO_IS_NULL;
        String[] selectionArgs = { String.valueOf(tableId) };
        String orderBy = COLUMN_DUEDATE + " ASC";
        Cursor c;
        db.beginTransaction();
        try {

            c = db.query(RESTORE_TABLE_NAME,TASK_COLUMNS,
                    selection,selectionArgs,null,null,orderBy);
            db.setTransactionSuccessful();
        }
        finally {

            db.endTransaction();
        }
        if(c != null) {

            tasksList = new ArrayList<Task>();
            while (c.moveToNext()) tasksList.add(cursorToTask(c));
        }
        if(c != null) {

            c.close();
        }
        db.close();
        if(tasksList == null) {

            callback.onDataNotAvailable();
        }
        else {

            callback.onTaskLoaded(tasksList);
        }
    }


    @Override
    public void getTasks(@NonNull String className,boolean isCompleted, @NonNull GetTaskCallback callback) {

        String selection = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                + ANDCLAUSE + COLUMN_CLASSNAME_KEY + WHERECLAUSE
                + ANDCLAUSE + COLUMN_IS_COMPLETED + WHERECLAUSE
                + ANDCLAUSE + MEMO_IS_NULL;
        int isCompletedTask = isCompleted ? TASK_COMPLETED : TASK_NOT_COMPLETED;
        String[] selectionArgs = { String.valueOf(tableId), className, String.valueOf(isCompletedTask) };
        ArrayList<Task> tasksList = getTasks(selection,selectionArgs);
        if(tasksList == null) {

            callback.onDataNotAvailable();
        }
        else {

            callback.onTaskLoaded(tasksList);
        }
    }

    @Override
    public Task getTask(@NonNull String className, String createDate) {

        String selection = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                + ANDCLAUSE + COLUMN_CLASSNAME_KEY + WHERECLAUSE
                + ANDCLAUSE + COLUMN_CREATEDATE + WHERECLAUSE
                + ANDCLAUSE + MEMO_IS_NULL;
        String[] selectionArgs = { String.valueOf(tableId), className, createDate };
        ArrayList<Task> tasks = getTasks(selection,selectionArgs);
        return tasks.size() > 0 ? tasks.get(0) : null;
    }

    private ArrayList getTasks(@NonNull String selection, @NonNull String[] selectionArgs){

        ArrayList<Task> tasksList = new ArrayList<Task>();
        Cursor c;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {

            c = db.query(RESTORE_TABLE_NAME, TASK_COLUMNS,
                    selection, selectionArgs, null, null, null);
            db.setTransactionSuccessful();
        }
        finally {

            db.endTransaction();
        }
        if(c != null && c.getCount() > 0) {

            while (c.moveToNext()) tasksList.add(cursorToTask(c));
        }
        if (c != null) {

            c.close();
        }
        db.close();
        return tasksList;
    }

    @Override
    public String getMemo(@NonNull String className) {

        String memo = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = { COLUMN_MEMO };
        String selection = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                + ANDCLAUSE + COLUMN_CLASSNAME_KEY + WHERECLAUSE
                + ANDCLAUSE + MEMO_IS_NOT_NULL;
        String[] selectionArgs = { String.valueOf(tableId), className };
        Cursor c;
        db.beginTransaction();
        try {

            c = db.query(RESTORE_TABLE_NAME, columns,
                    selection, selectionArgs,null,null,null);
            db.setTransactionSuccessful();
        }
        finally {

            db.endTransaction();
        }
        if(c != null && c.getCount() > 0) {

            c.moveToFirst();
            memo = c.getString(0);
        }
        if(c != null) {

            c.close();
        }
        db.close();
        return memo;
    }

    @Override
    public void saveTask(Task task) {

        ContentValues values = taskToValues(task);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long error = db.insert(RESTORE_TABLE_NAME,null,values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        MyApp.notifyRestoreObservers();
    }

    @Override
    public void saveMemo(@NonNull String className, String memo) {

        if (getMemo(className) == null) {

            insertMemo(className,memo);
        }
        else{

            updateMemo(className,memo);
        }
        MyApp.notifyRestoreObservers();
    }

    private void insertMemo(@NonNull String className,String memo){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_TABLE_ID, tableId);
        values.put(COLUMN_CLASSNAME_KEY,className);
        values.put(COLUMN_MEMO,memo);
        db.beginTransaction();
        long error = db.insertOrThrow(RESTORE_TABLE_NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private void updateMemo(@NonNull String className,String memo){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_TABLE_ID, tableId);
        values.put(COLUMN_CLASSNAME_KEY,className);
        values.put(COLUMN_MEMO,memo);
        String where = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                + ANDCLAUSE + COLUMN_CLASSNAME_KEY + WHERECLAUSE
                + ANDCLAUSE + TASK_NAME_IS_NULL;
        String[] whereArgs = { String.valueOf(tableId), className };

        db.beginTransaction();
        int num = db.update(RESTORE_TABLE_NAME, values, where, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    @Override
    public void updateTask(Task task) {

        ContentValues values = taskToValues(task);
        String selection = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                + ANDCLAUSE + COLUMN_CLASSNAME_KEY + WHERECLAUSE
                + ANDCLAUSE + COLUMN_CREATEDATE + WHERECLAUSE;
        String[] selectionArgs = {
                String.valueOf(tableId),
                task.getClassName(),
                CalendarToString.calendarToString(task.getCreateDate())
        };
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        int i = db.update(RESTORE_TABLE_NAME,values,selection,selectionArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        MyApp.notifyRestoreObservers();
    }

    @Override
    public void deleteTask(Task task) {

        String selection = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                + ANDCLAUSE + COLUMN_TASK_NAME + WHERECLAUSE
                + ANDCLAUSE + COLUMN_CREATEDATE + WHERECLAUSE;
        String[] selectionArgs = {
                String.valueOf(tableId),
                task.getClassName(),
                CalendarToString.calendarToString(task.getCreateDate())
        };

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        db.delete(RESTORE_TABLE_NAME,selection,selectionArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        MyApp.notifyRestoreObservers();
    }


    /**
     * @param cursor
     * @return Task
     *
     * Translate Cursor to ClassObject.Do not call cursor#moveToFirst().
     *
     * !!!!!!!!!!---------Warning-------------!!!!!!!!!!!
     * ColumnIndex dose not identify.So if you change column order of Table ,
     * absolutely check this columnIndexes.
     */
    private Task cursorToTask(Cursor cursor) {

        Task task = new Task(cursor.getString(0));
        task.setTaskName(cursor.getString(1));
        task.setDueDate(cursor.getString(2));
        task.setCreateDate(cursor.getString(3));
        task.setTaskContent(cursor.getString(4));
        task.setThemeColor(ThemeColor.getThemeColor(cursor.getInt(5)));
        task.setCompleted(cursor.getInt(6));

        return task;
    }

    private ContentValues taskToValues(Task task) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_TABLE_ID, tableId);
        values.put(COLUMN_CLASSNAME_KEY,task.getClassName());
        values.put(COLUMN_TASK_NAME,task.getTaskName());
        values.put(COLUMN_DUEDATE,
                CalendarToString.calendarToString(task.getDueDate()));
        values.put(COLUMN_CREATEDATE,
                CalendarToString.calendarToString(task.getCreateDate()));
        values.put(COLUMN_TASK_CONTENT, task.getTaskContent());
        values.put(COLUMN_TASK_COLOR,task.getThemeColor().getThemeId());
        values.put(COLUMN_IS_COMPLETED,task.isCompleted());

        return values;
    }

    @Override
    public void notifySettingChanged() {

        tableId = sharedPreferences.getInt(context.getString(R.string.settings_pref_table_id_key), 0);
    }
}
