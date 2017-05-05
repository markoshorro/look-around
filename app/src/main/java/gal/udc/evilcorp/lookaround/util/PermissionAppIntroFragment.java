package gal.udc.evilcorp.lookaround.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.paolorotolo.appintro.AppIntroBaseFragment;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.ISlidePolicy;

/**
 * Created by markoshorro on 5/5/17.
 */


public class PermissionAppIntroFragment extends AppIntroBaseFragment implements ISlidePolicy {

        public static PermissionAppIntroFragment newInstance(CharSequence title, CharSequence description,
                                                   @DrawableRes int imageDrawable,
                                                   @ColorInt int bgColor) {
            return newInstance(title, description, imageDrawable, bgColor, 0, 0);
        }

        public static PermissionAppIntroFragment newInstance(CharSequence title, CharSequence description,
                                                   @DrawableRes int imageDrawable, @ColorInt int bgColor,
                                                   @ColorInt int titleColor, @ColorInt int descColor) {
            PermissionAppIntroFragment slide = new PermissionAppIntroFragment();
            Bundle args = new Bundle();
            args.putString(ARG_TITLE, title.toString());
            args.putString(ARG_TITLE_TYPEFACE, null);
            args.putString(ARG_DESC, description.toString());
            args.putString(ARG_DESC_TYPEFACE, null);
            args.putInt(ARG_DRAWABLE, imageDrawable);
            args.putInt(ARG_BG_COLOR, bgColor);
            args.putInt(ARG_TITLE_COLOR, titleColor);
            args.putInt(ARG_DESC_COLOR, descColor);
            slide.setArguments(args);

            return slide;
        }

        public static PermissionAppIntroFragment newInstance(CharSequence title, String titleTypeface,
                                                   CharSequence description, String descTypeface,
                                                   @DrawableRes int imageDrawable,
                                                   @ColorInt int bgColor) {
            return newInstance(title, titleTypeface, description, descTypeface, imageDrawable, bgColor,
                    0, 0);
        }

        public static PermissionAppIntroFragment newInstance(CharSequence title, String titleTypeface,
                                                   CharSequence description, String descTypeface,
                                                   @DrawableRes int imageDrawable, @ColorInt int bgColor,
                                                   @ColorInt int titleColor, @ColorInt int descColor) {
            PermissionAppIntroFragment slide = new PermissionAppIntroFragment();
            Bundle args = new Bundle();
            args.putString(ARG_TITLE, title.toString());
            args.putString(ARG_TITLE_TYPEFACE, titleTypeface);
            args.putString(ARG_DESC, description.toString());
            args.putString(ARG_DESC_TYPEFACE, descTypeface);
            args.putInt(ARG_DRAWABLE, imageDrawable);
            args.putInt(ARG_BG_COLOR, bgColor);
            args.putInt(ARG_TITLE_COLOR, titleColor);
            args.putInt(ARG_DESC_COLOR, descColor);
            slide.setArguments(args);

            return slide;
        }


    @Override
    protected int getLayoutId() {
        return com.github.paolorotolo.appintro.R.layout.fragment_intro;
    }

    @Override
    public boolean isPolicyRespected() {
        return
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        Log.d("AppIntro", "Permission request");
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.CAMERA }, 1);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 1);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);

    }
}
