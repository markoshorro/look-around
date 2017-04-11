package gal.udc.evilcorp.lookaround.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by marcos on 11/04/17.
 */

public class PreferencesManager {
    private static final String MY_PREFERENCES = "my_preferences_manager";

    public static boolean isFirst(Context context){
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first", true);
        if(first){
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.commit();
        }
        return first;
    }

}
