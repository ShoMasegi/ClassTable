package masegi.sho.mytimetable.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.data.repository.PrefsRepository;
import masegi.sho.mytimetable.di.contract.SettingsContract;
import masegi.sho.mytimetable.presenter.SettingsPresenter;
import masegi.sho.mytimetable.util.ActivityUtils;
import masegi.sho.mytimetable.view.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = SettingsActivity.class.getSimpleName();

    private SettingsContract.Presenter settingsPresenter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar)findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        PrefsRepository repository = PrefsRepository.getInstance(getApplicationContext());

        SettingsFragment settingsFragment =
                (SettingsFragment)getSupportFragmentManager().findFragmentById(R.id.settings_container);
        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    settingsFragment,
                    R.id.settings_container
            );
        }

        settingsPresenter = new SettingsPresenter(repository, settingsFragment);
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
