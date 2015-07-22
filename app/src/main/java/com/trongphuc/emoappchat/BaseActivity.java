package com.trongphuc.emoappchat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by Phuc on 7/22/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected RelativeLayout mEmoView;
    public static final String KEYBOARD_KEY = "keyboard_height";
    private static final String TAG = "BottomStickerCtrl";

    @Override
    protected void onStart() {
        super.onStart();
        mEmoView = (RelativeLayout) getEmoView();
        final ViewGroup parentLayout = (ViewGroup) findViewById(android.R.id.content);
        int savedHeight = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(KEYBOARD_KEY,
                        (int) getResources().getDimension(
                                R.dimen.default_keyboard_height));
        setEmoHeight(mEmoView, savedHeight);

        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    private int oldKeyboardHeight;
                    private boolean hasCallback;

                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        parentLayout.getWindowVisibleDisplayFrame(r);

                        int screenHeight;
                        if (Build.VERSION.SDK_INT >= 5.0) {
                            screenHeight = calculateScreenHeightForLollipop();
                        } else {
                            screenHeight = parentLayout.getRootView().getHeight();
                        }
                        int keyboardHeight = screenHeight - (r.bottom);
                        if (HardwareUtils.hasNavBar(BaseActivity.this)) {
                            int resourceId = getResources()
                                    .getIdentifier("navigation_bar_height",
                                            "dimen", "android");
                            if (resourceId > 0) {
                                keyboardHeight -= getResources()
                                        .getDimensionPixelSize(resourceId);
                            }
                        }

                        if (keyboardHeight > 100) {
                            if (oldKeyboardHeight != keyboardHeight) {
                                oldKeyboardHeight = keyboardHeight;
                                setEmoHeight(mEmoView, oldKeyboardHeight);

                                SharedPreferences preferences = PreferenceManager
                                        .getDefaultSharedPreferences(BaseActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt(KEYBOARD_KEY, oldKeyboardHeight);
                                Log.v(TAG, "oldKeyboardHeight: " + oldKeyboardHeight);
                                editor.apply();
                            }
                                if (!hasCallback) {
                                    hasCallback = true;
                                    onShowKeyboard();
                                }
                        } else {
                            hasCallback = false;
                            onHideKeyboard();
                        }
                    }
                });
    }

    public abstract View getEmoView();
    public abstract void onShowKeyboard();
    public abstract void onHideKeyboard();

    private void setEmoHeight(RelativeLayout stickerSet, int savedHeight) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, savedHeight);
        stickerSet.setLayoutParams(params);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public int calculateScreenHeightForLollipop() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }
}
