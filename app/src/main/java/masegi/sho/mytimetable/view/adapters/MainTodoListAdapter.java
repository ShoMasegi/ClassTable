package masegi.sho.mytimetable.view.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.Utils.CalendarUtil;
import masegi.sho.mytimetable.domain.value.Task;

/**
 * Created by masegi on 2017/10/13.
 */

public class MainTodoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<Task> todoData;
    private ArrayList<Task> previousData;

    private LayoutInflater inflater;
    private Context context;

    public MainTodoListAdapter(Context context, ArrayList<Task> data){

        this.inflater = LayoutInflater.from(context);
        this.previousData = data;
        this.todoData = insertDateData(previousData);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_ITEM:
                final HolderViewHolder holder = new HolderViewHolder(inflater
                        .inflate(R.layout.todo_list_holder,parent,false));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Task task = todoData.get(holder.getAdapterPosition());
                        onTodoClicked(task);
                    }
                });
                return holder;

            case TYPE_SEPARATOR:
                return new SeparatorHolder(inflater
                        .inflate(R.layout.todo_list_header,parent,false));

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {

        Task item = todoData.get(position);

        if (item.getClassName() == null) {

            if (todoData.size() > position && item != null) {

                ((SeparatorHolder)holder).headerView.setText(item.getTaskName());
            }
        }else{

            if (todoData.size() > position && item != null) {

                String classNameString = "/ " + item.getClassName();
                String contentString = ": " + item.getTaskContent();
                ((HolderViewHolder)holder).taskNameView.setText(item.getTaskName());
                ((HolderViewHolder)holder).classNameView.setText(classNameString);
                ((HolderViewHolder)holder).contentView.setText(contentString);
                ((HolderViewHolder)holder).dueTimeView.setText(
                        CalendarUtil.calendarToSimpleTime(item.getDueDate()));
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.OVAL);
                int colorResId = item.getThemeColor().getPrimaryColorResId();
                drawable.setColor(ContextCompat.getColor(context,colorResId));
                ((HolderViewHolder)holder).colorView.setBackground(drawable);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        return todoData.get(position).getClassName() == null ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return todoData.size();
    }

    public Task getItem(int position) {

        return todoData.get(position);
    }

    protected void onTodoClicked(@NonNull Task task) {}

    public void removeItem(int position) {

        todoData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Task task, int position) {

        todoData.add(position, task);
        notifyItemInserted(position);
    }

    public void notifyDataChanged(ArrayList data) {

        this.previousData = data;
        this.todoData = insertDateData(this.previousData);
        notifyDataSetChanged();
    }


    public class SeparatorHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.todoList_header) TextView headerView;

        public SeparatorHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public class HolderViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.todoList_item_color) View colorView;
        @BindView(R.id.todoList_item_taskName) TextView taskNameView;
        @BindView(R.id.todoList_item_classname) TextView classNameView;
        @BindView(R.id.todoList_item_content) TextView contentView;
        @BindView(R.id.todoList_item_dueTime) TextView dueTimeView;
        @BindView(R.id.todoList_item_foreground)
        public RelativeLayout foregroundView;
        @BindView(R.id.todoList_item_background)
        public RelativeLayout backgroundView;

        public HolderViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }


    public ArrayList<Task> insertDateData(ArrayList<Task> data){

        if (data == null) return null;

        ArrayList<Task> dataWithDate = new ArrayList<>();
        String date = "yyyyMMdd";
        String taskDate;
        int i = 0;
        for (Task task : data){

            taskDate = CalendarUtil.calendarToString(task.getDueDate()).substring(0,8);
            if (!(date.equals(taskDate))){

                Task dateTask = new Task(null, CalendarUtil.calendarToTodoDate(task.getDueDate()));
                dataWithDate.add(i++,dateTask);
                date = taskDate;
            }
            dataWithDate.add(i,task);
            i++;
        }
        return dataWithDate;
    }
}
