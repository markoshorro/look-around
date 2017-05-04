package gal.udc.evilcorp.lookaround.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.model.Event;
import gal.udc.evilcorp.lookaround.view.EventsAdapter;

/**
 * Created by marcos on 20/04/17.
 */

public class EventFragment extends Fragment {
    private static final Fragment Instance = new EventFragment();

    private static FragmentActivity event_fragment;
    private static ListView eventLeadsList;
    private static ArrayAdapter<String> eventLeadsAdapter;

    /**
     * Singleton pattern
     * @return
     */
    public static Fragment getInstance() {
        return Instance;
    }

    public EventFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        eventLeadsList = (ListView) rootView.findViewById(R.id.EventListView);
        event_fragment = getActivity();

        return rootView;
    }

    public static void update(String[] items) {

        if (eventLeadsList==null) {
            return;
        }
        eventLeadsAdapter = new ArrayAdapter<>(
                event_fragment,
                android.R.layout.simple_list_item_1,
                items);

        eventLeadsList.setAdapter(eventLeadsAdapter);
    }


    public static void showList(final List<Event> events){
        List<HashMap<String, String>> datos = new ArrayList<HashMap<String, String>>();
        for (Event event: events) {

            HashMap<String, String> dato = new HashMap<String, String>();
            dato.put(EventsAdapter.NAME, event.getName());
            dato.put(EventsAdapter.LOCAL, event.getDescription());
            datos.add(dato);
        }

        EventsAdapter adapter = new EventsAdapter(event_fragment, datos);
        eventLeadsList.setAdapter(adapter);
    }
}
