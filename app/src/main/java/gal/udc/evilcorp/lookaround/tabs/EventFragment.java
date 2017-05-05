package gal.udc.evilcorp.lookaround.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
    private static final String TAG = "EVENTFRAGMENT";

    private static FragmentActivity event_fragment;
    private static ListView eventLeadsList;

    /**
     * Singleton pattern
     * @return
     */
    public static Fragment getInstance() {
        return Instance;
    }

    public EventFragment() {}

    public static EventFragment newInstance() {
        
        Bundle args = new Bundle();
        
        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        eventLeadsList = (ListView) rootView.findViewById(R.id.EventListView);
        event_fragment = getActivity();

        return rootView;
    }

    @Override
    public final void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    public final void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    public final void onStop() {
        super.onStop();
        getChildFragmentManager()
                .beginTransaction()
                .detach(Instance)
                .commitAllowingStateLoss();


        Log.e(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e(TAG, "onViewCreated");
    }


    public static void showList(final List<Event> events){
        List<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();
        for (Event event: events) {
            HashMap<String, String> e = new HashMap<String, String>();
            e.put(EventsAdapter.NAME, event.getName());
            e.put(EventsAdapter.LOCAL, event.getDescription());
            eventList.add(e);
        }

        EventsAdapter adapter = new EventsAdapter(event_fragment, eventList);
        eventLeadsList.setAdapter(adapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }
}
