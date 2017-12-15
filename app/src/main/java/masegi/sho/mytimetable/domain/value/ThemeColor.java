package masegi.sho.mytimetable.domain.value;

import java.util.ArrayList;

import masegi.sho.mytimetable.R;

/**
 * Created by masegi on 2017/09/03.
 */

public enum ThemeColor {

    DEFAULT(0, R.style.AppTheme, R.color.default_color, R.color.grey600),
    RED(1, R.style.AppTheme_Red, R.color.colorPrimary_red, R.color.colorPrimaryDark_red),
    PURPLE(2, R.style.AppTheme_Purple, R.color.colorPrimary_purple, R.color.colorPrimaryDark_purple),
    BLUE(3, R.style.AppTheme_Blue, R.color.colorPrimary_blue, R.color.colorPrimaryDark_blue),
    LIGHTBLUE(4, R.style.AppTheme_LightBlue, R.color.colorPrimary_light_blue, R.color.colorPrimaryDark_light_blue),
    TEAL(5, R.style.AppTheme_Teal, R.color.colorPrimary_teal, R.color.colorPrimaryDark_teal),
    GREEN(6, R.style.AppTheme_Green, R.color.colorPrimary_green, R.color.colorPrimaryDark_green),
    YELLOW(7, R.style.AppTheme_Yellow, R.color.colorPrimary_yellow, R.color.colorPrimaryDark_yellow),
    ORANGE(8, R.style.AppTheme_Orange, R.color.colorPrimary_orange, R.color.colorPrimaryDark_orange),
    BROWN(9, R.style.AppTheme_Brown, R.color.colorPrimary_brown, R.color.colorPrimaryDark_brown);

    int themeId;
    int themeResId;
    int primaryColorResId;
    int primaryColorDarkResId;

    private static ThemeColor[] themeColors = ThemeColor.values();

    private ThemeColor(int themeId, int themeResId, int primaryColorResId, int primaryColorDarkResId) {

        this.themeId = themeId;
        this.themeResId = themeResId;
        this.primaryColorResId = primaryColorResId;
        this.primaryColorDarkResId = primaryColorDarkResId;
    }

    public int getThemeResId() { return this.themeResId; }
    public int getThemeId() { return this.themeId; }
    public int getPrimaryColorResId() { return this.primaryColorResId; }
    public int getPrimaryColorDarkResId() { return this.primaryColorDarkResId; }

    public static ThemeColor getThemeColor(int themeId) {

        for (ThemeColor themeColor : themeColors) {

            if (themeColor.getThemeId() == themeId) return themeColor;
        }
        return DEFAULT;
    }

    public static ThemeColor getThemeColorByColorResId(int colorResId) {

        for (ThemeColor themeColor : themeColors) {

            if (themeColor.getPrimaryColorResId() == colorResId) return themeColor;
        }
        return DEFAULT;
    }

    public static ArrayList<Integer> getColorResIdArray(){

        ArrayList<Integer> colorResArray = new ArrayList<>();
        for(ThemeColor themeColor : themeColors){

            colorResArray.add(themeColor.getPrimaryColorResId());
        }
        return colorResArray;
    }

}
