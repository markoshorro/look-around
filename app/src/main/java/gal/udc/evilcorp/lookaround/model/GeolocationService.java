package gal.udc.evilcorp.lookaround.model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.model.Event;
import gal.udc.evilcorp.lookaround.model.Place;
import gal.udc.evilcorp.lookaround.util.Utils;

/**
 * Created by eloy on 09/03/2017.
 * Minor changes by marcos (last change 20/04/2017).
 */

public class GeolocationService extends Service {

    // for debugging
    private static final String TAG = "GeoLocationService";

    private static int SYNC_DIST;
    private static int SYNC_FREQ;

    private LocationManager locationManager;
    private LocalBroadcastManager broadcaster = null;
    private Location actualLocation;
    boolean isGPSEnabled, isNetworkEnabled;

    private LocationListener locationListener;

    ArrayList<Place> places = new ArrayList<>();
    private Set<Event> events = new HashSet<>();

    // for the messages
    static final public String GEO_RESULT = "gal.udc.evilcorp.lookaround.util.REQUEST_PROCESSED";
    static final public String GEO_MESSAGE = "gal.udc.evilcorp.lookaround.util.COPA_MSG";

    private SharedPreferences mPrefs;
    private SharedPreferences.OnSharedPreferenceChangeListener
            mPreferenceListener;

    // More efficient requests
    private RequestQueue requestQueue;
    private HurlStack hurlStack;

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
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mPreferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.e(TAG, key + ": " + sharedPreferences.getString(key, "60000"));
                if (key.equals("sync_frequency")) {
                    SYNC_FREQ = Integer.parseInt(sharedPreferences.getString(key, "60000"));
                }
                if (key.equals("sync_dist_frequency")) {
                    SYNC_DIST = Integer.parseInt(sharedPreferences.getString(key, "100"));
                }

