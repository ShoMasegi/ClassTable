package masegi.sho.mytimetable.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import masegi.sho.mytimetable.MyApp;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.Observer;
import masegi.sho.mytimetable.data.ClassDataSource;
import masegi.sho.mytimetable.di.ClassDataResources;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.entity.ClassTableEntity;
import masegi.sho.mytimetable.domain.entity.ErrorMessageEntity;
import masegi.sho.mytimetable.domain.value.DayOfWeek;
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.preferences.ClassTablePreference;

import static masegi.sho.mytimetable.domain.entity.ClassTableEntity.*;

/**
 * Created by masegi on 2017/06/30.
 */

public class ClassLocalDataResources implements ClassDataResources , Observer.Setting{

    private static ClassLocalDataResources INSTANCE;
    private ClassDataHelper dbHelper;
    private Context context;

    /**
     * save max id number to stamp new data.
     * Call incremantIdNumber() to update {@param ID_NUMBER} when save new data.
     * @see this#incrementIdNumber()
     */
    private int tableId;
    private SharedPreferences sharedPreferences;
    private int ID_NUMBER;
    private final String KEY_ID = "ID_NUMBER_KEY";
    private final String WHERECLAUSE = " = ?";
    private final String ANDCLAUSE = " AND ";


    private ClassLocalDataResources(@NonNull Context context){

        if (context != null) {
            this.context = context;
            dbHelper = ClassDataHelper.getInstance(context);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            ID_NUMBER = sharedPreferences.getInt(KEY_ID,1001);
            tableId = sharedPreferences.getInt(
                    context.getString(R.string.settings_pref_table_id_key), 0);
        }

        MyApp.addSettingsObserver(this);
    }

    public static ClassLocalDataResources getInstance(@NonNull Context context){
        if (INSTANCE == null){
            INSTANCE = new ClassLocalDataResources(context);
        }
        return INSTANCE;
    }

