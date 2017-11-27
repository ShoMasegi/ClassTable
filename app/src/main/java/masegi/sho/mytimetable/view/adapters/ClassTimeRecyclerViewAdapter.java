package masegi.sho.mytimetable.view.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Map;

import masegi.sho.mytimetable.BR;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.domain.entity.ClassTableEntity;
import masegi.sho.mytimetable.domain.value.ClassTime;

/**
 * Created by masegi on 2017/10/22.
 */

public class ClassTimeRecyclerViewAdapter
        extends RecyclerView.Adapter<ClassTimeRecyclerViewAdapter.ItemViewHolder> {

    protected void onClassTimeClicked(ClassTime time) {}

    private Map<Integer, ClassTime> classTimeMap;
    private int rowCount;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public ItemViewHolder(View view) {

            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public ViewDataBinding getBinding() {

            return binding;
        }
    }

    public ClassTimeRecyclerViewAdapter(Map<Integer, ClassTime> classTimeMap, int rowCount) {

        this.classTimeMap = classTimeMap;
        this.rowCount = rowCount;
    }

    @Override
    public int getItemCount() {

        return rowCount;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_time, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        ClassTime time = null;
        if (classTimeMap != null) {

            time = classTimeMap.get(position + 1);
        }
        if (time == null) {

            time = new ClassTime(position + 1);
        }
        final ClassTime finalTime = time;
        holder.getBinding().getRoot().setClickable(true);
        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onClassTimeClicked(finalTime);
            }
        });
        holder.getBinding().setVariable(BR.time, time);
        holder.getBinding().executePendingBindings();
    }

}
