package masegi.sho.mytimetable.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.di.contract.SettingsClassTimeContract;
import masegi.sho.mytimetable.domain.value.ClassTime;
import masegi.sho.mytimetable.preferences.ClassTablePreference;
import masegi.sho.mytimetable.view.DividerItemDecoration;
import masegi.sho.mytimetable.view.adapters.ClassTimeRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditClassTimeFragment extends Fragment implements SettingsClassTimeContract.Views{

    private SettingsClassTimeContract.Presenter presenter;

    private RecyclerView recyclerView;
    private ClassTimeRecyclerViewAdapter adapter;

    private Map<Integer, ClassTime> classTimeMap;
    private int count;


    public EditClassTimeFragment() {
        // Required empty public constructor
    }

    public static EditClassTimeFragment newInstance() {

        EditClassTimeFragment fragment = new EditClassTimeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        classTimeMap = presenter.getClassTimes();
        count = ClassTablePreference.getInstance().getCountOfClasses();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_edit_class_time, container, false);
        recyclerView = (RecyclerView)root.findViewById(R.id.edit_class_time_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new ClassTimeRecyclerViewAdapter(classTimeMap, count){

            @Override
            protected void onClassTimeClicked(ClassTime time) {

                super.onClassTimeClicked(time);
                showSetTimeDialog(time);
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        return root;
    }

    @Override
    public void onPause() {

        super.onPause();
        presenter.onPause(classTimeMap);
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
    public void setPresenter(@NonNull SettingsClassTimeContract.Presenter presenter) {

        this.presenter = presenter;
    }


    private void showSetTimeDialog(ClassTime time) {

        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.setTime(time)
                .setCallback(new TimePickerFragment.GetTimeCallback() {
                    @Override
                    public void onGetClassTime(ClassTime time) {

                        classTimeMap.put(time.getPeriodNum(), time);
                    }
                })
                .show(getFragmentManager(), "TimePickerDialog");
    }
}
