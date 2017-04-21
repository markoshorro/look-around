package gal.udc.evilcorp.lookaround.tabs;

/**
 * Created by marcos on 19/04/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gal.udc.evilcorp.lookaround.MainActivity;
import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.UnityPlayerActivity;
import gal.udc.evilcorp.lookaround.util.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class VuforiaFragment extends Fragment {
    private boolean mResumed;

    @Override
    public final void setUserVisibleHint(final boolean isVisibleToUser) {
        final boolean needUpdate = mResumed && isVisibleToUser != getUserVisibleHint();
        super.setUserVisibleHint(isVisibleToUser);
        if (needUpdate) {
            if (isVisibleToUser) {
                this.onVisible();
            } else {
                this.onInvisible();
            }
        }
    }

    @Override
    public final void onResume() {
        super.onResume();
        mResumed = true;
        if (this.getUserVisibleHint()) {
            this.onVisible();
        }
    }

    @Override
    public final void onPause() {
        super.onPause();
        mResumed = false;
        this.onInvisible();
    }

    /**
     * Returns true if the fragment is in resumed state and userVisibleHint was set to true
     *
     * @return true if resumed and visible
     */
    protected final boolean isResumedAndVisible() {
        return mResumed && getUserVisibleHint();
    }

    /**
     * Called when onResume was called and userVisibleHint is set to true or vice-versa
     */
    protected void onVisible() {
        getActivity().startActivityForResult(
                new Intent(getActivity(), UnityPlayerActivity.class),
                Utils.LAUNCH_AR);
    }

    /**
     * Called when onStop was called or userVisibleHint is set to false
     */
    protected void onInvisible() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Utils.LAUNCH_AR) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                getFragmentManager().popBackStackImmediate();
            }
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}