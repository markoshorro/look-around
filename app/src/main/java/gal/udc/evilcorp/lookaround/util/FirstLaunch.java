package gal.udc.evilcorp.lookaround.util;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import gal.udc.evilcorp.lookaround.MainActivity;
import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.settings.PreferencesManager;

/**
 * Created by marcos on 11/04/17.
 */

public class FirstLaunch extends AppIntro {
    private static final String TAG = "FirstLaunch"; // debug

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isFirstTime = PreferencesManager.isFirst(this);
        if (!isFirstTime) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.first_launch_title),
                getString(R.string.first_launch_desc),
                R.mipmap.ic_lookaround,
                ContextCompat.getColor(this, R.color.colorPrimaryLight)));

        addSlide(PermissionAppIntroFragment.newInstance(
                getString(R.string.first_launch_title),
                getString(R.string.first_launch_desc_sec_slide),
                R.mipmap.ic_lookaround,
                ContextCompat.getColor(this, R.color.colorSecondaryLight)));

        addSlide(PermissionAppIntroFragment.newInstance(
                getString(R.string.first_launch_title),
                getString(R.string.first_launch_desc_third_slide),
                R.mipmap.ic_lookaround,
                ContextCompat.getColor(this, R.color.colorPrimaryLight)));

        // asking for permission
        askForPermissions(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);


        // Override bar/separator color.
        setBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        setSeparatorColor(ContextCompat.getColor(this, R.color.colorSecondary));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // This method is not used...
    }

}
