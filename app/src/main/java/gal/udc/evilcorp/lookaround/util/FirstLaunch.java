package gal.udc.evilcorp.lookaround.util;

import android.Manifest;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

import gal.udc.evilcorp.lookaround.R;

/**
 * Created by marcos on 11/04/17.
 */

public class FirstLaunch extends AppIntro {
    private static final String TAG = "FirstLaunch";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.first_launch_title),
                getString(R.string.first_launch_desc),
                R.drawable.common_signin_btn_icon_light,
                Color.parseColor("#2f9bff")));

        //TODO
        // CHECK THIS
        if (!Utils.checkPermission(this)) {
            PermissionEverywhere.getPermission(getApplicationContext(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1,
                    getString(R.string.permission_request),
                    getString(R.string.permission_request_info),
                    R.mipmap.ic_launcher)
                    .enqueue(new PermissionResultCallback() {
                        @Override
                        public void onComplete(PermissionResponse permissionResponse) {
                            // This check is needed but unnecessary
                            if (Utils.checkPermission(getApplicationContext())) {
                                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
        }

        // Override bar/separator color.
        setBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        setSeparatorColor(ContextCompat.getColor(this, R.color.colorSecondary));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // This method is not used...
    }
}
