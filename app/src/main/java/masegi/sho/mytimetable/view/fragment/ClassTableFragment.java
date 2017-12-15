package masegi.sho.mytimetable.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.data.ClassDataSource;
import masegi.sho.mytimetable.di.contract.ClassTableContract;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.preferences.ClassTablePreference;
import masegi.sho.mytimetable.view.activity.DetailActivity;
import masegi.sho.mytimetable.view.activity.EditClassActivity;
import masegi.sho.mytimetable.view.customview.ClassTable;
import masegi.sho.mytimetable.view.adapters.ClassTableAdapter;
import masegi.sho.mytimetable.view.adapters.ClassTableAdapter.*;

import static masegi.sho.mytimetable.view.activity.EditClassActivity.EXTRA_CLASS_NAME;
import static masegi.sho.mytimetable.view.activity.EditClassActivity.EXTRA_CLASS_POSITION;


/**
 * Created by masegi on 2017/06/30.
 */

public class ClassTableFragment extends Fragment implements ClassTableContract.Views ,
        ClassTableAdapter.OnTableItemClickListener {

    public static final String TAG = ClassTableFragment.class.getSimpleName();

    private ClassTableContract.Presenter presenter;

    private ClassTableAdapter classTableAdapter;

    public ClassTableFragment(){
        //Required empty public constructor
    }

    public static ClassTableFragment newInstance(){ return new ClassTableFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        classTableAdapter = new ClassTableAdapter(getActivity(),(OnTableItemClickListener)this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        presenter.onCreate();
        View root = inflater.inflate(R.layout.frag_home,container,false);
        ClassTable timeTable = (ClassTable)root.findViewById(R.id.timetable);
        ClassTablePreference preference = ClassTablePreference.getInstance();
        timeTable.setWeek(preference.getDaysOfWeek())
                .setSectionCount(preference.getCountOfClasses())
                .setAdapter(classTableAdapter)
                .build();
        return root;
    }

    @Override
    public void onResume() {

        presenter.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {

        presenter.onDestroy();
        super.onDestroy();
    }


    @Override
    public void setPresenter(@NonNull ClassTableContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void startDetailActivity(ClassObject item) {

        if(item.getClassName() != null) {

            Intent intent = new Intent(getActivity(), DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailActivity.EXTRA_CLASS_OBJECT,item);
            intent.putExtra(DetailActivity.EXTRA_BUNDLE, bundle);
            startActivity(intent);
        }
    }

    @Override
    public void startEditActivity(@Nullable ClassObject item) {

        Intent intent = new Intent(getActivity(),EditClassActivity.class);
        intent.putExtra(EXTRA_CLASS_NAME, item.getClassName());
        intent.putExtra(EXTRA_CLASS_POSITION, item.getPosition());
        startActivity(intent);
    }

    @Override
    public void showPopupMenu(View view, ClassObject item) {

        PopupMenu popupMenu = new PopupMenu(getContext(),view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        popupMenu.setOnMenuItemClickListener(new MenuItemClickListener(item));
        if (item.getClassName() != null) {

            menuInflater.inflate(R.menu.menu_exist_class_object,popupMenu.getMenu());
        }
        else {

            menuInflater.inflate(R.menu.menu_class_object,popupMenu.getMenu());
        }
        popupMenu.show();
    }

    @Override
    public void setClassDataSource(ClassDataSource dataSource) {

        classTableAdapter.setItemsAndRefresh(dataSource, false);
    }


    @Override
    public void refresh(ClassDataSource dataSource) {

        classTableAdapter.setItemsAndRefresh(dataSource, true);
    }

    @Override
    public void showAlertsDialog(String className,
                                 final DeleteClassCallback callback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getResources().getString(R.string.delete_class_question) + className)
                .setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                callback.onDelete();
                            }
                        })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                callback.onCancel();
                            }
                        })
                .create().show();
    }


    /**
     * Override touched event of View in TimeTable
     *
     * @see ClassTableAdapter.OnTableItemClickListener#onTableItemClick(ClassObject)
     * @see ClassTableAdapter.OnTableItemClickListener#onTableItemLongClick(View, ClassObject)
     */
    @Override
    public void onTableItemClick(ClassObject item) {
        presenter.onTableItemClicked(item);
    }

    @Override
    public void onTableItemLongClick(View view,ClassObject item) {
        //onLongClick item
        presenter.onTableItemLongClicked(view,item);
    }



    /**
     * @see this#showPopupMenu(View, ClassObject)
     *
     * Override method performed when MenuItem clicked
     */
    private class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener{

        private ClassObject classObject;

        public MenuItemClickListener(@Nullable ClassObject classObject){

            this.classObject = classObject;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.menu_add:
                    presenter.onMenuAddClicked(classObject);
                    return true;
                case R.id.menu_delete:
                    presenter.onMenuDeleteClicked(classObject);
                    return true;
                case R.id.menu_edit:
                    presenter.onMenuEditClicked(classObject);
                    return true;
                default:
                    return false;
            }
        }
    }
}
