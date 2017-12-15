package masegi.sho.mytimetable.view.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import masegi.sho.mytimetable.BR;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.databinding.ItemClassCardBinding;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.ClassTime;
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.preferences.ClassTablePreference;


/**
 * Created by masegi on 2017/11/14.
 */

public class ClassCardAdapter extends RecyclerView.Adapter<ClassCardAdapter.CardClassViewHolder> {

    private ClassObject[] dataSet;
    private HashMap<String,String> memoDataSet;
    private Map<Integer, ClassTime> timeMap;
    private Context context;

    public ClassCardAdapter(Context context, ClassObject[] dataSet, HashMap memoDataSet) {

        this.context = context;
        this.dataSet = dataSet;
        this.memoDataSet = memoDataSet;
        this.timeMap = ClassTablePreference.getInstance().getClassTimeMap();
    }

    @Override
    public ClassCardAdapter.CardClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_class_card,parent,false);
        final CardClassViewHolder vh = new CardClassViewHolder(view);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClassObject item = dataSet[vh.getAdapterPosition()];
                onItemClicked(item);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(CardClassViewHolder holder, int position) {

        if (this.dataSet == null) {

            return;
        }
        ClassObject object = this.dataSet[position];
        ClassTime time = this.timeMap.get(object.getStart());
        holder.getBinding().setVariable(BR.object, object);
        holder.getBinding().setVariable(BR.time, time);
        holder.getBinding().setVariable(BR.memo, memoDataSet.get(object.getClassName()));
        holder.getBinding().executePendingBindings();
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int scale = (int)(context.getResources().getDisplayMetrics().density*1.0f);
        ThemeColor themeColor = object.getThemeColor();
        if (themeColor == ThemeColor.DEFAULT) {

            drawable.setStroke(scale, ContextCompat.getColor(context, themeColor.getPrimaryColorDarkResId()));
        }
        drawable.setColor(ContextCompat.getColor(context, themeColor.getPrimaryColorResId()));
        holder.getBinding().cardViewColor.setBackground(drawable);
    }

    @Override
    public int getItemCount() {

        if (dataSet == null) {

            return 0;
        }
        else {

            return dataSet.length;
        }
    }

    protected void onItemClicked(ClassObject item) { }

    public void setDataSet(ClassObject[] dataSet,HashMap memoDataSet) {

        this.dataSet = dataSet;
        this.memoDataSet = memoDataSet;
    }

    public class CardClassViewHolder extends RecyclerView.ViewHolder {

        private ItemClassCardBinding binding;

        public CardClassViewHolder(View itemView) {

            super(itemView);
            this.binding = DataBindingUtil.bind(itemView);
        }

        public ItemClassCardBinding getBinding() {

            return this.binding;
        }
    }
}
