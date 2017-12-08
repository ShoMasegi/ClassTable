package masegi.sho.mytimetable.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.data.source.RestoreLocalDataSource;
import masegi.sho.mytimetable.di.RestoreDataSource;
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.preferences.Preferences;


public class MemoEditActivity extends AppCompatActivity {

    public static final int MEMO_REQUEST_CODE = 1;
    public static final String MEMO_CLASSNAME_KEY = "MEMO_CLASSNAME_KEY";
    public static final String MEMO_CLASSCOLOR_KEY = "MEMO_CLASSCOLOR_KEY";
    public static final String MEMO_CONTENT_KEY = "MEMO_CONTENT_KEY";

    private String memo;
    private String className;
    private RestoreDataRepository repository;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        memo = intent.getStringExtra(MEMO_CONTENT_KEY);
        className = intent.getStringExtra(MEMO_CLASSNAME_KEY);
        int themeId = intent.getIntExtra(MEMO_CLASSCOLOR_KEY, -1);
        if (!(themeId < 0)) {

            ThemeColor themeColor = ThemeColor.getThemeColor(themeId);
            Preferences.applyTheme(this, themeColor);
        }
        setContentView(R.layout.activity_memo_edit);
        setupView();
        RestoreLocalDataSource dataSource = RestoreLocalDataSource.getInstance(getApplicationContext());
        repository = RestoreDataRepository.getInstance(dataSource);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void setupView() {

        toolbar = (Toolbar)findViewById(R.id.editMemo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.memo);

        final TextView textView = (TextView)findViewById(R.id.editMemo_sub);
        final EditText editText = (EditText)findViewById(R.id.editMemo_edit_text);
        final Button submitBtn = (Button)findViewById(R.id.detailMemo_submit_btn);
        if(memo != null) editText.setText(memo);
        editText.setSelection(editText.getText().length());
        textView.setText(className);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                memo = editText.getText().toString();
                repository.saveMemo(className, memo);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
