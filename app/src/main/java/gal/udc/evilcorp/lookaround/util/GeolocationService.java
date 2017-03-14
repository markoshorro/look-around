package gal.udc.evilcorp.lookaround.util;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import gal.udc.evilcorp.lookaround.MainActivity;
import gal.udc.evilcorp.lookaround.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by eloy on 09/03/2017.
 * Minor changes by marcos (last change 14/03/2017).
 */

public class GeolocationService extends Service {

    // for debugging
    private static final String TAG = "GeoLocationService";

    private LocationManager locationManager;
    private LocalBroadcastManager broadcaster = null;
    private Location location;
    boolean isGPSEnabled, isNetworkEnabled;

    private LocationListener locationListener;

    // for the messages
    static final public String GEO_RESULT = "gal.udc.evilcorp.lookaround.util.REQUEST_PROCESSED";
    static final public String GEO_MESSAGE = "gal.udc.evilcorp.lookaround.util.COPA_MSG";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor
     */
    public GeolocationService() {
        super();
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GeolocationService(String name) {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addListenerLocation();
        updateLocation();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Send a broadcast message
     *
     * @param message Message to send
     */
    public void sendResult(String message) {
        broadcaster = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent(GEO_RESULT);
        if (message != null)
            intent.putExtra(GEO_MESSAGE, message);
        broadcaster.sendBroadcast(intent);
    }

    private void addListenerLocation() {
        Log.e(TAG, "****************************************addlistener");

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            /**
             * Called when the location has changed
             *
             * @param location New location
             */
            @Override
            public void onLocationChanged(Location location) {
                double lat = (location.getLatitude());
                double lng = (location.getLongitude());
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lat, lng, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    // Update the title of the MainActivity to set the location
                    Log.d(TAG, "sendingResult...");
                    sendResult(address + ":::::" + city);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            /**
             * Called when location service status has changed
             *
             * @param s
             * @param i
             * @param bundle
             */
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.e(TAG, "****************************************STATUSCHANGE");

                updateLocation();
            }

            /**
             * Called when provided is enabled by the user
             *
             * @param s Name of the new provider
             */
            @Override
            public void onProviderEnabled(String s) {
                //Toast.makeText(getApplicationContext(), "Enabled provider", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "****************************************ONPROVIDERENABLED");
                updateLocation();
            }

            /**
             * Called when the provider is disabled by the user.
             *
             * @param s Message
             */
            @Override
            public void onProviderDisabled(String s) {
                //Toast.makeText(getApplicationContext(), "Disabled provider", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "****************************************ONPROVIDERDISABLED");
                updateLocation();
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    /**
     * Check whether the app has permissions or not
     * @param context
     * @return boolean
     */
    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Clean the location manager
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        removeLocations();
    }

    /**
     * Called when location has changed
     */
    public void updateLocation() {
        try {
            checkProviders();
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(this, getString(R.string.providers_not_available), Toast.LENGTH_SHORT).show();
                sendResult(getString(R.string.location_not_available));
            } else {
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("Network", "Network Enabled");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60, 1, locationListener);
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60, 1, locationListener);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Remove the locations stored
     */
    public void removeLocations() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
        locationManager.removeUpdates(locationListener);
    }


    /**
     * This method check if any providers is enabled
     */
    private void checkProviders()
    {
        locationManager = (LocationManager)
                this.getSystemService(LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
