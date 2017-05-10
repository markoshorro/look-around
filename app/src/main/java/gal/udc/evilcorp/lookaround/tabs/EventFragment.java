package gal.udc.evilcorp.lookaround.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.model.Event;
import gal.udc.evilcorp.lookaround.view.EventsAdapter;
import gal.udc.evilcorp.lookaround.view.ListItemActivity;

/**
 * Created by marcos on 20/04/17.
 */

public class EventFragment extends Fragment {
    /**
     * Singleton pattern
     */
    private static final Fragment Instance = new EventFragment();
    private static final String TAG = "EVENTFRAGMENT"; //debug

    /**
     *
     */
    private static FragmentActivity event_fragment;
    private static ListView eventLeadsList;

    /**
     * Data to update
     */
    private static List<Event> eventList = new ArrayList<>();

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

        Log.e(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        eventLeadsList = (ListView) rootView.findViewById(R.id.EventListView);
        event_fragment = getActivity();

        EventsAdapter adapter = new EventsAdapter(event_fragment, eventList);
        eventLeadsList.setAdapter(adapter);

        eventLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) parent.getItemAtPosition(position);
                Intent myIntent = new Intent(event_fragment, ListItemActivity.class);
                myIntent.putExtra("event_serializable", event);
                event_fragment.startActivity(myIntent);
            }
        });

        return rootView;
    }

    /**
     * This method rewrites the list and updates the view
     * @param events
     */
    public static void updateList(final List<Event> events){
        ((EventsAdapter)eventLeadsList.getAdapter()).update(events);
    }

}

