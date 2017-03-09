package gal.udc.evilcorp.lookaround.geolocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import gal.udc.evilcorp.lookaround.MainActivity;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by eloy on 09/03/2017.
 */

public class Geolocation implements LocationListener {

    private MainActivity context;
    private LocationManager locationManager;
    private Location location;
    boolean isGPSEnabled, isNetworkEnabled;


    public Geolocation(MainActivity _context) {
        context = _context;
    }


    // Called when the location has changed.
    @Override
    public void onLocationChanged(Location location) {
        double lat = (location.getLatitude());
        double lng = (location.getLongitude());
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            // Update the title of the MainActivity to set the location
            context.setTitle(Html.fromHtml("<small>"+address + ", " + city+"</small>"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }


    // Called when the provider is enabled by the user.
    @Override
    public void onProviderEnabled(String s) {
        context.setTitle("Enabled provider");
    }


    // Called when the provider is disabled by the user.
    @Override
    public void onProviderDisabled(String s) {
        context.setTitle("Disabled provider");
    }


    // Called to get the actual location
    public void getLocation() {
        try {

            checkProviders();

            if (!isGPSEnabled && !isNetworkEnabled) {
                context.setTitle("Providers not available");
            } else {
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        context.setTitle("Permission denied");
                    }
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }

            }
            if (location != null) {
                onLocationChanged(location);
            } else {
                context.setTitle("Location not available");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateLocation() {
        try {

            checkProviders();

            if (!isGPSEnabled && !isNetworkEnabled) {
                context.setTitle("Providers not available");
            } else {
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        context.setTitle("Permission denied");
                    }
                    Log.d("Network", "Network Enabled");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60, 1, this);
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60, 1, this);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void removeLocations() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            context.setTitle("Permission denied");
        }
        locationManager.removeUpdates(this);
    }


    // This method check if any providers is enabled
    private void checkProviders()
    {
        locationManager = (LocationManager)
                context.getSystemService(LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
