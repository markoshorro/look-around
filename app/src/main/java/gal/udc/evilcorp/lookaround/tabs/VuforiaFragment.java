package gal.udc.evilcorp.lookaround.tabs;

/**
 * Created by marcos on 19/04/17.
 */

import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.unity3d.player.UnityPlayer;

import gal.udc.evilcorp.lookaround.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class VuforiaFragment extends Fragment {

    private static final Fragment Instance = new VuforiaFragment();
    private final static String TAG = "VuforiaFragment";

    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    private FrameLayout fl_forUnity;

    /**
     * Singleton pattern
     * @return
     */
    public static Fragment getInstance() {
        return Instance;
    }

    public VuforiaFragment() {}

    /**
     * Lifecycle methods
     */

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

        mUnityPlayer = new UnityPlayer(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_ar, container, false);
        this.fl_forUnity = (FrameLayout) rootView.findViewById(R.id.fl_forUnity);
        //Add the mUnityPlayer view to the FrameLayout, and set it to fill all the area available
        this.fl_forUnity.addView(mUnityPlayer.getView(),
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mUnityPlayer.requestFocus();
        Log.e(TAG, "windowFocusChangedTrue");

        return rootView;
    }

    @Override
    public final void onResume() {
        super.onResume();
        mUnityPlayer.resume();
        Log.e(TAG, "onResume");
    }

    @Override
    public final void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    @Override
    public void onDestroy() {
        mUnityPlayer.quit();
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "setUserVisibleHint");
        mUnityPlayer.configurationChanged(newConfig);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mUnityPlayer==null)
            return;

        if (isVisibleToUser) {
            mUnityPlayer.windowFocusChanged(true);
            mUnityPlayer.resume();
        }
        else {
            //mUnityPlayer.windowFocusChanged(false);
            mUnityPlayer.pause();
        }
    }
}