package masegi.sho.mytimetable.di.contract;

import android.support.annotation.NonNull;

/**
 * Created by masegi on 2017/06/30.
 */

public interface BaseView<T>{
    void setPresenter(@NonNull T presenter);
}
