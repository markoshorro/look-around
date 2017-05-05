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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.model.Event;
import gal.udc.evilcorp.lookaround.model.Place;
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
    private static Set<Place> loadedPlaces;

    /**
     * Singleton pattern
     * @return
     */
    public static Fragment getInstance() {
        return Instance;
    }

    public MapFragment() {
        this.loadedEvents = new HashSet<>();
        this.loadedPlaces = new HashSet<>();
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
    }


    public static void update(double lat, double lng) {
        LatLng pos = new LatLng(lat, lng);
        if (googleMap==null) {
            return;
        }
        // For showing a move to my location button
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(pos).zoom(Utils.ZOOM_CAMERA).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public static void update(final List<Event> events) {
        if (googleMap==null) {
            return;
        }

        // For showing a move to my location button
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        for(final Event event: events) {
            if (!loadedEvents.contains(event)) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(event.getLatitude(), event.getLongitude()))
                        .title(event.getDescription()));
                loadedEvents.add(event);
            }
        }
    }

    public static void update(final List<Place> places, boolean val) {
        if (googleMap==null) {
            return;
        }

        // For showing a move to my location button
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        for(final Place place: places) {
            if (!loadedPlaces.contains(place)) {
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(place.getLat(), place.getLng()))
                        .title(place.getName()
                        );
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                googleMap.addMarker(marker);
                loadedPlaces.add(place);
            }
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
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            }
        });

        return rootView;
    }
}
