package masegi.sho.mytimetable.domain.value;

import java.util.ArrayList;

import masegi.sho.mytimetable.R;

/**
 * Created by masegi on 2017/09/03.
 */

public enum ThemeColor {

    DEFAULT(0, R.style.AppTheme, R.color.default_color),
    RED(1, R.style.AppTheme_Red, R.color.colorPrimary_red),
    PURPLE(2, R.style.AppTheme_Purple, R.color.colorPrimary_purple),
    BLUE(3, R.style.AppTheme_Blue, R.color.colorPrimary_blue),
    LIGHTBLUE(4, R.style.AppTheme_LightBlue, R.color.colorPrimary_light_blue),
    TEAL(5, R.style.AppTheme_Teal, R.color.colorPrimary_teal),
    GREEN(6, R.style.AppTheme_Green, R.color.colorPrimary_green),
    YELLOW(7, R.style.AppTheme_Yellow, R.color.colorPrimary_yellow),
    ORANGE(8, R.style.AppTheme_Orange, R.color.colorPrimary_orange),
    BROWN(9, R.style.AppTheme_Brown, R.color.colorPrimary_brown);

    public enum Type{
        STRING,COLOR;
    }

    int themeId;
    int themeResId;
    int primaryColorResId;

    private static ThemeColor[] themeColors = ThemeColor.values();

    private ThemeColor(int themeId, int themeResId, int primaryColorResId){

        this.themeId = themeId;
        this.themeResId = themeResId;
        this.primaryColorResId = primaryColorResId;
    }

    public int getThemeResId(){ return this.themeResId; }
    public int getThemeId() { return this.themeId; }
    public int getPrimaryColorResId(){ return this.primaryColorResId; }

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
