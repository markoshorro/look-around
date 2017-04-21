package gal.udc.evilcorp.lookaround.tabs;

/**
 * Created by marcos on 19/04/17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.unity3d.player.UnityPlayer;

import gal.udc.evilcorp.lookaround.MainActivity;
import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.UnityPlayerActivity;
import gal.udc.evilcorp.lookaround.util.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class VuforiaFragment extends Fragment {
    private final static String TAG = "VuforiaFragment";

    private boolean setWindows = false;

    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    FrameLayout fl_forUnity;

    /**
     * Lifecycle methods
     */

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

        if (!setWindows) {
            mUnityPlayer.windowFocusChanged(true);//First fix Line
            setWindows = true;
        }

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
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
    }

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static Context mContext = null;

    public VuforiaFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static VuforiaFragment newInstance(int sectionNumber, Context mContext) {
        VuforiaFragment fragment = new VuforiaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        VuforiaFragment.mContext = mContext;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "setUserVisibleHint");
        mUnityPlayer.configurationChanged(newConfig);
    }
}


//    @Override
//    public final void setUserVisibleHint(final boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (mUnityPlayer==null) {
//            return;
//        }
//        Log.e(TAG, "setUserVisibleHint");
//
//        if (isVisibleToUser) {
//            Log.e(TAG, "setUserVisibleHintVisible");
//
//            mUnityPlayer.windowFocusChanged(true);//First fix Line
//        } else {
//            Log.e(TAG, "setUserVisibleHintInVisible");
//
//            mUnityPlayer.windowFocusChanged(false);//First fix Line
//        }
//    }