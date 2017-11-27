package masegi.sho.mytimetable.domain.value;

import android.graphics.Color;

import java.util.Calendar;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.CalendarToString;

import static masegi.sho.mytimetable.domain.entity.RestoreDataEntity.TASK_COMPLETED;

/**
 * Created by masegi on 2017/07/06.
 */

public class Task {

    private String className;
    private String taskName;
    private String content;
    private Calendar dueDate;
    private Calendar createDate;
    private ThemeColor themeColor = ThemeColor.DEFAULT;

    /**
     * To save in sqlite_database ,isCompleted is integer.
     * @see masegi.sho.mytimetable.domain.entity.RestoreDataEntity
     * completed : TASK_COMPLETED
     * not completed : TASK_NOT_COMPLETED
     */
    private boolean isCompleted;


    public Task(String className, String taskName, String content,
                Calendar dueDate, ThemeColor themeColor){
        this.className = className;
        this.taskName = taskName;
        this.content = content;
        this.dueDate = dueDate;
        this.createDate = Calendar.getInstance();
        this.themeColor = themeColor;

    }
    public Task(String className, String taskName, String content,
                String dueDate, ThemeColor themeColor){

        this(className,taskName, content,
                CalendarToString.stringToCalendar(dueDate), themeColor);
    }
    public Task(String className, String taskName, String content, String dueDate){
        this(className, taskName, content, dueDate, ThemeColor.DEFAULT);
    }
    public Task(String className, String taskName, String content){
        this(className, taskName, content, Calendar.getInstance(), ThemeColor.DEFAULT);
    }
    public Task(String className, String taskName){
        this(className,taskName,null);
    }
    public Task(String className){
        this(className,null);
    }

    public String getClassName(){ return this.className; }
    public String getTaskName(){ return this.taskName; }
    public String getTaskContent(){ return this.content; }
    public Calendar getDueDate(){ return this.dueDate; }
    public Calendar getCreateDate(){ return this.createDate; }
    public String getDueDateString(){ return CalendarToString.calendarToDueDate(this.dueDate); }
    public String getCreateDateString(){ return CalendarToString.calendarToCreateDate(this.createDate); }
    public ThemeColor getThemeColor() { return themeColor; }
    public boolean isCompleted(){ return this.isCompleted; }

    public void setClassName(String className){ this.className = className; }
    public void setTaskName(String taskName){ this.taskName = taskName; }
    public void setTaskContent(String content){ this.content = content; }
    public void setDueDate(String dueDate){
        this.dueDate = CalendarToString.stringToCalendar(dueDate);
    }
    public void setDueDate(Calendar dueDateCalendar){
        this.dueDate = dueDateCalendar;
    }
    public void setCreateDate(String createDate){
        this.createDate = CalendarToString.stringToCalendar(createDate);
    }
    public void setCreateDate(Calendar createDateCalendar){
        this.createDate = createDateCalendar;
    }
    public void setThemeColor(ThemeColor themeColor) { this.themeColor = themeColor; }
    public void setCompleted(int isCompleted){
        this.isCompleted = (isCompleted == TASK_COMPLETED ? true : false);
    }

}
