package masegi.sho.mytimetable.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import masegi.sho.mytimetable.data.ClassDataSource;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.DayOfWeek;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.view.adapters.ClassTableAdapter;

/**
 * Created by masegi on 2017/06/09.
 */

public class ClassTable extends GridLayout {

    private Context context;

    public float scale;

    private int widthParent;
    private static int width0;
    private static int width1;
    private static int height0;
    private static int height1;
    private int marginSize;

    private int sectionCount;
    private int dayCount;
    private DayOfWeek[] week;

    private ClassTableAdapter adapter;


    public ClassTable(Context context){
        this(context,null,0);
    }

    public ClassTable(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClassTable(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        this.context = context;

        scale = getResources().getDisplayMetrics().density;
        int paddingSize = (int)(scale * 18);
        widthParent = getResources().getDisplayMetrics().widthPixels - paddingSize;
    }


    public ClassTable setWeek(DayOfWeek[] week){
        this.week = week;
        dayCount = week.length;
        setMetrics(dayCount);

        return this;
    }

    public ClassTable setSectionCount(int count){
        this.sectionCount = count;
        return this;
    }

    public ClassTable setAdapter(ClassTableAdapter adapter) {
        this.adapter = adapter;
        adapter.setClassTable(this);
        return this;
    }

    public void build(){
        setUpView();
    }

    private void setMetrics(int count){

        int wWeight = count > 5 ? 5 : count;
        this.marginSize = (int)scale / 2;
        widthParent = widthParent - marginSize * count;
        this.width0 = widthParent / 20;
        this.width1 = (widthParent - width0) / wWeight;
        this.height0 = width1 / 4;
        if (count > 4) this.height1 = width1 * 5 / 4;
        else this.height1 = width1;
    }


    private void setUpView(){

        removeAllViews();

        setRowCount(sectionCount + 1);
        setColumnCount(dayCount + 1);

        setUpFrame();
        setUpContent();
    }

    private void setUpFrame(){

        GridLayout.Spec rowSpec;
        GridLayout.Spec colSpec;
        GridLayout.LayoutParams child_params;
        TextView child;

        for(int colNum = 0; colNum < dayCount + 1; colNum++){

            for (int rowNum = 0; rowNum < sectionCount + 1 ; rowNum++){

                colSpec = GridLayout.spec(colNum);
                rowSpec = GridLayout.spec(rowNum);
                child_params = new GridLayout.LayoutParams(rowSpec,colSpec);
                float textSize = sectionCount > 4 ? 11.0f : 12.0f;

                if(colNum == 0){

                    if (rowNum == 0){

                        child = new TextView(context);
                        child.setTextSize(textSize);
                        child.setGravity(Gravity.CENTER);
                        child_params.width = width0;
                        child_params.height = height0;
                    }
                    else {

                        child = new TextView(context);
                        child.setText(String.valueOf(rowNum));
                        child.setTextSize(12.0f);
                        child.setGravity(Gravity.CENTER);
                        child_params.width = width0;
                        child_params.height = height1;
                    }
                    child_params.setMargins(marginSize, marginSize, marginSize, marginSize);
                    child.setBackgroundResource(R.drawable.simple_border);
                    addView(child, child_params);
                }
                else{

                    if (rowNum == 0){

                        child = new TextView(context);
                        child.setTextSize(textSize);
                        child.setText(week[colNum - 1].toString());
                        child.setGravity(Gravity.CENTER);
                        child_params.width = width1;
                        child_params.height = height0;
                        child_params.setMargins(marginSize ,marginSize, marginSize, marginSize);
                        child.setBackgroundResource(R.drawable.simple_border);
                        addView(child, child_params);
                    }
                }
            }
        }
    }

    public void setUpContent(){

        GridLayout.Spec rowSpec;
        GridLayout.Spec colSpec;
        GridLayout.LayoutParams child_params;
        ClassTableAdapter.ClassViewHolder holder;

        for (int colNum = 1; colNum < dayCount + 1; colNum++){

            colSpec = GridLayout.spec(colNum);
            DayOfWeek day = week[colNum - 1];
            for (int rowNum = 1; rowNum < sectionCount + 1; rowNum++){

                rowSpec = GridLayout.spec(rowNum);
                child_params = new GridLayout.LayoutParams(rowSpec, colSpec);
                child_params.width = width1;
                child_params.height = height1;
                child_params.setMargins(marginSize, marginSize, marginSize, marginSize);
                holder = adapter.createViewHolder(this);

                adapter.bindViewHolder(holder, day, rowNum);
                addView(holder.itemView, child_params);
            }
        }
    }

    public void setContentData() {

        for (int colNum = 1; colNum < dayCount + 1; colNum++) {

            DayOfWeek day = week[colNum - 1];
            for (int rowNum = 1; rowNum < sectionCount + 1; rowNum++) {

                int offset = dayCount + sectionCount;
                int position = offset + (colNum - 1) * sectionCount + rowNum;
                View childView = getChildAt(position);
                adapter.bindView(childView, day, rowNum);
            }
        }
    }
}
