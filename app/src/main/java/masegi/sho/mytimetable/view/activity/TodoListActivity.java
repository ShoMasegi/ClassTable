package masegi.sho.mytimetable.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.CalendarToString;
import masegi.sho.mytimetable.data.repository.RestoreDataRepository;
import masegi.sho.mytimetable.data.source.RestoreLocalDataSource;
import masegi.sho.mytimetable.di.contract.TodoListContract;
import masegi.sho.mytimetable.domain.value.Task;
import masegi.sho.mytimetable.presenter.TodoListPresenter;
import masegi.sho.mytimetable.view.DividerItemDecoration;
import masegi.sho.mytimetable.view.adapters.MainTodoListAdapter;
import masegi.sho.mytimetable.view.helper.RecyclerItemTouchHelper;

import static masegi.sho.mytimetable.view.activity.TodoEditActivity.TODO_CLASSNAME_KEY;
import static masegi.sho.mytimetable.view.activity.TodoEditActivity.TODO_CREATE_KEY;
import static masegi.sho.mytimetable.view.fragment.TodoEditFragment.RESULT_REMOVED;
import static masegi.sho.mytimetable.view.fragment.TodoEditFragment.RESULT_SAVED;

public class TodoListActivity extends AppCompatActivity
        implements TodoListContract.Views, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @BindView(R.id.todoList_sub_parent) CoordinatorLayout layout;
    @BindView(R.id.todoList_sub) TextView sub;
    @BindView(R.id.todoList_fab_add) FloatingActionButton fab;
    @BindView(R.id.todoList_listView) RecyclerView recyclerView;
    @BindView(R.id.todoList_toolbar) Toolbar toolbar;

    public static final int TODOLIST_REQUEST_CODE = 3;
    public static final String TODOLIST_CLASSNAME_KEY = "TODOLIST_CLASSNAME_KEY";

    private String className;
    private ArrayList<Task> tasks = new ArrayList<>();
    private MainTodoListAdapter adapter;

    private RestoreDataRepository repository;
    private TodoListContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Todo");

        Intent intent = getIntent();
        className = intent.getStringExtra(TODOLIST_CLASSNAME_KEY);

        RestoreLocalDataSource restoreLocalDataSource = RestoreLocalDataSource.getInstance(getApplicationContext());
        repository = RestoreDataRepository.getInstance(restoreLocalDataSource);
        presenter = new TodoListPresenter(repository, (TodoListContract.Views)this);
        presenter.onCreate(className);
        this.setupViews();
    }

    private void setupViews() {

        sub.setText(className);
        adapter = new MainTodoListAdapter(this, tasks) {

            @Override
            protected void onTodoClicked(@NonNull Task task) {

                super.onTodoClicked(task);
                presenter.onItemClicked(task);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFABClicked();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {

                        fab.hide(true);
                    }
                    else if (dy < 0) {

                        fab.show(true);
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {

        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_SAVED || resultCode == RESULT_REMOVED) {

            presenter.backedFromEditActivity();
        }
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


    @Override
    public void setPresenter(@NonNull TodoListContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void setData(ArrayList data) {

        this.tasks = data;
        if (this.adapter != null) {

            this.adapter.notifyDataChanged(tasks);
        }
    }

    @Override
    public void showError() {

        Snackbar.make(layout,getString(R.string.error_get_todo),Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showDeletedTaskSnackBar(final Task item, final int position) {

        String text = item.getTaskName() + " removed from TODO.";
        Snackbar.make(layout, text, Snackbar.LENGTH_SHORT)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        adapter.restoreItem(item, position);
                        presenter.cancelDeleteTask(item);
                    }
                })
                .setActionTextColor(Color.GREEN)
                .show();
    }

    @Override
    public void startTodoEditActivity(Task item) {

        Intent intent = new Intent(this, TodoEditActivity.class);
        if(item != null) {

            intent.putExtra(TODO_CLASSNAME_KEY, item.getClassName());
            intent.putExtra(TODO_CREATE_KEY,
                    CalendarToString.calendarToString(item.getCreateDate()));
        }
        else {

            intent.putExtra(TODO_CLASSNAME_KEY, className);
        }
        startActivityForResult(intent,0);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder holder, int direction, int position) {

        final Task deletedTask = adapter.getItem(position);
        final int deletedIndex = position;
        adapter.removeItem(position);
        presenter.onSwipedToRemove(deletedTask, deletedIndex);
    }
}