    private void incrementIdNumber(){
        ID_NUMBER++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID,ID_NUMBER);
        editor.commit();
    }


    @Override
    public void getAllClasses(@NonNull GetAllClassesCallback callback) {

        EnumMap<DayOfWeek, ArrayList<ClassObject>> dataSource
                = new EnumMap<DayOfWeek, ArrayList<ClassObject>>(DayOfWeek.class);

        boolean isNull = true;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        for(DayOfWeek week : DayOfWeek.values()) {
            ClassObject weekObject[] = new ClassObject[10];
            ArrayList<ClassObject> array = new ArrayList<>();
            String[] selectionArgs = {String.valueOf(tableId), week.toString()};
            String where = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                    + ANDCLAUSE + COLUMN_WEEK + WHERECLAUSE;
            db.beginTransaction();
            try {
                Cursor c = db.query(CLASSES_TABLE_NAME, null, where, selectionArgs, null, null, null);
                if (c != null) {
                    isNull = false;
                    while (c.moveToNext()) {
                        array.add(cursorToClassObject(c));
                    }
                    dataSource.put(week, array);
                }
                if (c != null) c.close();
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        db.close();

        ClassDataSource classDataSource = new ClassDataSource(dataSource);
        if(isNull) callback.onDataNotAvailable();
        else callback.onClassesLoaded(classDataSource);

    }

    @Override
    public void getClass(@NonNull String className, @NonNull GetClassCallback callback) {
        ClassObject object = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                + ANDCLAUSE + COLUMN_CLASS_NAME + WHERECLAUSE;
        String[] selectionArgs = { String.valueOf(tableId), className};
        Cursor c;
        if(className == null){
            callback.onDataNotAvailable();
            return;
        }
        db.beginTransaction();
        try{
            c = db.query(CLASSES_TABLE_NAME,null,
                    selection,selectionArgs,null,null,COLUMN_STARTTIME+" ASC");
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

        if(c != null && c.getCount() > 0){
            c.moveToFirst();
            object = cursorToClassObject(c);
        }
        if(c != null) c.close();

        db.close();
        if (object == null) callback.onDataNotAvailable();
        else callback.onClassLoaded(object);

    }

    @Override
    public void getClass(DayOfWeek week, int startTime,GetClassCallback callback) {
        ClassObject object = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        String selection = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                + ANDCLAUSE + COLUMN_WEEK + WHERECLAUSE
                + ANDCLAUSE + COLUMN_STARTTIME + WHERECLAUSE;
        String[] selectionArgs = { week.toString(),String.valueOf(startTime) };
        db.beginTransaction();
        try{
            c = db.query(CLASSES_TABLE_NAME,null,selection,selectionArgs,
                    null,null,COLUMN_STARTTIME+" ASC");
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

        if(c != null && c.getCount() > 0){
            c.moveToFirst();
            object = cursorToClassObject(c);
        }
        if(c != null) c.close();

        db.close();
        if(object == null) callback.onDataNotAvailable();
        else callback.onClassLoaded(object);
    }

    @Override
    public void getWeekClasses(DayOfWeek week, @NonNull GetClassesCallback classesCallback) {
        ClassObject[] objects = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        String selection = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                + ANDCLAUSE + COLUMN_WEEK + WHERECLAUSE;
        String[] selectionArgs = {String.valueOf(tableId), week.toString()};
        db.beginTransaction();
        try {
            c = db.query(CLASSES_TABLE_NAME,null,selection,
                selectionArgs,null,null,COLUMN_STARTTIME);
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }

        if(c != null && c.getCount() >0){
            objects = new ClassObject[c.getCount()];
            int i = 0;
            while (c.moveToNext()){
                objects[i] = cursorToClassObject(c);
                i++;
            }
        }
        if(c != null) c.close();

        db.close();

        if (objects == null) classesCallback.onDataNotAvailable();
        else classesCallback.onClassesLoaded(objects);

    }

    @Override
    public int getClassId(DayOfWeek week, int startTime) {
        final int[] id = new int[1];
        getClass(week, startTime, new GetClassCallback() {
            @Override
            public void onClassLoaded(ClassObject classObject) {
                id[0] = classObject.getId();
            }

            @Override
            public void onDataNotAvailable() {
                id[0] = -1;
            }
        });
        return id[0];
    }

    @Override
    public int getClassId(final String className) {
        final int[] id = new int[1];
        getClass(className, new GetClassCallback() {
            @Override
            public void onClassLoaded(ClassObject classObject) {
                id[0] = classObject.getId();
            }

            @Override
            public void onDataNotAvailable() {
                id[0] = -1;
            }
        });
        return id[0];
    }


    @Override
    public void update(@NonNull ClassObject classObject,
                       @NonNull SaveClassCallback callback) {
        ContentValues values = classObjectToValues(classObject);

        int startTime = classObject.getStart();
        int section = classObject.getSection();


         int checkNum = check(classObject);

        if(checkNum<0){
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.beginTransaction();
            boolean isSaved = true;
            try{
                int id = classObject.getId();
                //delete data with id
                String whereClause = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                        + ANDCLAUSE + _ID + WHERECLAUSE;
                String whereArgs[] = { String.valueOf(tableId), String.valueOf(id) };
                db.delete(CLASSES_TABLE_NAME,whereClause,whereArgs);
                values.put(_ID,id);
                int limit = startTime + section - 1;
                while(startTime<=limit) {
                    isSaved = isSaved && (db.insertOrThrow(CLASSES_TABLE_NAME,null,values) > 0);
                    startTime++;
                    values.put(COLUMN_STARTTIME, startTime);
                }
                db.setTransactionSuccessful();
            }finally {
                db.endTransaction();
            }
            db.close();

            if(isSaved) {
                callback.onClassSaved();
                MyApp.notifyClassObservers(classObject.getWeek());
            } else callback.onClassSaveFailed(checkNum);
        }else{
            callback.onClassSaveFailed(checkNum);
        }

    }

    @Override
    public void save(@NonNull ClassObject classObject,
                     @NonNull SaveClassCallback callback) {
        ContentValues values = classObjectToValues(classObject);

        if(classObject.getId() > 0){
            update(classObject,callback);
            return;
        }

        values.put(_ID,ID_NUMBER);
        classObject.setId(ID_NUMBER);

        int startTime = classObject.getStart();
        int section = classObject.getSection();

        int checkNum = check(classObject);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean isSaved = true;
        if(checkNum<0){
            db.beginTransaction();
            try{
                int limit = startTime + section - 1;
                while(startTime<=limit){
                    isSaved = isSaved && (db.insertOrThrow(CLASSES_TABLE_NAME,null,values) > 0);
                    startTime++;
                    values.put(COLUMN_STARTTIME, startTime);
                }
                db.setTransactionSuccessful();
            }finally {
                db.endTransaction();
            }
            db.close();

            if (isSaved) {
                callback.onClassSaved();
                MyApp.notifyClassObservers(classObject.getWeek());
            } else callback.onClassSaveFailed(checkNum);
        }else{
            callback.onClassSaveFailed(checkNum);
        }
        incrementIdNumber();

    }

    @Override
    public void delete(@NonNull ClassObject classObject) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            String whereClasuse = COLUMN_CLASS_TABLE_ID + WHERECLAUSE
                    + ANDCLAUSE + COLUMN_CLASS_NAME + WHERECLAUSE;
            String[] whereArgs  = { String.valueOf(tableId), classObject.getClassName() };
            db.delete(CLASSES_TABLE_NAME, whereClasuse, whereArgs);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        db.close();
        MyApp.notifyClassObservers(classObject.getWeek());
    }

    @Override
    public void deleteAllData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            db.delete(ClassTableEntity.CLASSES_TABLE_NAME,null,null);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        db.close();
    }

    /**
     * @param cursor
     * @return ClassObject
     *
     * Translate Cursor to ClassObject.Do not call cursor#moveToFirst().
     *
     * !!!!!!!!!!---------Warning-------------!!!!!!!!!!!
     * ColumnIndex dose not identify.So if you change Table column order ,
     * absolutely check this columnIndexes.
     */
    private ClassObject cursorToClassObject(Cursor cursor){

        ClassObject co = new ClassObject();
        co.setId(cursor.getInt(1));
        co.setClassName(cursor.getString(2));
        co.setTeacherName(cursor.getString(3));
        co.setRoomName(cursor.getString(4));
        co.setWeek(DayOfWeek.valueOf(cursor.getString(5)));
        co.setStart(cursor.getInt(6));
        co.setSection(cursor.getInt(7));
        co.setAtt(cursor.getInt(8));
        co.setLate(cursor.getInt(9));
        co.setAbs(cursor.getInt(10));
        int themeId = cursor.getInt(11);
        co.setThemeColor(ThemeColor.getThemeColor(themeId));
        return co;
    }

    private ContentValues classObjectToValues(ClassObject co){

        ContentValues values = new ContentValues();

        int id = co.getId();

        String vClassName = co.getClassName();
        String vTeacherName = co.getTeacherName();
        String vRoomName = co.getRoomName();
        DayOfWeek vWeek = co.getWeek();
        int vStartTime = co.getStart();
        int vNumOfKoma = co.getSection();
        int vAttend = co.getAtt();
        int vLate = co.getLate();
        int vAbsent = co.getAbs();
        int vColor = co.getThemeColor().getThemeId();

        values.put(COLUMN_CLASS_TABLE_ID, tableId);
        values.put(_ID,id);
        values.put(COLUMN_CLASS_NAME,vClassName);
        values.put(COLUMN_TEACHER_NAME, vTeacherName);
        values.put(COLUMN_ROOM_NAME,vRoomName);
        values.put(COLUMN_WEEK, vWeek.toString());
        values.put(COLUMN_STARTTIME, vStartTime);
        values.put(COLUMN_SECTION,vNumOfKoma);
        values.put(COLUMN_ATTEND,vAttend);
        values.put(COLUMN_LATE,vLate);
        values.put(COLUMN_ABSENT,vAbsent);
        values.put(COLUMN_COLOR,vColor);

        return values;
    }

    private int check(ClassObject co){
        int id = co.getId();
        String name = co.getClassName();
        DayOfWeek week = co.getWeek();
        int time = co.getStart();
        int section = co.getSection();

        return check(id,name,week,time,section);
    }

    private int check(int id,String name,DayOfWeek week,int time,int section){

        int msgNum = -1;
        int nameId = getClassId(name);
        boolean check1 = name.isEmpty();
        boolean check2 = (time + section - 1) > ClassTablePreference.getInstance().getCountOfSection();

        if(check1) return ErrorMessageEntity.NO_CLASSNAME;
        else if(check2) return ErrorMessageEntity.OVERFLOW;

        if(id>1000 && nameId>0){
            /**
             * if id > 1000, this method is called by update().
             * else, called by save()
             */
            if(nameId != id){
                msgNum = ErrorMessageEntity.CLASSNAME_USED;
                return msgNum;
            }
        }

        int limit = time + section -1;
        int checkId;
        for (int i=time;i<=limit;i++){
            checkId = getClassId(week,i);
            if(checkId>0) {
                if (id != checkId) {
                    msgNum = ErrorMessageEntity.ALREADY_EXIST;
                    return msgNum;
                }
            }
        }
        return msgNum;
    }

    @Override
    public void notifySettingChanged() {

        tableId = sharedPreferences.getInt(
                context.getString(R.string.settings_pref_table_id_key), 0);
    }
}
