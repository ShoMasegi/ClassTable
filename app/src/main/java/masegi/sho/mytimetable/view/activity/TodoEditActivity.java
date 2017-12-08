package masegi.sho.mytimetable.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.data.source.RestoreLocalDataSource;
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.preferences.Preferences;
import masegi.sho.mytimetable.presenter.TodoEditPresenter;
import masegi.sho.mytimetable.view.fragment.TodoEditFragment;
import masegi.sho.mytimetable.util.ActivityUtils;


public class TodoEditActivity extends AppCompatActivity {


    public static final int TODO_REQUEST_CODE = 2;
    public static final String TODO_CLASSNAME_KEY = "TODO_CLASSNAME_KEY";
    public static final String TODO_CREATE_KEY = "TODO_CREATE_KEY";
    public static final String TODO_CLASSCOLOR_KEY = "TODO_CLASSCOLOR_KEY";

    private TodoEditPresenter todoEditPresenter;
    private String className;
    private String createDateKey;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        className = intent.getStringExtra(TODO_CLASSNAME_KEY);
        createDateKey = intent.getStringExtra(TODO_CREATE_KEY);
        int themeId = intent.getIntExtra(TODO_CLASSCOLOR_KEY, -1);
        if (!(themeId < 0)) {

            ThemeColor themeColor = ThemeColor.getThemeColor(themeId);
            Preferences.applyTheme(this, themeColor);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);

        toolbar = (Toolbar)findViewById(R.id.todoEdit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.todo));
        Bundle args = new Bundle();
        args.putString(TODO_CLASSNAME_KEY, className);
        if(createDateKey != null) {

            args.putString(TODO_CREATE_KEY, createDateKey);
        }
        RestoreLocalDataSource restoreDataSource = RestoreLocalDataSource.getInstance(getApplicationContext());
        RestoreDataRepository repository = RestoreDataRepository.getInstance(restoreDataSource);

        TodoEditFragment todoEditFragment =
                (TodoEditFragment)getSupportFragmentManager().findFragmentById(R.id.todoEdit_container);
        if(todoEditFragment == null){

            todoEditFragment = TodoEditFragment.newInstance();
            todoEditFragment.setArguments(args);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),todoEditFragment,R.id.todoEdit_container);
        }
        todoEditPresenter = new TodoEditPresenter(repository,todoEditFragment);
        focusToBackground();
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

    private void focusToBackground(){
        //when tap screen, focus on background layout.
        //add focusableInTouchMode = "true" in layout file ViewGroup
        final InputMethodManager inputMethodManager
                = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        final LinearLayout parentLayout = (LinearLayout)findViewById(R.id.todoEdit_parent);
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(parentLayout.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                parentLayout.requestFocus();
            }
        });
    }

}
