package masegi.sho.mytimetable.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import masegi.sho.mytimetable.data.ClassDataSource;
import masegi.sho.mytimetable.domain.entity.ClassTableEntity;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.domain.value.DayOfWeek;
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.preferences.ClassTablePreference;
import masegi.sho.mytimetable.view.customview.ClassTable;

/**
 * Created by masegi on 2017/06/16.
 */

public class ClassTableAdapter {

    protected final Context context;
    private final OnTableItemClickListener onTableItemClickListener;
    private ClassDataSource dataSource;
    private ClassTable classTable;

    public ClassTableAdapter(Context context, OnTableItemClickListener onTableItemClickListener){
        this.context = context;
        this.onTableItemClickListener = onTableItemClickListener;
    }

    public void setItemsAndRefresh(ClassDataSource dataSource, boolean isRefresh){

        this.dataSource = dataSource;
        if (classTable == null) return;
        if (isRefresh) {

            ClassTablePreference preference = ClassTablePreference.getInstance();
            classTable.removeAllViews();
            classTable.setSectionCount(preference.getCountOfSection())
                    .setWeek(preference.getDaysOfWeek())
                    .build();
        } else {

            classTable.setContentData();
        }
    }

    public void setClassTable(ClassTable classTable){

        this.classTable = classTable;
    }

    public ClassObject getItemAt(DayOfWeek day, int start){

        ClassObject item = dataSource.getClassObject(day, start);
        if (item != null) {

            return item;
        } else {

            return new ClassObject(null, day, start);
        }
    }

    public ClassViewHolder createViewHolder(ViewGroup parent){

        final View view = LayoutInflater.from(context).inflate(R.layout.view_class_object,parent,false);
        return new ClassViewHolder(view);
    }

    public void bindView(View childView, DayOfWeek day, int start) {

        ClassViewHolder holder = new ClassViewHolder(childView);
        bindViewHolder(holder, day, start);
    }

    public void bindViewHolder(final ClassViewHolder holder, DayOfWeek day, int start){

        final ClassObject item = getItemAt(day, start);

        /**
         * @see masegi.sho.mytimetable.view.fragment.ClassTableFragment
         * implements {@see this#OnTableItemClickListener }
         *
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTableItemClickListener.onTableItemClick(item);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onTableItemClickListener.onTableItemLongClick(v, item);
                return false;
            }
        });

        holder.setClassObject(item);
    }


    public interface OnTableItemClickListener{
        void onTableItemClick(ClassObject item);
        void onTableItemLongClick(View view,ClassObject item);
    }


    public class ClassViewHolder{
        private final TextView tClassName;
        private final TextView tRoomName;
        private final View colorView;
        public final View itemView;

        private ClassObject object;

        public ClassViewHolder(View itemView){
            this.itemView = itemView;
            tClassName = (TextView)itemView.findViewById(R.id.class_name);
            tRoomName = (TextView)itemView.findViewById(R.id.room_name);
            colorView = (View)itemView.findViewById(R.id.class_color);
        }

        public void setClassObject(ClassObject classObject){

            if (classObject == null) return;

            this.object = classObject;
            tClassName.setText(classObject.getClassName());
            tRoomName.setText(object.getRoomName());
            int colorResId = classObject.getThemeColor().getPrimaryColorResId();
            colorView.setBackgroundColor(ContextCompat.getColor(context, colorResId));

            //set background color with border
            int scale = (int)context.getResources().getDisplayMetrics().density;
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke((int)(scale * 1.5f), Color.parseColor("#95989A"));
            drawable.setColor(Color.WHITE);
            itemView.setBackground(drawable);
        }
    }
}
