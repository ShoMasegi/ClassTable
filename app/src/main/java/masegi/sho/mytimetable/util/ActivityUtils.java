package masegi.sho.mytimetable.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by masegi on 2017/07/03.
 */

public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container. view with id {@code frameId}.
     * The operation is performed by the {@code fragmentManager}.
     */

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment,int frameId){
        if(fragmentManager!=null && fragment!=null){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(frameId,fragment);
            transaction.commit();
        }
    }

}
