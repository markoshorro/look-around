package gal.udc.evilcorp.lookaround.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by markoshorro on 4/13/17.
 */

public class Utils {
    // debugging purposes
    private static final String TAG = "Utils";

    // for messages
    public static final String MSG_EVT = "evtMsg";
    public static final String MSG_MAP = "mapMsg";
    public static final String MSG_LOC = "locMsg";
    public static final String MSG_ERR = "errorMsg";
    public static final String MSG_NA = "notAvailableMsg";
    public static final String MSG_DELIMITER = "::::";

    // map
    public static int ZOOM_CAMERA = 17;

    /**
     * Check whether the app has permissions or not
     * @param context
     * @return boolean
     */
    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
