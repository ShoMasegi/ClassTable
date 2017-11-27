package masegi.sho.mytimetable.di;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/07/06.
 */

public interface RestoreDataSource {

    interface GetTaskCallback{
        void onTaskLoaded(ArrayList<Task> tasksList);
        void onDataNotAvailable();
    }

    void getAllTask(@NonNull String className,@NonNull GetTaskCallback callback);
    void getAllTask(@NonNull GetTaskCallback callback);
    void getTasks(@NonNull String className, boolean isCompleted,
                 @NonNull GetTaskCallback callback);
    Task getTask(@NonNull String className,String createDate);
    String getMemo(@NonNull String className);
    void saveTask(@NonNull Task task);
    void saveMemo(@NonNull String className,String memo);
    void updateTask(@NonNull Task task);
    void deleteTask(@NonNull Task task);
}
