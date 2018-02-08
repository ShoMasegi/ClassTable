package masegi.sho.mytimetable.domain.value;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Calendar;

import masegi.sho.mytimetable.BR;
import masegi.sho.mytimetable.Utils.CalendarUtil;

import static masegi.sho.mytimetable.domain.entity.RestoreDataEntity.TASK_COMPLETED;

/**
 * Created by masegi on 2017/07/06.
 */

public class Task extends BaseObservable {

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
                Calendar dueDate, ThemeColor themeColor) {

        this.className = className;
        this.taskName = taskName;
        this.content = content;
        this.dueDate = dueDate;
        this.createDate = Calendar.getInstance();
        this.themeColor = themeColor;
    }
    public Task(String className, String taskName, String content,
                String dueDate, ThemeColor themeColor) {

        this(className,taskName, content,
                CalendarUtil.stringToCalendar(dueDate), themeColor);
    }
    public Task(String className, String taskName, String content, String dueDate) {

        this(className, taskName, content, dueDate, ThemeColor.DEFAULT);
    }
    public Task(String className, String taskName, String content) {

        this(className, taskName, content, Calendar.getInstance(), ThemeColor.DEFAULT);
    }
    public Task(String className, String taskName){
        this(className,taskName,null);
    }
    public Task(String className){
        this(className,null);
    }

    public String getClassName() {

        return this.className;
    }
    @Bindable
    public String getTaskName() {

        return this.taskName;
    }
    @Bindable
    public String getTaskContent() {

        return this.content;
    }
    @Bindable
    public Calendar getDueDate() {

        return this.dueDate;
    }
    @Bindable
    public Calendar getCreateDate() {

        return this.createDate;
    }
    public String getDueDateString() {

        return CalendarUtil.calendarToDueDate(this.dueDate);
    }
    public String getCreateDateString() {

        return CalendarUtil.calendarToCreateDate(this.createDate);
    }
    public ThemeColor getThemeColor() {

        return themeColor;
    }
    public boolean isCompleted() {

        return this.isCompleted;
    }

    public void setClassName(String className) {

        this.className = className;
    }
    public void setTaskName(String taskName) {

        this.taskName = taskName;
        notifyPropertyChanged(BR.taskName);
    }
    public void setTaskContent(String content) {

        this.content = content;
        notifyPropertyChanged(BR.taskContent);
    }
    public void setDueDate(String dueDate) {

        this.dueDate = CalendarUtil.stringToCalendar(dueDate);
        notifyPropertyChanged(BR.dueDate);
    }
    public void setDueDate(Calendar dueDateCalendar) {

        this.dueDate = dueDateCalendar;
        notifyPropertyChanged(BR.dueDate);
    }
    public void setCreateDate(String createDate) {

        this.createDate = CalendarUtil.stringToCalendar(createDate);
        notifyPropertyChanged(BR.createDate);
    }
    public void setCreateDate(Calendar createDateCalendar) {

        this.createDate = createDateCalendar;
        notifyPropertyChanged(BR.createDate);
    }
    public void setThemeColor(ThemeColor themeColor) {

        this.themeColor = themeColor;
    }
    public void setCompleted(int isCompleted) {

        this.isCompleted = (isCompleted == TASK_COMPLETED ? true : false);
    }
}
