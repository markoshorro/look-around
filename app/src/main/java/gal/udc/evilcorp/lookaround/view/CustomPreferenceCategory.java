package gal.udc.evilcorp.lookaround.view;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/**
 * Created by markoshorro on 13/12/15.
 * This class is used for the template. It is just for design
 */
public class CustomPreferenceCategory extends PreferenceCategory {
    public CustomPreferenceCategory(Context context) {
        super(context);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);

        titleView.setTextColor(Color.rgb(89, 143, 199));
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.f);
    }
}