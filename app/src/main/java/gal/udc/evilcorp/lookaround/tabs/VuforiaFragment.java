package gal.udc.evilcorp.lookaround.tabs;

/**
 * Created by marcos on 19/04/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gal.udc.evilcorp.lookaround.MainActivity;
import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.UnityPlayerActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class VuforiaFragment extends Fragment {

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
        getActivity().startActivity(new Intent(this.getContext(), UnityPlayerActivity.class));

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}