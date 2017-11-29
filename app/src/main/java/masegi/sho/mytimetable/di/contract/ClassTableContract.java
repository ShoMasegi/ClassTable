package masegi.sho.mytimetable.di.contract;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import masegi.sho.mytimetable.data.ClassDataSource;
import masegi.sho.mytimetable.domain.value.ClassObject;

/**
 * Created by masegi on 2017/06/30.
 */

public interface ClassTableContract {
    interface Views extends BaseView<Presenter>{
        void startDetailActivity(@NonNull ClassObject item);
        void startEditActivity(@Nullable ClassObject item);
        void showPopupMenu(View view,ClassObject item);
        void setClassDataSource(ClassDataSource dataSource);
        void refresh(ClassDataSource dataSource);
        void showAlertsDialog(String className, DeleteClassCallback callback);

        interface DeleteClassCallback{
            void onDelete();
            void onCancel();
        }
    }
    interface Presenter extends BasePresenter{
        void onTableItemClicked(ClassObject item);
        void onTableItemLongClicked(View view, ClassObject item);
        void onMenuAddClicked(@Nullable ClassObject item);
        void onMenuEditClicked(@NonNull ClassObject item);
        void onMenuDeleteClicked(@NonNull ClassObject item);
        void onResume();
        void onDestroy();
    }
}
