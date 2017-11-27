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

import masegi.sho.mytimetable.data.repository.ClassObjectsRepository;
import masegi.sho.mytimetable.data.source.ClassLocalDataResources;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.presenter.EditClassPresenter;
import masegi.sho.mytimetable.view.fragment.EditClassFragment;
import masegi.sho.mytimetable.util.ActivityUtils;

public class EditClassActivity extends AppCompatActivity{

    public static final String EXTRA_CLASS_NAME = "EXTRA_CLASS_NAME";
    public static final String EXTRA_CLASS_POSITION = "EXTRA_CLASS_POSITION";

    private EditClassPresenter editClassPresenter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        toolbar = (Toolbar)findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        String className = intent.getStringExtra(EXTRA_CLASS_NAME);
        int[] position = intent.getIntArrayExtra(EXTRA_CLASS_POSITION);

        ClassLocalDataResources localDataSource = ClassLocalDataResources.getInstance(getApplicationContext());
        ClassObjectsRepository repository = ClassObjectsRepository.getInstance(localDataSource);
        EditClassFragment editClassFragment =
                (EditClassFragment)getSupportFragmentManager().findFragmentById(R.id.edit_content);
        if(editClassFragment == null){
            editClassFragment = EditClassFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), editClassFragment,R.id.edit_content);
        }

        editClassPresenter = new EditClassPresenter(repository, editClassFragment);
        focusToBackground();
        editClassPresenter.prepare(className, position);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void focusToBackground(){
        //when tap screen, focus on background layout.
        final InputMethodManager inputMethodManager
                = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        final LinearLayout parentLayout = (LinearLayout)findViewById(R.id.edit_parent);
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
