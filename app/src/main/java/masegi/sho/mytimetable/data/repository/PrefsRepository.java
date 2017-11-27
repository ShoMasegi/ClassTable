package masegi.sho.mytimetable.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.data.source.ClassDataHelper;
import masegi.sho.mytimetable.domain.value.ClassTime;
import masegi.sho.mytimetable.domain.value.DayOfWeek;

import static masegi.sho.mytimetable.domain.entity.PreferenceEntity.*;
import static masegi.sho.mytimetable.domain.entity.PreferenceEntity.ClassTimeEntity.*;

/**
 * Created by masegi on 2017/09/08.
 */

public class PrefsRepository {

    private static PrefsRepository INSTANCE;
    private ClassDataHelper dbHelper;
    private SQLiteDatabase db;

    private static final String WHERECLAUSE = " = ?";
    private static final String DAYSOFWEEKS[] =
            { COLUMN_SUN_ON, COLUMN_MON_ON, COLUMN_TUE_ON,
                COLUMN_WED_ON, COLUMN_THU_ON, COLUMN_FRI_ON, COLUMN_SAT_ON };
    private static final String TIMEPREFSCOLUMN[] =
            { COLUMN_PERIOD,
                    COLUMN_START_TIME_HOUR, COLUMN_START_TIME_MIN,
                    COLUMN_END_TIME_HOUR, COLUMN_END_TIME_MIN };


    private PrefsRepository(@NonNull Context context){

        if (context != null) {

            dbHelper = ClassDataHelper.getInstance(context);
        }
    }

    public static PrefsRepository getInstance(@NonNull Context context){
        if (INSTANCE == null){
            INSTANCE = new PrefsRepository(context);
        }
        return INSTANCE;
    }

    public void setDaysOfWeek(int tableId, @NonNull DayOfWeek[] days) {

        String[] columns = DAYSOFWEEKS;

        ContentValues values = new ContentValues();
        List weekList = java.util.Arrays.asList(days);
        DayOfWeek[] weeks = DayOfWeek.values();

        for (int i = 0; i < weeks.length; i++) {

            int value = weekList.contains(weeks[i]) ? 1 : 0;
            values.put(columns[i], value);
        }

        String whereClause = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] whereArgs = { String.valueOf(tableId) };

        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long error = db.update(PREFS_TABLE_NAME, values, whereClause, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        MyApp.notifySettingsObservers();
    }

    public void setDaysOfWeek(int tableId, @NonNull boolean[] isExistDays) {

        String[] columns = DAYSOFWEEKS;

        ContentValues values = new ContentValues();

        for (int i = 0; i < isExistDays.length; i++) {

            int value = isExistDays[i] ? 1 : 0;
            values.put(columns[i], value);
        }

        String whereClause = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] whereArgs = { String.valueOf(tableId) };

        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long error = db.update(PREFS_TABLE_NAME, values, whereClause, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        MyApp.notifySettingsObservers();
    }

    public DayOfWeek[] getDaysOfWeek(int tableId) {

        ArrayList<DayOfWeek> weeksList = new ArrayList<>();
        DayOfWeek[] days = null;

        String[] columns = DAYSOFWEEKS;
        String selection = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] selectionArgs = { String.valueOf(tableId) };
        Cursor c;

        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            c = db.query(PREFS_TABLE_NAME, columns,
                    selection, selectionArgs, null, null, null);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        if (c != null) {

            while (c.moveToNext()) {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    if (c.getInt(i) > 0) {
                        weeksList.add(DayOfWeek.getWeekByOrdinal(i));
                    }
                }
            }

