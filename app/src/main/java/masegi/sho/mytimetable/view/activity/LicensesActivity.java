package masegi.sho.mytimetable.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.domain.value.Library;
import masegi.sho.mytimetable.domain.value.License;
import masegi.sho.mytimetable.view.adapters.LicensesListViewAdapter;

public class LicensesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;

    private List<Library> libraries;

    private LicensesListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenses);

        toolbar = (Toolbar)findViewById(R.id.license_toolbar);
        listView = (ListView)findViewById(R.id.license_list_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        prepareData();
        adapter = new LicensesListViewAdapter(this,libraries);
        listView.setAdapter(adapter);
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


    private void prepareData() {

        libraries = new ArrayList<>();

        Library butterKnife = new Library(
                "JakeWharton/butterknife",
                "Copyright 2013 Jake Wharton",
                License.Apache);

        Library fab = new Library(
                "Clans/FloatingActionButton",
                "Copyright Clans",
                License.Apache);

        Library materialAbout = new Library(
                "jrvansuita/MaterialAbout",
                "Copyright (c) 2016 Arleu Cezar Vansuita Júnior",
                License.MIT);

        libraries.add(butterKnife);
        libraries.add(fab);
        libraries.add(materialAbout);
    }
}
