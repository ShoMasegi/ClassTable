package masegi.sho.mytimetable.view.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.data.repository.PrefsRepository;
import masegi.sho.mytimetable.di.contract.SettingsClassTimeContract;
import masegi.sho.mytimetable.domain.value.ClassTime;
import masegi.sho.mytimetable.presenter.SettingsClassTimePresenter;
import masegi.sho.mytimetable.util.ActivityUtils;
import masegi.sho.mytimetable.view.adapters.ClassTimeRecyclerViewAdapter;
import masegi.sho.mytimetable.view.fragment.EditClassTimeFragment;

public class EditClassTimeActivity extends AppCompatActivity {

    private SettingsClassTimeContract.Presenter presenter;

    private int classCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class_time);

        Toolbar toolbar = (Toolbar)findViewById(R.id.edit_class_time_toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.settings_class_time);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        PrefsRepository prefsRepository = PrefsRepository.getInstance(getApplicationContext());

        Fragment editClassTimeFragment =
                (EditClassTimeFragment)getSupportFragmentManager().findFragmentById(R.id.edit_class_time_container);
        if (editClassTimeFragment == null) {
            editClassTimeFragment = EditClassTimeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    editClassTimeFragment,
                    R.id.edit_class_time_container
            );
        }
        presenter = new SettingsClassTimePresenter(
                this, prefsRepository, (SettingsClassTimeContract.Views)editClassTimeFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