                if ((locationManager!=null)&&(Utils.checkPermission(getApplicationContext()))) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, SYNC_FREQ,
                            SYNC_DIST, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, SYNC_FREQ,
                            SYNC_DIST, locationListener);
                }

            }
        };
        mPrefs.registerOnSharedPreferenceChangeListener(mPreferenceListener);
        addListenerLocation();
        updateLocation();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.


        hurlStack = new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpsURLConnection httpsURLConnection =
                        (HttpsURLConnection) super.createConnection(url);
                try {
                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return httpsURLConnection;
            }
        };
        requestQueue = Volley.newRequestQueue(this, hurlStack);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * Type of message sent (declared in Utils)
     * Message sent
     * @param msgType
     * @param msg
     */
    private void sendResult(final int msgType, final Parcelable msg) {
        broadcaster = LocalBroadcastManager.getInstance(this);

        Intent intent = new Intent(GEO_RESULT);
        intent.putExtra(Utils.EVENT_TYPE, msgType);
        intent.putExtra(Utils.EVENT_CONTENT, msg);

        broadcaster.sendBroadcast(intent);
    }

    /**
     * Adds listener
     */
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
                Log.e(TAG, "onLocationChanged!");
                sendResult(Utils.MSG_INFO, Parcels.wrap("start_spinner"));
                actualLocation = location;
                double lat = (location.getLatitude());
                double lng = (location.getLongitude());
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                // Get places by new location
                getPlacesByLocation();

                try {
                    // address
                    addresses = geocoder.getFromLocation(lat, lng, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    sendResult(Utils.MSG_LOC, Parcels.wrap(
                            new ArrayList<>(Arrays.asList(address, city))));

                    // coordinates
                    sendResult(Utils.MSG_MAP, Parcels.wrap(
                            new ArrayList<>(Arrays.asList(lat, lng))));
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
                Log.e(TAG, "****************************************ONPROVIDERDISABLED");
                updateLocation();
            }
        };

        if (Utils.checkPermission(this)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, SYNC_FREQ,
                    SYNC_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, SYNC_FREQ,
                    SYNC_DIST, locationListener);
        }

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
                sendResult(Utils.MSG_NA, Parcels.wrap(getString(R.string.location_not_available)));
            } else {
                if (isNetworkEnabled) {
                    if (!Utils.checkPermission(this)) {
                        Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("Network", "Network Enabled");
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            SYNC_FREQ, SYNC_DIST, locationListener);
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (actualLocation == null) {
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                SYNC_FREQ, SYNC_DIST, locationListener);
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
        if (Utils.checkPermission(this)) {
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

    /**
     * This method creates the request for facebook events
     */
    private void getPlacesByLocation()
    {
        if(actualLocation == null)
        {
            sendResult(Utils.MSG_NA, Parcels.wrap(getString(R.string.location_not_available)));
        }
        else
        {
            // actual location request
            /*requestPlaces("search?type=place&center=" + actualLocation.getLatitude() + ","
                    + actualLocation.getLongitude() + "&fields=name,location&distance=100&" +
                    "limit=5&access_token=" + Utils.ACCESS_TOKEN_FB);*/

            // specific location to test
            requestPlaces("search?type=place&center=43.368065,-8.400727" +
                    "&fields=name,location&distance=100&limit=5&access_token=" + Utils.ACCESS_TOKEN_FB);
        }
    }

    /**
     * This method makes the request for facebook events
     */
    private void requestPlaces(String query)
    {
        String url = Utils.URL_FB + query;

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parsePlaces(response);
                        Log.e(TAG, "Get places response: " + response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendResult(Utils.MSG_ERR, Parcels.wrap("Error to get places from Facebook"));
                    }
        });

        // Start the RequestQueue
        if (requestQueue==null) {
            requestQueue = Volley.newRequestQueue(this, hurlStack);
        }
        // Add the request to the RequestQueue.
        requestQueue.add(jsonRequest);
    }

    /**
     * This method parses the response from facebook
     */
    private void parsePlaces(JSONObject json)
    {
        JSONArray jsonList = null;
        try {
            jsonList = json.getJSONArray("data");
            for(int i=0;i<jsonList.length();i++)
            {
                JSONObject obj = jsonList.getJSONObject(i);
                Place place = new Place(obj.getString("id"), obj.getString("name"));
                final JSONObject location = obj.getJSONObject("location");
                place.setLat(location.getDouble("latitude"));
                place.setLng(location.getDouble("longitude"));
                places.add(place);
            }
            getEventsByPlaces();
            sendResult(Utils.MSG_PLACES, Parcels.wrap(places));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates multiple requests
     */
    private void getEventsByPlaces()
    {
        if(places!=null && places.size()>0)
        {
            for (int i=0;i<places.size();i++)
            {
                String query = places.get(i).getId()+"/events";
                requestEvents(query);
                places.remove(i);
            }
        }
    }

    /**
     * This method sends multiple requests
     */
    private void requestEvents(String query)
    {
        String url = Utils.URL_FB + query;

        HurlStack hurlStack = new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpsURLConnection httpsURLConnection =
                        (HttpsURLConnection) super.createConnection(url);
                try {
                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return httpsURLConnection;
            }
        };

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseEvents(response);
                        Log.e(TAG, "Get events response: " + response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendResult(Utils.MSG_ERR, Parcels.wrap("Error to get events from Facebook"));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", Utils.AUTH_CODE);
                return params;
            }
        };

        // Start the RequestQueue

        // Add the request to the RequestQueue.
        if (requestQueue==null) {
            requestQueue = Volley.newRequestQueue(this, hurlStack);
        }
        requestQueue.add(jsonRequest);
    }

    /**
     * This method parses multiple requests
     */
    private void parseEvents(JSONObject json)
    {
        try {
            final List<Event> newEvents = new ArrayList<Event>();
            JSONArray jsonList = json.getJSONArray("data");
            for(int i = 0;i < jsonList.length(); i++)
            {
                JSONObject obj = jsonList.getJSONObject(i);
                final Event event = new Event();
                event.setId(obj.getString("id"));
                event.setName(obj.getString("name"));
                event.setDescription(obj.getString("description"));
                event.setPlace(obj.getJSONObject("place").getString("name"));
                final JSONObject location = obj.getJSONObject("place").getJSONObject("location");
                event.setLatitude(location.getDouble("latitude"));
                event.setLongitude(location.getDouble("longitude"));

                newEvents.add(event);
            }
            events.addAll(newEvents);
            if (events.size() > 0) {
                Log.e(TAG,"SEND EVENTS!!!!!!");
                sendResult(Utils.MSG_NEW_EVENT, Parcels.wrap(new ArrayList<>(events)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * This method verfies hostname
     */
    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //return true; // verify always returns true,
                // which could cause insecure network traffic due to
                // trusting TLS/SSL server certificates for wrong hostnames
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("localhost", session);
            }
        };
    }

    /**
     * Certifications
     */
    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkClientTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkClientTrusted", e.toString());
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkServerTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkServerTrusted", e.toString());
                        }
                    }
                }
        };
    }

    private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException,
            NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = getResources().openRawResource(R.raw.mykey);
        // this cert file stored in \app\src\main\res\raw folder path

        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();
    }

}
