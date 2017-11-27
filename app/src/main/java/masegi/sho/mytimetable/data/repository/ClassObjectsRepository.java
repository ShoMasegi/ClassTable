package masegi.sho.mytimetable.data.repository;


import android.support.annotation.NonNull;

import masegi.sho.mytimetable.data.ClassDataSource;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.DayOfWeek;
import masegi.sho.mytimetable.di.ClassDataResources;

/**
 * Created by masegi on 2017/06/24.
 */

public class ClassObjectsRepository implements ClassDataResources {

    private static ClassObjectsRepository INSTANCE = null;
    private final ClassDataResources classLocalDataSource;


    private ClassObjectsRepository(@NonNull ClassDataResources classLocalDataSource){
        this.classLocalDataSource = classLocalDataSource;
    }

    /**
     * Returns the single instance of this class, creating it ig necessary.
     *
     * @param classLocalDataSource the device storage data source
     * @return the {@link ClassObjectsRepository} instance
     */
    public static ClassObjectsRepository getInstance(ClassDataResources classLocalDataSource){
        if (INSTANCE == null){
            INSTANCE = new ClassObjectsRepository(classLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }


    @Override
    public void getAllClasses(@NonNull final GetAllClassesCallback callback) {
        classLocalDataSource.getAllClasses(new GetAllClassesCallback() {
            @Override
            public void onClassesLoaded(ClassDataSource classDataSource) {
                callback.onClassesLoaded(classDataSource);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getClass(@NonNull String className, @NonNull final GetClassCallback callback) {
        classLocalDataSource.getClass(className, new GetClassCallback() {
            @Override
            public void onClassLoaded(ClassObject classObject) {
                callback.onClassLoaded(classObject);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getClass(DayOfWeek week, int startTime, @NonNull final GetClassCallback callback) {
        classLocalDataSource.getClass(week, startTime, new GetClassCallback() {
            @Override
            public void onClassLoaded(ClassObject classObject) {
                callback.onClassLoaded(classObject);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getWeekClasses(DayOfWeek week, @NonNull final GetClassesCallback classesCallback) {
        classLocalDataSource.getWeekClasses(week, new GetClassesCallback() {
            @Override
            public void onClassesLoaded(ClassObject[] classObjects) {
                classesCallback.onClassesLoaded(classObjects);
            }

            @Override
            public void onDataNotAvailable() {
                classesCallback.onDataNotAvailable();
            }
        });
    }

    @Override
    public int getClassId(DayOfWeek week, int startTime) {
        return classLocalDataSource.getClassId(week,startTime);
    }

    @Override
    public int getClassId(String className) {
        return classLocalDataSource.getClassId(className);
    }

    @Override
    public void update(@NonNull ClassObject classObject, @NonNull final SaveClassCallback callback) {
        classLocalDataSource.update(classObject, new SaveClassCallback() {
            @Override
            public void onClassSaved() {
                callback.onClassSaved();
            }

            @Override
            public void onClassSaveFailed(int num) {
                callback.onClassSaveFailed(num);
            }
        });
    }

    @Override
    public void save(@NonNull ClassObject classObject, @NonNull final SaveClassCallback callback) {
        classLocalDataSource.save(classObject, new SaveClassCallback() {
            @Override
            public void onClassSaved() {
                callback.onClassSaved();
            }

            @Override
            public void onClassSaveFailed(int num) {
                callback.onClassSaveFailed(num);
            }
        });
    }

    @Override
    public void delete(@NonNull ClassObject classObject) {
        classLocalDataSource.delete(classObject);
    }

    @Override
    public void deleteAllData() {
        classLocalDataSource.deleteAllData();
    }
}
