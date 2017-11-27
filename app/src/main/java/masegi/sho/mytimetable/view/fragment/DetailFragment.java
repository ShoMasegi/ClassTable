package masegi.sho.mytimetable.view.fragment;


import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.CalendarToString;
import masegi.sho.mytimetable.api.OrdinalNumber;
import masegi.sho.mytimetable.di.contract.DetailContract;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.Task;
import masegi.sho.mytimetable.view.activity.MemoEditActivity;
import masegi.sho.mytimetable.view.activity.TodoEditActivity;
import masegi.sho.mytimetable.view.activity.TodoListActivity;

import static android.app.Activity.RESULT_OK;
import static masegi.sho.mytimetable.view.activity.MemoEditActivity.*;
import static masegi.sho.mytimetable.view.activity.TodoEditActivity.*;
import static masegi.sho.mytimetable.view.activity.TodoListActivity.TODOLIST_CLASSNAME_KEY;
import static masegi.sho.mytimetable.view.fragment.TodoEditFragment.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements DetailContract.Views {

    @BindView(R.id.content_edit_scroll) NestedScrollView scrollView;
    @BindView(R.id.detail_teacher_name) TextView teacherTextView;
    @BindView(R.id.detail_room_name) TextView roomNameTextView;
    @BindView(R.id.detail_week) TextView weekTextView;
    @BindView(R.id.detail_start) TextView startTextView;
    @BindView(R.id.detail_section) TextView sectionTextView;
    @BindView(R.id.detail_attend) TextView attendTextView;
    @BindView(R.id.detail_late) TextView lateTextView;
    @BindView(R.id.detail_abs) TextView absTextView;
    @BindView(R.id.detail_list_content) ListView todoList;
    @BindView(R.id.detail_todo_list) RelativeLayout todoListParent;
    @BindView(R.id.no_task_text) TextView noTaskText;
    @BindView(R.id.detail_todo_more) TextView moreView;
    @BindView(R.id.detail_memo) TextView memoTextView;
    private FloatingActionMenu menuFab;
    private FloatingActionButton todoFab;
    private FloatingActionButton memoFab;
    private CoordinatorLayout layout;

    private ClassObject object;
    private ArrayList<Task> tasks;
    private TasksAdapter listAdapter;
    private String memo;

    private static final int MEMO_REQUEST_CODE = 1;
    private static final int TODO_REQUEST_CODE = 2;
    private static final int TODOLIST_REQUEST_CODE = 3;

    private DetailContract.Presenter detailPresenter;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance() {

        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        listAdapter = new TasksAdapter(tasks, taskItemListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_detail, container, false);
        ButterKnife.bind(this, root);
        menuFab = (FloatingActionMenu)getActivity().findViewById(R.id.detail_fab_menu);
        todoFab = (FloatingActionButton)getActivity().findViewById(R.id.detail_fab1);
        memoFab = (FloatingActionButton)getActivity().findViewById(R.id.detail_fab2);
        layout = (CoordinatorLayout)getActivity().findViewById(R.id.detail_coordinator_layout);
        setupViews();
        return root;
    }

    private void setupViews() {

        if (object.getTeacherName().isEmpty()) {

            teacherTextView.setText("No Name");
        }
        else {

            teacherTextView.setText(object.getTeacherName());
        }
        if (object.getRoomName().isEmpty()) {

            roomNameTextView.setText("No Location");
        }
        else {

            roomNameTextView.setText(object.getRoomName());
        }
        weekTextView.setText(object.getWeek().getWeekName());
        String period = OrdinalNumber.ordinalNumberString(object.getStart()) + " Period";
        startTextView.setText(period);
        sectionTextView.setText(String.valueOf(object.getSection()));
        attendTextView.setText(String.valueOf(object.getAtt()));
        lateTextView.setText(String.valueOf(object.getLate()));
        absTextView.setText(String.valueOf(object.getAbs()));
        todoList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        if (tasks.size() > 0) {

            showTasksViews();
            if(tasks.size() > 3) {

                moreView.setVisibility(View.VISIBLE);
            }
            else {

                moreView.setVisibility(View.GONE);
            }
        }
        else showNoTasksViews();
        showMemo(memo);
        moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailPresenter.clickTaskMore();
            }
        });
        menuFab.setClosedOnTouchOutside(true);
        todoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuFab.close(true);
                detailPresenter.addNewTask();
            }
        });
        memoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuFab.close(true);
                detailPresenter.clickMemoView(memo);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {

                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {

                        menuFab.hideMenuButton(true);
                    }
                    else if (scrollY < oldScrollY) {

                        menuFab.showMenuButton(true);
                    }
                }

            });
        }
    }

    private void showNoTasksViews() {

        noTaskText.setVisibility(View.VISIBLE);
        todoList.setVisibility(View.GONE);
        moreView.setVisibility(View.GONE);
    }

    private void showTasksViews() {

        noTaskText.setVisibility(View.GONE);
        todoList.setVisibility(View.VISIBLE);
    }


    @Override
    public void setPresenter(@NonNull DetailContract.Presenter presenter) {
        this.detailPresenter = presenter;
    }

    @Override
    public void prepareData(ClassObject classObject) {
        this.object = classObject;
    }

    @Override
    public void prepareTask(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void prepareMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public void showTasks(ArrayList<Task> tasks) {

        listAdapter.replaceData(tasks);

        if (tasks.size() > 0) {

            todoList.setVisibility(View.VISIBLE);
            noTaskText.setVisibility(View.GONE);
            if(tasks.size() > 3) {

                moreView.setVisibility(View.VISIBLE);
            }
            else {

                moreView.setVisibility(View.GONE);
            }
        }
        else {

            todoList.setVisibility(View.GONE);
            noTaskText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMemo(String memo) {

        if (memo == null) {

            memoTextView.setText(R.string.no_memo);
            memoTextView.setGravity(Gravity.CENTER);
            memoTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.char_default));
        }
        else if(memo.isEmpty()) {

            memoTextView.setText(R.string.no_memo);
            memoTextView.setGravity(Gravity.CENTER);
            memoTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.char_default));
        }
        else {

            memoTextView.setText(memo);
            memoTextView.setGravity(Gravity.NO_GRAVITY);
            memoTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.char_black));
        }
    }

    @Override
    public void showSnackBar(int messageId) {

        Snackbar.make(layout,getString(messageId),Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case(MEMO_REQUEST_CODE):
                if(resultCode == RESULT_OK){

                    memo = data.getStringExtra(MEMO_CONTENT_KEY);
                    detailPresenter.saveMemoAndRefresh(memo);
                }
                break;
            case(TODO_REQUEST_CODE):
                if (resultCode == RESULT_SAVED){

                    detailPresenter.saveTodoAndRefresh();
                }
                if (resultCode == RESULT_REMOVED){

                    detailPresenter.removeTodoAndRefresh();
                }
                break;
            case(TODOLIST_REQUEST_CODE):
                detailPresenter.saveTodoAndRefresh();
            default:
                break;
        }
    }

    @Override
    public void startMemoEditActivity(String memo) {

        Intent intent = new Intent(getActivity(), MemoEditActivity.class);
        intent.putExtra(MEMO_CONTENT_KEY,memo);
        intent.putExtra(MEMO_CLASSCOLOR_KEY, object.getThemeColor().getThemeId());
        intent.putExtra(MEMO_CLASSNAME_KEY, object.getClassName());
        startActivityForResult(intent,MEMO_REQUEST_CODE);
    }


    @Override
    public void startTodoEditActivity(Task task) {

        Intent intent = new Intent(getActivity(),TodoEditActivity.class);
        intent.putExtra(TODO_CLASSNAME_KEY, object.getClassName());
        intent.putExtra(TODO_CLASSCOLOR_KEY, object.getThemeColor().getThemeId());
        if (task != null) {

            intent.putExtra(TODO_CREATE_KEY,
                    CalendarToString.calendarToString(task.getCreateDate()));
        }
        startActivityForResult(intent,TODO_REQUEST_CODE);
    }

    @Override
    public void startTodoListActivity() {

        Intent intent = new Intent(getActivity(), TodoListActivity.class);
        intent.putExtra(TODOLIST_CLASSNAME_KEY,object.getClassName());
        startActivityForResult(intent, TODOLIST_REQUEST_CODE);
    }



    TaskItemListener taskItemListener = new TaskItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {
            detailPresenter.clickTaskItem(clickedTask);
        }

        @Override
        public void onCompletedTaskClick(Task completedTask) {
        }

        @Override
        public void onActiveTaskClick(Task activeTask) {
        }
    };

    private class TasksAdapter extends BaseAdapter {

        private ArrayList<Task> tasks;
        private TaskItemListener itemListener;

        public TasksAdapter(@NonNull ArrayList<Task> tasks,
                            TaskItemListener itemListener) {

            this.tasks = tasks;
            this.itemListener = itemListener;
        }

        public void replaceData(@NonNull ArrayList<Task> tasks) {

            this.tasks = tasks;
            notifyDataSetChanged();
        }

        @Override
        public void notifyDataSetChanged() {

            super.notifyDataSetChanged();
            int itemCount = getCount();
            if (itemCount > 0 && itemCount <= 3) {

                View item = getView(0, null, todoList);
                item.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                int itemHeight = item.getMeasuredHeight();
                ViewGroup.LayoutParams params = todoListParent.getLayoutParams();
                params.height = itemCount * itemHeight;
                todoListParent.setLayoutParams(params);
            }
            else if (itemCount <= 0) {

                ViewGroup.LayoutParams params = todoListParent.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                todoListParent.setLayoutParams(params);
            }
        }

        @Override
        public int getCount() {

            if(tasks.size() > 3) return 3;
            else return tasks.size();
        }

        @Override
        public Task getItem(int position) {
            return tasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            if (view == null) {

                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_detail_todo, parent, false);
            }
            final Task task = getItem(position);

            View colorView = (View) view.findViewById(R.id.list_item_color);
            TextView taskName = (TextView) view.findViewById(R.id.list_item_taskName);
            TextView dueDate = (TextView) view.findViewById(R.id.list_item_dueDate);

            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            int colorResId = task.getThemeColor().getPrimaryColorResId();
            drawable.setColor(ContextCompat.getColor(getContext(),colorResId));
            colorView.setBackground(drawable);
            taskName.setText(task.getTaskName());
            dueDate.setText(CalendarToString.calendarToSimpleDate(task.getDueDate()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onTaskClick(task);
                }
            });

            return view;
        }
    }

    public interface TaskItemListener {
        void onTaskClick(Task clickedTask);

        void onCompletedTaskClick(Task completedTask);

        void onActiveTaskClick(Task activeTask);
    }
}