            days = (DayOfWeek[])weeksList.toArray(new DayOfWeek[0]) ;
            c.close();
        }

        return days;

    }

    public boolean[] getDaysOfWeekValue(int tableId) {

        boolean[] isExistDays = new boolean[7];

        String[] columns = DAYSOFWEEKS;
        String selection = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] selectionArgs = { String.valueOf(tableId) };
        Cursor c;

        db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            c = db.query(PREFS_TABLE_NAME, columns,
                    selection, selectionArgs, null, null, null);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        if (c != null) {

            c.moveToFirst();
            for (int i = 0; i < columns.length; i++) {
                isExistDays[i] = c.getInt(i) == 1;
            }

            c.close();
        }

        return isExistDays;

    }

    public void setCountOfClasses(int tableId, int count) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_COUNT_CLASS, count);
        String whereClause = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] whereArgs = { String.valueOf(tableId) };

        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long error = db.update(PREFS_TABLE_NAME, values, whereClause, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        MyApp.notifySettingsObservers();
    }

    public int getCountOfClasses(int tableId) {

        int count = 5;
        db = dbHelper.getReadableDatabase();
        String[] columns = { COLUMN_COUNT_CLASS };
        String selection = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] selectionArgs = { String.valueOf(tableId) };
        Cursor c;

        db.beginTransaction();
        try {
            c = db.query(PREFS_TABLE_NAME, columns,
                    selection, selectionArgs,
                    null, null, null);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        if (c != null) {

            c.moveToFirst();
            count = c.getInt(0);
            c.close();
        }

        db.close();
        return count;
    }

    public void setTableName(int whereId, @NonNull String tableName) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PREFS_TABLE_NAME, tableName);
        String whereClause = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] whereArgs = { String.valueOf(whereId) };

        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long error = db.update(PREFS_TABLE_NAME, values, whereClause, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public Map<Integer, String> getTableNames() {

        Map<Integer, String> tableNameList = null;

        db = dbHelper.getReadableDatabase();
        String[] columns = { COLUMN_PREFS_TABLE_ID, COLUMN_PREFS_TABLE_NAME };
        Cursor c;

        db.beginTransaction();
        try {
            c = db.query(PREFS_TABLE_NAME, columns, null, null, null, null, null);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        if (c != null) {
            tableNameList = new HashMap<Integer, String>();
            while (c.moveToNext())
                tableNameList.put(c.getInt(0), c.getString(1));
            c.close();
        }

        return tableNameList;
    }

    public void createTable(String tableName) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PREFS_TABLE_NAME, tableName);
        values.put(COLUMN_SUN_ON, 0);
        values.put(COLUMN_MON_ON, 1);
        values.put(COLUMN_TUE_ON, 1);
        values.put(COLUMN_WED_ON, 1);
        values.put(COLUMN_THU_ON, 1);
        values.put(COLUMN_FRI_ON, 1);
        values.put(COLUMN_SAT_ON, 0);
        values.put(COLUMN_COUNT_CLASS, 5);
        values.put(COLUMN_CARD_VIS, 0);
        values.put(COLUMN_NOTIF_ON, 0);
        values.put(COLUMN_ATT_MAN, 0);

        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long error = db.insert(PREFS_TABLE_NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void deleteTable(int tableId) {

        String whereClause = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] whereArgs = { String.valueOf(tableId) };
        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        int error = db.delete(PREFS_TABLE_NAME, whereClause, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        // TODO: 2017/10/06 remove classes of tableId
    }

    public void setNotificationFlag(int tableId, Boolean isChecked) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIF_ON, isChecked ? 1 : 0);

        String whereClause = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] whereArgs = {String.valueOf(tableId) };

        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long error = db.update(PREFS_TABLE_NAME, values, whereClause, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public boolean getNotificationFlag(int tableId) {

        boolean flag = false;
        db = dbHelper.getReadableDatabase();
        String[] columns = { COLUMN_NOTIF_ON };
        String selection = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] selectionArgs = { String.valueOf(tableId) };
        Cursor c;

        db.beginTransaction();
        try {
            c = db.query(PREFS_TABLE_NAME, columns,
                    selection, selectionArgs,
                    null, null, null);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        if (c != null) {
            c.moveToFirst();
            flag = c.getInt(0) > 0;
            c.close();
        }

        db.close();
        return flag;
    }

    public void setAttendManagementFlag(int tableId ,Boolean isChecked) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_ATT_MAN, isChecked ? 1 : 0);

        String whereClause = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] whereArgs = { String.valueOf(tableId) };

        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long error = db.update(PREFS_TABLE_NAME, values, whereClause, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public boolean getAttendManagementFlag(int tableId) {

        boolean flag = false;
        db = dbHelper.getReadableDatabase();
        String[] columns = { COLUMN_ATT_MAN };
        String selection = COLUMN_PREFS_TABLE_ID + WHERECLAUSE;
        String[] selectionArgs = { String.valueOf(tableId) };
        Cursor c;

        db.beginTransaction();
        try {
            c = db.query(PREFS_TABLE_NAME, columns,
                    selection, selectionArgs,
                    null, null, null);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        if (c != null) {
            c.moveToFirst();
            flag = c.getInt(0) > 0;
            c.close();
        }

        db.close();
        return flag;
    }

    public Map<Integer,ClassTime> getClassTimes(int tableId) {

        Map<Integer,ClassTime> map = new HashMap<>();
        db = dbHelper.getReadableDatabase();
        String[] columns = TIMEPREFSCOLUMN;
        String selection = COLUMN_TIME_PREFS_TABLE_ID + WHERECLAUSE;
        String[] selectionArgs = { String.valueOf(tableId) };
        Cursor c;

        db.beginTransaction();
        try {
            c = db.query(TIME_PREFS_TABLE_NAME, columns, selection,selectionArgs,
                    null, null, null);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        if (c != null) {

            while (c.moveToNext()) {

                ClassTime time = new ClassTime(
                        c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4));
                map.put(c.getInt(0), time);
            }
            c.close();
        }
        return map;
    }

    public void setClassTimes(int tableId, Map<Integer, ClassTime> classTimeMap) {

        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        for (Map.Entry<Integer, ClassTime> entry : classTimeMap.entrySet()) {

            String whereClause = COLUMN_TIME_PREFS_TABLE_ID + WHERECLAUSE
                    + " and " + COLUMN_PERIOD + WHERECLAUSE;
            String[] whereArgs = { String.valueOf(tableId), String.valueOf(entry.getValue().getPeriodNum()) };
            values.put(COLUMN_TIME_PREFS_TABLE_ID, tableId);
            values.put(COLUMN_PERIOD, entry.getValue().getPeriodNum());
            values.put(COLUMN_START_TIME_HOUR, entry.getValue().getStartHour());
            values.put(COLUMN_START_TIME_MIN, entry.getValue().getStartMin());
            values.put(COLUMN_END_TIME_HOUR, entry.getValue().getEndHour());
            values.put(COLUMN_END_TIME_MIN, entry.getValue().getEndMin());
            int e = db.update(TIME_PREFS_TABLE_NAME, values, whereClause, whereArgs);
            if (!(e > 0)) {

                db.insertOrThrow(TIME_PREFS_TABLE_NAME, null, values);
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        MyApp.notifySettingsObservers();
    }
}
