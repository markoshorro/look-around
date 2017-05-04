package gal.udc.evilcorp.lookaround;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.List;

import gal.udc.evilcorp.lookaround.model.Event;
import gal.udc.evilcorp.lookaround.settings.SettingsActivity;
import gal.udc.evilcorp.lookaround.tabs.EventFragment;
import gal.udc.evilcorp.lookaround.tabs.MapFragment;
import gal.udc.evilcorp.lookaround.tabs.VuforiaFragment;
import gal.udc.evilcorp.lookaround.util.FirstLaunch;
import gal.udc.evilcorp.lookaround.util.GeolocationService;
import gal.udc.evilcorp.lookaround.util.PreferencesManager;
import gal.udc.evilcorp.lookaround.util.Utils;
import gal.udc.evilcorp.lookaround.view.AboutDialog;

/*
* Main class
* */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {
    // debug purpose
    private static final String TAG = "MainActivity";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static Activity mActivity;

    /**
     * Fragment manager
     */
    private FragmentManager fragManager;

    /**
     * About dialog. It is handled with fragManager
     */
    private AboutDialog aboutDialog;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Best strategy ever to manage the fragments. Thanks stackoverflow: you're the real MVP
        fragManager = getSupportFragmentManager();
        aboutDialog = new AboutDialog();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        // Again, SO saving our lives... how much do we owe you???
        // https://developer.android.com/reference/android/support/v4/view/ViewPager.html#setOffscreenPageLimit%28int%29
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // First time launch!
        boolean isFirstTime = PreferencesManager.isFirst(MainActivity.this);
        if (isFirstTime) {
            startActivity(new Intent(MainActivity.this, FirstLaunch.class));
        }

        // start service
        Intent intent = new Intent(this, GeolocationService.class);
        startService(intent);

        mActivity = this;

        // listener
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                /*
                String s = intent.getStringExtra(GeolocationService.GEO_MESSAGE);
                String[] tokens = s.split(Utils.MSG_DELIMITER);
                Log.e(TAG, tokens.toString());
                switch(tokens[0]) {
                    case Utils.MSG_LOC: break;
                    case Utils.MSG_MAP:
                        MapFragment.update(Float.valueOf(tokens[1]),
                                Float.valueOf(tokens[2])); break;
                    case Utils.MSG_EVT:
                        EventFragment.update(Arrays.copyOfRange(tokens, 1, tokens.length));
                        break;
                    case Utils.MSG_ERR:
                    case Utils.MSG_NA:
                        Utils.buildAlertMessageNoGps(mActivity);
                        break;
                    default: break;
                }
                */
                final int messageType = intent.getIntExtra(Utils.EVENT_TYPE, Utils.NO_EVENT);
                final Parcelable messageContent = intent.getParcelableExtra(Utils.EVENT_CONTENT);
                switch(messageType) {
                    case Utils.NO_EVENT:
                        // Do nothing or log
                        break;
                    case Utils.NEW_EVENT:
                        final List<Event> events = Parcels.unwrap(messageContent);
                        final String[] tokens = new String[events.size()];
                        for (int i = 0; i < events.size(); i++)
                        {
                            tokens[i] = events.get(i).toString();
                        }
                       // EventFragment.update(tokens);
                        EventFragment.showList(events);
                        MapFragment.update(events);
                        break;
                }
            }
        };
    }

    /**
     * Lifecycle methods
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }
    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(GeolocationService.GEO_RESULT)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }


    /**
     * TABS
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                aboutDialog.show(fragManager, "aboutDialog");
                return true;
            case R.id.action_settings:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(_openSettings());
                    }
                }).start();
                return true;
            default: break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Private method to run settings
     */
    private Runnable _openSettings() {
        try {
            final Intent intent = new Intent(this, SettingsActivity.class);
            return new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            };
        } catch (final Exception e) {
            e.printStackTrace();
            return new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(),
                            R.string.err_msg, Toast.LENGTH_LONG).show();
                }
            };
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return MapFragment.getInstance();
                case 1:
                    return EventFragment.getInstance();
                case 2:
                    return VuforiaFragment.getInstance();
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.section_one);
                case 1:
                    return getString(R.string.section_two);
                case 2:
                    return getString(R.string.section_three);
            }
            return null;
        }
    }
}