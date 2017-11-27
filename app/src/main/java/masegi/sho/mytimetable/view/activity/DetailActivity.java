package masegi.sho.mytimetable.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.data.repository.ClassObjectsRepository;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.data.source.ClassLocalDataResources;
import masegi.sho.mytimetable.data.source.RestoreLocalDataSource;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.preferences.Preferences;
import masegi.sho.mytimetable.presenter.DetailPresenter;
import masegi.sho.mytimetable.view.fragment.DetailFragment;
import masegi.sho.mytimetable.util.ActivityUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_CLASS_OBJECT = "EXTRA_CLASS_OBJECT";
    public static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";

    private ClassObject classObject;

    private DetailPresenter detailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
        classObject = bundle.getParcelable(EXTRA_CLASS_OBJECT);
        Preferences.applyTheme(this, classObject.getThemeColor());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        RestoreLocalDataSource restoreLocalDataSource = RestoreLocalDataSource.getInstance(getApplicationContext());
        RestoreDataRepository restoreRepository = RestoreDataRepository.getInstance(restoreLocalDataSource);

        DetailFragment detailFragment =
                (DetailFragment)getSupportFragmentManager().findFragmentById(R.id.detail_content);
        if(detailFragment == null){
            detailFragment = DetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),detailFragment,R.id.detail_content);
        }

        detailPresenter = new DetailPresenter(restoreRepository,detailFragment);
        setUpViews();
        detailPresenter.prepare(classObject);
    }

    private void setUpViews(){

        Toolbar toolbar = (Toolbar)findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        setTitle(classObject.getClassName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
