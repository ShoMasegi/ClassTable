package masegi.sho.mytimetable.view.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.data.repository.PrefsRepository;

public class EditTableActivity extends AppCompatActivity{

    private PrefsRepository prefsRepository;
    private int currentTableId;
    private ListView listView;

    private ArrayAdapter<String> arrayAdapter;

    private Map<Integer, String> tableNameMap;
    private List<String> tableNameList;
    private List<Integer> tableIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_table);

        listView = (ListView)findViewById(R.id.table_edit_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.table_edit_fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.table_edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        currentTableId = sharedPref.getInt(getString(R.string.settings_pref_table_id_key), 0);

        prefsRepository = PrefsRepository.getInstance(getApplicationContext());
        tableNameMap = prefsRepository.getTableNames();

        tableNameList = new ArrayList<>(tableNameMap.values());
        tableIdList = new ArrayList<>(tableNameMap.keySet());

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tableNameList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                showOptionsMenu(position);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View dialogView = LayoutInflater.from(EditTableActivity.this).inflate(R.layout.dialog_create_table, null);
                final EditText editText = (EditText)dialogView.findViewById(R.id.dialog_create_table_edit);

                new AlertDialog.Builder(EditTableActivity.this)
                        .setTitle(R.string.dialog_create_table)
                        .setView(dialogView)
                        .setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                prefsRepository.createTable(editText.getText().toString());
                                syncAdapterSource();
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });
    }

    private void syncAdapterSource() {

        tableIdList = new ArrayList<>(prefsRepository.getTableNames().keySet());
        tableNameList = new ArrayList<>(prefsRepository.getTableNames().values());
        arrayAdapter.clear();
        arrayAdapter.addAll(tableNameList);
    }

    private void showOptionsMenu(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditTableActivity.this);
        builder.setTitle(tableNameList.get(position))
                .setItems(R.array.edit,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    showEditTableNameDialog(tableIdList.get(position));
                                } else {
                                    showDeleteConfirmDialog(tableIdList.get(position));
                                }
                            }
                        }
                ).show();
    }

    private void showEditTableNameDialog(final int tableId) {

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_table, null);
        final EditText editText = (EditText)dialogView.findViewById(R.id.dialog_create_table_edit);
        editText.setText(tableNameMap.get(tableId));

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_table_name)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        prefsRepository.setTableName(tableId, editText.getText().toString());
                        syncAdapterSource();
                        arrayAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showDeleteConfirmDialog(final int tableId) {

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_table_delete)
                .setMessage(R.string.dialog_table_delete_message)
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (currentTableId == tableId) {
                            showErrorMessage();
                            return;
                        } else {
                            prefsRepository.deleteTable(tableId);
                            syncAdapterSource();
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showErrorMessage() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_error)
                .setMessage(R.string.error_delete_table)
                .setPositiveButton(android.R.string.ok, null)
                .show();
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
}
