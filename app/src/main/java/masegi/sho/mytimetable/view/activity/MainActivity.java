package masegi.sho.mytimetable.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.data.source.ClassLocalDataResources;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.data.source.RestoreLocalDataSource;
import masegi.sho.mytimetable.di.contract.ClassListContract;
import masegi.sho.mytimetable.di.contract.ClassTableContract;
import masegi.sho.mytimetable.di.contract.MainTodoListContract;
import masegi.sho.mytimetable.presenter.ClassListPresenter;
import masegi.sho.mytimetable.presenter.ClassTablePresenter;
import masegi.sho.mytimetable.data.repository.ClassObjectsRepository;
import masegi.sho.mytimetable.presenter.MainTodoListPresenter;
import masegi.sho.mytimetable.view.fragment.AboutMeDialogFragment;
import masegi.sho.mytimetable.view.fragment.ClassTableFragment;
import masegi.sho.mytimetable.view.fragment.MainTodoListFragment;
import masegi.sho.mytimetable.view.fragment.TodayClassesFragment;

public class MainActivity extends AppCompatActivity {

    private ClassTablePresenter classTablePresenter;
    private ClassListPresenter classListPresenter;
    private MainTodoListPresenter mainTodoListPresenter;

    private ClassLocalDataResources localClassesDataSource;
    private ClassObjectsRepository classesRepository;
    private RestoreLocalDataSource localRestoreDataSource;
    private RestoreDataRepository restoreRepository;


    private Fragment tableFragment;
    private Fragment todayFragment;
    private Fragment mainTodoListFragment;

    @BindView(R.id.main_navigation)BottomNavigationView navigation;
    @BindView(R.id.main_toolbar)Toolbar toolbar;
    @BindView(R.id.main_tab)TabLayout tabLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.nav_home:
                    switchFragment(tableFragment,ClassTableFragment.TAG);
                    break;
                case R.id.nav_today:
                    switchFragment(todayFragment,TodayClassesFragment.TAG);
                    break;
                case R.id.nav_todo:
                    switchFragment(mainTodoListFragment,MainTodoListFragment.TAG);
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        tabLayout.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        initFragment(savedInstanceState);

        localClassesDataSource = ClassLocalDataResources.getInstance(getApplicationContext());
        classesRepository = ClassObjectsRepository.getInstance(localClassesDataSource);
        localRestoreDataSource = RestoreLocalDataSource.getInstance(getApplicationContext());
        restoreRepository = RestoreDataRepository.getInstance(localRestoreDataSource);


        classTablePresenter = new ClassTablePresenter(
                classesRepository,(ClassTableContract.Views) tableFragment);

        classListPresenter = new ClassListPresenter(
                classesRepository,restoreRepository,(ClassListContract.Views)todayFragment);

        mainTodoListPresenter = new MainTodoListPresenter(
                restoreRepository,(MainTodoListContract.Views)mainTodoListFragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_about:
                FragmentManager manager = getSupportFragmentManager();
                AboutMeDialogFragment dialog = AboutMeDialogFragment.newInstance();
                dialog.show(manager, "AboutMe");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        if (switchFragment(tableFragment, ClassTableFragment.TAG)) {

            navigation.setSelectedItemId(R.id.nav_home);
            return;
        }
        super.onBackPressed();
    }


    private void initFragment(Bundle savedInstanceState){
        final FragmentManager manager = getSupportFragmentManager();
        todayFragment = manager.findFragmentByTag(TodayClassesFragment.TAG);
        tableFragment = manager.findFragmentByTag(ClassTableFragment.TAG);
        mainTodoListFragment = manager.findFragmentByTag(MainTodoListFragment.TAG);

        if(todayFragment == null){
            todayFragment = TodayClassesFragment.newInstance();
        }
        if (tableFragment == null){
            tableFragment = ClassTableFragment.newInstance();
        }
        if (mainTodoListFragment == null){
            mainTodoListFragment = MainTodoListFragment.newInstance();
        }

        if (savedInstanceState == null){
            switchFragment(tableFragment,ClassTableFragment.TAG);
            navigation.setSelectedItemId(R.id.nav_home);
        }
    }

    private boolean switchFragment(@NonNull Fragment fragment,@NonNull String tag){
        if (fragment.isAdded()){
            return false;
        }

        final FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        final Fragment currentFragment = manager.findFragmentById(R.id.content);
        if (currentFragment != null){
            if (currentFragment instanceof TodayClassesFragment)
                tabLayout.setVisibility(View.GONE);
            transaction.detach(currentFragment);
        }
        if(fragment.isDetached()){
            transaction.attach(fragment);
        }else{
            transaction.add(R.id.content,fragment,tag);
        }

        if (fragment instanceof TodayClassesFragment)
            tabLayout.setVisibility(View.VISIBLE);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

        manager.executePendingTransactions();

        return true;
    }
}