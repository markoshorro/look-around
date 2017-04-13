package gal.udc.evilcorp.lookaround;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;

import gal.udc.evilcorp.lookaround.util.FirstLaunch;
import gal.udc.evilcorp.lookaround.util.GeolocationService;
import gal.udc.evilcorp.lookaround.util.PreferencesManager;
import gal.udc.evilcorp.lookaround.vuforia.UnityPlayerActivity;

/*
* Main class
* */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends UnityPlayerActivity {
    private static final String TAG = "MainActivity";

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread t = new Thread(new Runnable() {
            public void run() {
                boolean isFirstTime = PreferencesManager.isFirst(MainActivity.this);

                if (isFirstTime) {
                    // launch firstlaunchactivity
                    startActivity(new Intent(MainActivity.this, FirstLaunch.class));
                }

                //TODO
                // NEED TO REDO THIS CODE
                // THIS CODE IS JUST AN EXAMPLE
                ViewGroup rootView = (ViewGroup)MainActivity.this.findViewById
                        (android.R.id.content);

                // find the first leaf view (i.e. a view without children)
                // the leaf view represents the topmost view in the view stack
                View topMostView = getLeafView(rootView);

                // let's add a sibling to the leaf view
                ViewGroup leafParent = (ViewGroup)topMostView.getParent();
                Button sampleButton = new Button(MainActivity.this);
                sampleButton.setText("Press Me");
                leafParent.addView(sampleButton, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));

            }
        });

        t.start();

        /*
         * Creates a new Intent to start the GeolocationService
         * IntentService.
         */
        Intent mServiceIntent = new Intent(this, GeolocationService.class);
        startService(mServiceIntent);

        final ViewGroup rootView = (ViewGroup)MainActivity.this.findViewById
                (android.R.id.content);

        final TextView textView = new TextView(this);


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(GeolocationService.GEO_MESSAGE);
                Log.e(TAG, "received: " + s);
                String[] addr = s.split(":::::");
                if (addr.length>=2) {
                    setTitle(addr[0] + ", " + addr[1]);
                    textView.setText(addr[0] + ", " + addr[1]);
                } else {
                    setTitle(addr[0]);
                    textView.setText(addr[0]);
                }
                rootView.addView(textView);
            }
        };
    }

    /*
        This is a temporal method...
     */
    private View getLeafView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup)view;
            for (int i = 0; i < vg.getChildCount(); ++i) {
                View chview = vg.getChildAt(i);
                View result = getLeafView(chview);
                if (result != null)
                    return result;
            }
            return null;
        }
        else {
            Log.d(TAG, "Found leaf view");
            return view;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(MainActivity.this, "Sorry! You can't use this app without granting permission!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
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
}