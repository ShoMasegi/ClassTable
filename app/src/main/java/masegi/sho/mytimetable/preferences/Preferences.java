package masegi.sho.mytimetable.preferences;

import android.view.ContextThemeWrapper;

import masegi.sho.mytimetable.domain.value.ThemeColor;

/**
 * Created by masegi on 2017/09/03.
 */

public class Preferences {

    public static void applyTheme(ContextThemeWrapper contextThemeWrapper,
                                  ThemeColor themeColor){

        contextThemeWrapper.setTheme(themeColor.getThemeResId());
    }

}
