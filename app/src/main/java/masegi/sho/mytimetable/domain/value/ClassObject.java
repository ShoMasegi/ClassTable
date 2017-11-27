package masegi.sho.mytimetable.domain.value;

import android.os.Parcel;
import android.os.Parcelable;

import masegi.sho.mytimetable.R;

/**
 * Created by masegi on 2017/06/16.
 */

public class ClassObject implements Parcelable {

    private int id;
    private String className;
    private int start;
    private DayOfWeek week;
    private int section;
    private String roomName;
    private String teacherName;
    private int att;
    private int late;
    private int abs;
    private ThemeColor themeColor = ThemeColor.DEFAULT;


    /*--------        Constructor        ------------*/

    public ClassObject(String className,
            DayOfWeek week, int start, int section,
            String roomName, String teacherName, ThemeColor themeColor) {

        this.className = className;
        this.week = week;
        this.start = start;
        this.section = section;
        this.roomName = roomName;
        this.teacherName = teacherName;
        this.themeColor = themeColor;
    }
    public ClassObject(String className, DayOfWeek week, int start, int section){
        this(className,
                week, start, section,
                null,null, ThemeColor.DEFAULT);
    }
    public ClassObject(String className,DayOfWeek week,int start){
        this(className,week,start,1);
    }
    public ClassObject(String className,DayOfWeek week){
        this(className,week,0);
    }
    public ClassObject() {}

    private ClassObject(Parcel in) {

        this.id = in.readInt();
        this.className = in.readString();
        this.start = in.readInt();
        this.week = DayOfWeek.getWeekByOrdinal(in.readInt());
        this.section = in.readInt();
        this.roomName = in.readString();
        this.teacherName = in.readString();
        this.att = in.readInt();
        this.late = in.readInt();
        this.abs = in.readInt();
        this.themeColor = ThemeColor.getThemeColor(in.readInt());
    }


    /*-------        getter & setter        --------------*/

    public int getId(){ return this.id; }
    public String getClassName(){ return this.className; }
    public DayOfWeek getWeek(){ return  this.week; }
    public int getStart(){ return  this.start; }
    public int getSection(){ return this.section; }
    public String getRoomName(){ return this.roomName; }
    public String getTeacherName(){ return this.teacherName; }
    public int getAtt(){ return this.att; }
    public int getLate(){ return this.late; }
    public int getAbs(){ return this.abs; }
    public int[] getPosition(){ return new int[]{getWeek().ordinal(),getStart()};}
    public ThemeColor getThemeColor() { return this.themeColor; }

    public void setId(int id){ this.id = id; }
    public void setClassName(String className){ this.className = className; }
    public void setWeek(DayOfWeek week){
        this.week = week;
    }
    public void setStart(int start){ this.start = start; }
    public void setSection(int section){ this.section = section; }
    public void setRoomName(String roomName){ this.roomName = roomName; }
    public void setTeacherName(String teacherName){ this.teacherName = teacherName; }
    public void setAtt(int att){ this.att = att; }
    public void setLate(int late){ this.late = late; }
    public void setAbs(int abs){ this.abs = abs; }
    public void setThemeColor(ThemeColor themeColor) { this.themeColor = themeColor; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(className);
        dest.writeInt(start);
        dest.writeInt(week.ordinal());
        dest.writeInt(section);
        dest.writeString(roomName);
        dest.writeString(teacherName);
        dest.writeInt(att);
        dest.writeInt(late);
        dest.writeInt(abs);
        dest.writeInt(themeColor.getThemeId());
    }

    public static final Creator<ClassObject> CREATOR = new Creator<ClassObject>() {
        @Override
        public ClassObject createFromParcel(Parcel in) {
            return new ClassObject(in);
        }

        @Override
        public ClassObject[] newArray(int size) {
            return new ClassObject[size];
        }
    };
}
