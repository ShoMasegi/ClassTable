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
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.preferences.Preferences;


public class MemoEditActivity extends AppCompatActivity {

    public static final String MEMO_CLASSNAME_KEY = "MEMO_CLASSNAME_KEY";
    public static final String MEMO_CLASSCOLOR_KEY = "MEMO_CLASSCOLOR_KEY";
    public static final String MEMO_CONTENT_KEY = "MEMO_CONTENT_KEY";
    private String memo;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        memo = intent.getStringExtra(MEMO_CONTENT_KEY);
        String className = intent.getStringExtra(MEMO_CLASSNAME_KEY);
        int themeId = intent.getIntExtra(MEMO_CLASSCOLOR_KEY, -1);
        if (!(themeId < 0)) {

            ThemeColor themeColor = ThemeColor.getThemeColor(themeId);
            Preferences.applyTheme(this, themeColor);
        }
        setContentView(R.layout.activity_memo_edit);
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
                returnDetailActivity();
            }
        });
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

    private void returnDetailActivity() {

        Intent intent = new Intent();
        intent.putExtra(MEMO_CONTENT_KEY, memo);
        setResult(RESULT_OK, intent);
        finish();
    }

}
