package gal.udc.evilcorp.lookaround.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;


/**
 * Created by markoshorro on 4/13/17.
 */

public class Utils {
    // debugging purposes
    private static final String TAG = "Utils";

    public static final String EVENT_TYPE = "EVENT_TYPE";
    public static final String EVENT_CONTENT = "EVENT_CONTENT";

    public static final int NO_EVENT = 0;
    public static final int NEW_EVENT = 1;

    // for messages
    public static final String MSG_EVT = "evtMsg";
    public static final String MSG_MAP = "mapMsg";
    public static final String MSG_LOC = "locMsg";
    public static final String MSG_ERR = "errorMsg";
    public static final String MSG_NA = "notAvailableMsg";
    public static final String MSG_DELIMITER = "::::";

    // to control frequency
    public static final long MIN_TIME = 10*1000;
    public static final float MIN_DIST = 25;


    // map
    public static int ZOOM_CAMERA = 17;

    // events
    public static final String ACCESS_TOKEN_FB = "EAAKDLq6ADLEBALZAyzDAweFgwFjRt3t6puo0wYT9RGietaH6v53XcNs7ENQ47kBu7YveZAcZBGqAlHZB7SNafY83L32tjkiBvnZCNTO6MVhAW7tRrt1Io9dZARtz5xcj5LEkxwDaCJZBUgMntzcS4oUoMEVxFjjfKsZD";
    public static final String AUTH_CODE = "OAuth EAAKDLq6ADLEBALZAyzDAweFgwFjRt3t6puo0wYT9RGietaH6v53XcNs7ENQ47kBu7YveZAcZBGqAlHZB7SNafY83L32tjkiBvnZCNTO6MVhAW7tRrt1Io9dZARtz5xcj5LEkxwDaCJZBUgMntzcS4oUoMEVxFjjfKsZD";
    public static final String URL_FB = "https://graph.facebook.com/";

    // AR
    public static final int LAUNCH_AR = 11;

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

    public static void buildAlertMessageNoGps(final Activity c) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        c.startActivity(new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
