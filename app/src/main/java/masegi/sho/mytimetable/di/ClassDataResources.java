package masegi.sho.mytimetable.di;

import android.support.annotation.NonNull;

import masegi.sho.mytimetable.data.ClassDataSource;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.DayOfWeek;

/**
 * Created by masegi on 2017/06/30.
 */

public interface ClassDataResources {

    interface GetAllClassesCallback{
        void onClassesLoaded(ClassDataSource dataSource);
        void onDataNotAvailable();
    }

    interface GetClassesCallback{
        void onClassesLoaded(ClassObject[] classObjects);
        void onDataNotAvailable();
    }

    interface GetClassCallback{
        void onClassLoaded(ClassObject classObject);
        void onDataNotAvailable();
    }

    interface SaveClassCallback{
        void onClassSaved();
        void onClassSaveFailed(int num);
    }



    void getAllClasses(@NonNull GetAllClassesCallback callback);
    void getClass(@NonNull String className,@NonNull GetClassCallback callback);
    void getClass(DayOfWeek week,int startTime,@NonNull GetClassCallback callback);
    void getWeekClasses(DayOfWeek week,@NonNull GetClassesCallback classesCallback);
    int getClassId(DayOfWeek week,int startTime);
    int getClassId(String className);
    void update(@NonNull ClassObject classObject,@NonNull SaveClassCallback callback);
    void save(@NonNull ClassObject classObject,@NonNull SaveClassCallback callback);
    void delete(@NonNull ClassObject classObject);
    void deleteAllData();

}
