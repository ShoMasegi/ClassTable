package masegi.sho.mytimetable.view.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.OrdinalNumber;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.ClassTime;
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.preferences.ClassTablePreference;


/**
 * Created by masegi on 2017/11/14.
 */

public class ClassCardAdapter extends RecyclerView.Adapter<ClassCardAdapter.ViewHolder>{

        private ClassObject[] dataSet;
        private HashMap<String,String> memoDataSet;
        private Map<Integer, ClassTime> timeMap;
        private Context context;

        public ClassCardAdapter(Context context, ClassObject[] dataSet, HashMap memoDataSet){

            this.context = context;
            this.dataSet = dataSet;
            this.memoDataSet = memoDataSet;
            this.timeMap = ClassTablePreference.getInstance().getClassTimeMap();
        }

        @Override
        public ClassCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context)
                                       .inflate(R.layout.item_class_card,parent,false);
            final ViewHolder vh = new ViewHolder(view);
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
        public void onBindViewHolder(ClassCardAdapter.ViewHolder holder, int position) {

            if (dataSet == null) return;

            ClassObject object = dataSet[position];
            holder.tClassName.setText(object.getClassName());
            if (object.getTeacherName().isEmpty()) {

                holder.tTeacher.setText("No Name");
            }
            else {

                holder.tTeacher.setText(object.getTeacherName());
            }
            if (object.getRoomName().isEmpty()) {

                holder.tLocation.setText("No Location");
            }
            else {

                holder.tLocation.setText(object.getRoomName());
            }
            String period = OrdinalNumber.ordinalNumberString(object.getStart());
            holder.tTimeSection.setText(period);
            ClassTime time = timeMap.get(object.getStart());
            if (time != null) {

                String timeText = time.getStartTime() + " ~ " + time.getEndTime();
                holder.tTime.setText(timeText);
            }
            else {

                holder.tTime.setVisibility(View.GONE);
            }
            holder.tMemo.setText(memoDataSet.get(object.getClassName()));

            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            int scale = (int)(context.getResources().getDisplayMetrics().density*1.0f);
            ThemeColor themeColor = object.getThemeColor();
            if (themeColor == ThemeColor.DEFAULT) {

                drawable.setStroke(scale, ContextCompat.getColor(context,R.color.colorBorder));
            }
            drawable.setColor(ContextCompat.getColor(context, themeColor.getPrimaryColorResId()));
            holder.tColorView.setBackground(drawable);
        }

        @Override
        public int getItemCount() {
            if (dataSet == null) return 0;
            else return dataSet.length;
        }

        protected void onItemClicked(ClassObject item) { }

        public void setDataSet(ClassObject[] dataSet,HashMap memoDataSet){
            this.dataSet = dataSet;
            this.memoDataSet = memoDataSet;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.card_view_className) TextView tClassName;
            @BindView(R.id.card_view_sectionTime) TextView tTimeSection;
            @BindView(R.id.card_view_time) TextView tTime;
            @BindView(R.id.card_view_teacher) TextView tTeacher;
            @BindView(R.id.card_view_location) TextView tLocation;
            @BindView(R.id.card_view_memo) TextView tMemo;
            @BindView(R.id.card_view_color) View tColorView;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
