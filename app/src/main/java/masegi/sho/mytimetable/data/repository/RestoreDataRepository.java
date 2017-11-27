package masegi.sho.mytimetable.data.repository;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import masegi.sho.mytimetable.di.RestoreDataSource;
import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/07/07.
 */

public class RestoreDataRepository implements RestoreDataSource{

    private static RestoreDataRepository INSTANCE = null;
    private final RestoreDataSource restoreDataSource;

    private RestoreDataRepository(@NonNull RestoreDataSource restoreDataSource){
        this.restoreDataSource = restoreDataSource;
    }

    /**
     * Returns the single instance of this class, creating it ig necessary.
     *
     * @param restoreDataSource the device storage data source
     * @return the {@link ClassObjectsRepository} instance
     */
    public static RestoreDataRepository getInstance(RestoreDataSource restoreDataSource){
        if(INSTANCE == null){
            INSTANCE = new RestoreDataRepository(restoreDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance(){ INSTANCE = null; }
    @Override
    public void getAllTask(@NonNull String className, @NonNull final GetTaskCallback callback) {
        restoreDataSource.getAllTask(className, callback);
    }

    @Override
    public void getAllTask(@NonNull GetTaskCallback callback) {
        restoreDataSource.getAllTask(callback);
    }

    @Override
    public void getTasks(@NonNull String className,boolean isCompleted,@NonNull GetTaskCallback callback) {
        restoreDataSource.getTasks(className,isCompleted,callback);
    }

    @Override
    public Task getTask(@NonNull String className, String createDate) {
        return restoreDataSource.getTask(className,createDate);
    }

    @Override
    public String getMemo(@NonNull String className) {
        return restoreDataSource.getMemo(className);
    }

    @Override
    public void saveTask(@NonNull Task task) {
        restoreDataSource.saveTask(task);
    }

    @Override
    public void saveMemo(@NonNull String className, String memo) {
        restoreDataSource.saveMemo(className,memo);
    }

    @Override
    public void updateTask(@NonNull Task task) {
        restoreDataSource.updateTask(task);
    }

    @Override
    public void deleteTask(@NonNull Task task) {
        restoreDataSource.deleteTask(task);
    }
}
