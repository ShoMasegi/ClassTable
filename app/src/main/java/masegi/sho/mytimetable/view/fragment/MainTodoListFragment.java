package masegi.sho.mytimetable.view.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.CalendarToString;
import masegi.sho.mytimetable.di.contract.MainTodoListContract;
import masegi.sho.mytimetable.domain.value.Task;
import masegi.sho.mytimetable.view.activity.TodoEditActivity;
import masegi.sho.mytimetable.view.adapters.MainTodoListAdapter;
import masegi.sho.mytimetable.view.helper.RecyclerItemTouchHelper;

import static masegi.sho.mytimetable.view.activity.TodoEditActivity.TODO_CLASSNAME_KEY;
import static masegi.sho.mytimetable.view.activity.TodoEditActivity.TODO_CREATE_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainTodoListFragment extends Fragment implements MainTodoListContract.Views ,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    public static final String TAG = MainTodoListFragment.class.getSimpleName();
    private CoordinatorLayout layoutParent;
    private RecyclerView recyclerView;
    private RelativeLayout emptyStateParent;

    private MainTodoListAdapter adapter;
    private ArrayList<Task> data;
    private MainTodoListContract.Presenter presenter;

    public MainTodoListFragment() {
        // Required empty public constructor
    }

    public static MainTodoListFragment newInstance() {
        return new MainTodoListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        presenter.refreshData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_todo, container, false);
        layoutParent = (CoordinatorLayout) root.findViewById(R.id.todoList_parent);
        emptyStateParent = (RelativeLayout)root.findViewById(R.id.todoList_empty_state);
        recyclerView = (RecyclerView)root.findViewById(R.id.todoList_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new masegi.sho.mytimetable.view.DividerItemDecoration(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MainTodoListAdapter(getContext(),data){

            @Override
            protected void onTodoClicked(@NonNull Task task) {

                super.onTodoClicked(task);
                presenter.itemClicked(task);
            }
        };
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        showViews();

        return root;
    }


    @Override
    public void onPause() {

        super.onPause();
        presenter.onPause();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {

        super.onResume();
        presenter.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {

        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setPresenter(@NonNull MainTodoListContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void setData(ArrayList todoList) {

        this.data = todoList;
        if(adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {}

    @Override
    public void showViews() {

        if (emptyStateParent == null || recyclerView == null) return;
        if (data.size() > 0) {

            emptyStateParent.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {

            emptyStateParent.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void startTodoEditActivity(Task item) {

        Intent intent = new Intent(getActivity(), TodoEditActivity.class);
        intent.putExtra(TODO_CLASSNAME_KEY, item.getClassName());
        intent.putExtra(TODO_CREATE_KEY,
                    CalendarToString.calendarToString(item.getCreateDate()));
        startActivity(intent);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder holder, int direction, int position) {

        final Task deletedTask = adapter.getItem(position);
        final int deletedIndex = position;
        adapter.removeItem(position);
        presenter.addToRemoveList(deletedTask);
        Snackbar snackbar = Snackbar.make(
                layoutParent,
                deletedTask.getTaskName() + " removed from TODO.",
                Snackbar.LENGTH_SHORT
        );
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.restoreItem(deletedTask, deletedIndex);
                presenter.deleteFromRemoveList(deletedTask);
            }
        });
        snackbar.setActionTextColor(Color.GREEN)
                .show();
    }
}
