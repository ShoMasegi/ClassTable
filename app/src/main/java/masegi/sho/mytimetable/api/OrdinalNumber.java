package masegi.sho.mytimetable.api;

/**
 * Created by masegi on 2017/10/20.
 */

public class OrdinalNumber {

    public static String ordinalNumberString(int num) {

        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (num % 100) {
            case 11:
            case 12:
            case 13:
                return num + "th";
            default:
                return num + sufixes[num % 10];

        }
    }
}
