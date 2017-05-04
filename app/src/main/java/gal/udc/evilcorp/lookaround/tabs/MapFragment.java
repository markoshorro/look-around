package gal.udc.evilcorp.lookaround.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.model.Event;
import gal.udc.evilcorp.lookaround.util.Utils;

/**
 * Created by marcos on 20/04/17.
 */

public class MapFragment extends Fragment {

    private static final Fragment Instance = new MapFragment();

    MapView mMapView;
    public static GoogleMap googleMap;

    // set of elements that can not be duplicated, hashcode needed
    private static Set<Event> loadedEvents;

    /**
     * Singleton pattern
     * @return
     */
    public static Fragment getInstance() {
        return Instance;
    }

    public MapFragment() {
        this.loadedEvents = new HashSet<>();
    }

    public static void update(float lat, float lng) {
        LatLng pos = new LatLng(lat, lng);
        if (googleMap==null) {
            return;
        }
        // For showing a move to my location button
        googleMap.setMyLocationEnabled(true);
        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(pos).zoom(Utils.ZOOM_CAMERA).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public static void update(final List<Event> events)
    {
        for(final Event event: events) {
            if (!loadedEvents.contains(event)) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(event.getLatitude(), event.getLongitude()))
                        .title(event.getDescription()));
                loadedEvents.add(event);
            }
        }
        googleMap.setMyLocationEnabled(true);
        if (!loadedEvents.isEmpty()) {
            final Event firstEvent = loadedEvents.iterator().next();
            final LatLng latLng = new LatLng(firstEvent.getLatitude(), firstEvent.getLongitude());
            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(Utils.ZOOM_CAMERA).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);
            }
        });

        return rootView;
    }
}
