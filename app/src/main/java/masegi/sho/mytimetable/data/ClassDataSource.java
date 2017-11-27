package masegi.sho.mytimetable.data;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.DayOfWeek;

/**
 * Created by masegi on 2017/09/23.
 */

public class ClassDataSource {

    private Map<DayOfWeek, ArrayList<ClassObject>> dataSource;

    public ClassDataSource(EnumMap<DayOfWeek, ArrayList<ClassObject>> dataSource) {

        this.dataSource = dataSource;
    }

    public ClassDataSource(ClassObject[][] items) {

        dataSource = new EnumMap<DayOfWeek, ArrayList<ClassObject>>(DayOfWeek.class);
        DayOfWeek day;

        for (int i = 0; i < items.length; i++) {

            day = DayOfWeek.getWeekByOrdinal(i);
            ArrayList<ClassObject> classData = new ArrayList<>();

            for (int j = 0; j < items[i].length; j++) {

                if (items[i][j] != null) classData.add(items[i][j]);
            }

            dataSource.put(day, classData);
        }
    }

    public ArrayList<ClassObject> getDayClasses(DayOfWeek dayOfWeek) {

        return dataSource.get(dayOfWeek);
    }

    public ClassObject getClassObject(DayOfWeek dayOfWeek, int start) {

        for (ClassObject item : dataSource.get(dayOfWeek)) {

            if (item.getStart() == start) {

                return item;
            }
        }

        return null;
    }
}

