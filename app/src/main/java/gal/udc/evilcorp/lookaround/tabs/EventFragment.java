package gal.udc.evilcorp.lookaround.tabs;

import android.content.Context;
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

import java.util.List;

import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.model.Event;
import gal.udc.evilcorp.lookaround.view.EventsAdapter;
import gal.udc.evilcorp.lookaround.view.ListItemActivity;

/**
 * Created by marcos on 20/04/17.
 */

public class EventFragment extends Fragment {
    private static final Fragment Instance = new EventFragment();
    private static final String TAG = "EVENTFRAGMENT";

    private static FragmentActivity event_fragment;
    private static ListView eventLeadsList;

    private Context appContext;

    /**
     * Singleton pattern
     * @return
     */
    public static Fragment getInstance() {
        return Instance;
    }

    private Context context;
    public void initialize(Context context) {
        this.context = context;
    }


    public EventFragment() {}


    public static EventFragment newInstance() {
        
        Bundle args = new Bundle();
        
        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private List<Event> events = null;

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

        EventsAdapter adapter = new EventsAdapter(event_fragment, events);
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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

}

