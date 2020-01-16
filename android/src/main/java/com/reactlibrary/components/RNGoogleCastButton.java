package com.reactlibrary.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.mediarouter.app.MediaRouteButton;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;

public class RNGoogleCastButton
        extends SimpleViewManager<MediaRouteButton> {
    public static final String REACT_CLASS = "RnCastButton";

    private Integer mColor = null;
    private static MediaRouteButton googleCastButtonManagerInstance;

    @Override
    public String getName() {
        return REACT_CLASS;
    }


    public static MediaRouteButton getGoogleCastButtonManagerInstance() {
        return googleCastButtonManagerInstance;
    }

    @Override
    public MediaRouteButton createViewInstance(ThemedReactContext context) {
        CastContext castContext = CastContext.getSharedInstance(context);

        final MediaRouteButton button = new ColorableMediaRouteButton(context);
        googleCastButtonManagerInstance = button;

        CastButtonFactory.setUpMediaRouteButton(context, button);

        updateButtonState(button, castContext.getCastState());

        castContext.addCastStateListener(new CastStateListener() {
            @Override
            public void onCastStateChanged(int newState) {
                RNGoogleCastButton.this.updateButtonState(button, newState);
            }
        });

        return button;
    }


    private void updateButtonState(MediaRouteButton button, int state) {
        // hide the button when no device available (default behavior is show it
        // disabled)
        if (CastState.NO_DEVICES_AVAILABLE == state) {
            button.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
        }
    }


    // https://stackoverflow.com/a/41496796/384349
    private class ColorableMediaRouteButton extends MediaRouteButton {
        protected Drawable mRemoteIndicatorDrawable;

        public ColorableMediaRouteButton(Context context) { super(context); }

        public ColorableMediaRouteButton(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ColorableMediaRouteButton(Context context, AttributeSet attrs,
                                         int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void setRemoteIndicatorDrawable(Drawable d) {
            mRemoteIndicatorDrawable = d;
            super.setRemoteIndicatorDrawable(d);
            if (mColor != null)
                applyTint(mColor);
        }

        public void applyTint(Integer color) {
            if (mRemoteIndicatorDrawable == null)
                return;

            Drawable wrapDrawable = DrawableCompat.wrap(mRemoteIndicatorDrawable);
            DrawableCompat.setTint(wrapDrawable, color);
        }
    }
}
