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

    // codes for messages
    public static final int MSG_NO_EVENT  = 0x0000;
    public static final int MSG_NEW_EVENT = 0x0001;
    public static final int MSG_MAP       = 0x0002;
    public static final int MSG_LOC       = 0x0003;
    public static final int MSG_PLACES    = 0x0004;

    public static final int MSG_DELIMITER = 0x0040;

    public static final int MSG_ERR       = 0x0080;
    public static final int MSG_NA        = 0x0081;
    public static final int MSG_INFO      = 0x0082;

    // windows close
    public static boolean closed = true;

    // map
    public static int ZOOM_CAMERA = 17;

    // events
    public static final String ACCESS_TOKEN_FB =
            "EAAKDLq6ADLEBALZAyzDAweFgwFjRt3t6puo0wYT9RGietaH6v53XcNs7ENQ47kBu7YveZAcZBGqAlHZB7SNafY83L32tjkiBvnZCNTO6MVhAW7tRrt1Io9dZARtz5xcj5LEkxwDaCJZBUgMntzcS4oUoMEVxFjjfKsZD";
    public static final String AUTH_CODE =
            "OAuth EAAKDLq6ADLEBALZAyzDAweFgwFjRt3t6puo0wYT9RGietaH6v53XcNs7ENQ47kBu7YveZAcZBGqAlHZB7SNafY83L32tjkiBvnZCNTO6MVhAW7tRrt1Io9dZARtz5xcj5LEkxwDaCJZBUgMntzcS4oUoMEVxFjjfKsZD";
    public static final String URL_FB =
            "https://graph.facebook.com/";

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
                        Utils.closed = true;
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
