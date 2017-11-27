package masegi.sho.mytimetable.view.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.databinding.FragSettingsBinding;
import masegi.sho.mytimetable.di.contract.SettingsContract;
import masegi.sho.mytimetable.domain.value.DayOfWeek;
import masegi.sho.mytimetable.preferences.ClassTablePreference;
import masegi.sho.mytimetable.presenter.SettingsPresenter;
import masegi.sho.mytimetable.view.activity.EditClassTimeActivity;
import masegi.sho.mytimetable.view.activity.EditTableActivity;
import masegi.sho.mytimetable.view.activity.LicensesActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements SettingsContract.Views {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    private FragSettingsBinding binding;

    private SettingsContract.Presenter presenter;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_settings, container, false);
        View root = binding.getRoot();
        binding.setPresenter((SettingsPresenter) presenter);

        return root;
    }


    @Override
    public void setPresenter(@NonNull SettingsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showChooseDaysDialog() {

        DayOfWeek[] week = ClassTablePreference.getInstance().getDaysOfWeek();
        List weekList = Arrays.asList(week);
        final boolean[] isExistDays = new boolean[7];
        int i = 0;
        for (DayOfWeek day : DayOfWeek.values()) {

            isExistDays[i++] = weekList.contains(day) ? true : false;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                DayOfWeek.getWeekString(),
                isExistDays,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        isExistDays[which] = isChecked;
                    }
                })
                .setTitle(getString(R.string.dialog_days_of_week))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.setDaysOfWeekValue(isExistDays);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void showSetClassTimeActivity() {

        Intent intent = new Intent(getContext(), EditClassTimeActivity.class);
        startActivity(intent);
    }

    @Override
    public void showChooseTableDialog(int nowId) {
        final Map<Integer, String> tableNamesMap = presenter.getTableNames();

        final ArrayList<Integer> idsList = new ArrayList<>(tableNamesMap.keySet());
        String[] tableNames = tableNamesMap.values().toArray(new String[0]);

        final List<Integer> checkedIds = new ArrayList<>();
        checkedIds.add(idsList.indexOf(nowId));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setSingleChoiceItems(
                tableNames,
                idsList.indexOf(nowId),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkedIds.clear();
                        checkedIds.add(which);
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.setCurrentTableId(idsList.get(checkedIds.get(0)));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setTitle(getString(R.string.dialog_choose_table))
                .show();
    }

    @Override
    public void showEditTableActivity(int tableId) {

        Intent intent = new Intent(getContext(), EditTableActivity.class);
        intent.putExtra("tableId", tableId);
        startActivity(intent);
    }

    @Override
    public void showClassesCountDialog() {

        NumberPickerDialogFragment dialogFragment
                = new NumberPickerDialogFragment().createDialog(
                new NumberPickerDialogFragment.NumberPickerCallback() {
                    @Override
                    public void callback(int number) {
                        presenter.setCountOfClasses(number);
                        binding.numberOfClassDescription.setText(presenter.getCountOfClassesString());
                    }
                },
                getString(R.string.dialog_count_classes),
                1,
                8,
                ClassTablePreference.getInstance().getCountOfSection()
        );
        dialogFragment.show(getFragmentManager(), "classes_count_picker");
    }

    @Override
    public void showChooseAttendModeDialog() {

    }

    @Override
    public void onResume() {
        binding.settingSelectTableDescription.setText(
                presenter.getCurrentTableName());
        super.onResume();
    }

    @Override
    public void showLicensesListActivity() {

        Intent intent = new Intent(getActivity(), LicensesActivity.class);
        startActivity(intent);
    }

    @Override
    public void restartView() {

        Fragment fragment = getFragmentManager().findFragmentById(R.id.settings_container);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
        prepareRestartMainActivity();
    }

    @Override
    public void prepareRestartMainActivity() {
        getActivity().setResult(1);
    }
}
