package com.trongphuc.emoappchat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChatActivity extends BaseActivity implements PagerFragment.OnClickStickerListener, PagerItemFragment.OnStickerPagerFragmentInteractionListener {
    private static final String TAG = "ChatActivity";
    private InputMethodManager inputManager;
    private EditText mInputEditText;
    private boolean isShowKb = false;
    private ImageButton btnEmo;
    private ImageButton btnSend;
    private boolean hasShowEmo = false;
    private TextView tvIsTyping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputEditText = (EditText) findViewById(R.id.inputText);
        btnSend = (ImageButton) findViewById(R.id.btn_send);
        btnEmo = (ImageButton) findViewById(R.id.btn_emo);
        tvIsTyping = (TextView) findViewById(R.id.is_typing);
        btnEmo.setSelected(true);
        refreshBtnSend(mInputEditText);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ChatFragment.newInstance())
                    .commit();
        }

        mInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshBtnSend(mInputEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void refreshBtnSend(EditText chatText) {
        String te = chatText.getText().toString();
        if (te.length() > 0) {
            btnSend.setEnabled(true);
            tvIsTyping.setVisibility(View.VISIBLE);
        } else {
            btnSend.setEnabled(false);
            tvIsTyping.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showFace(View view) {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (isShowKb) {
            inputManager.hideSoftInputFromWindow(
                    mInputEditText.getWindowToken(), 0);
            hasShowEmo = true;
        } else {
            if (mEmoView.getVisibility() == View.VISIBLE) {
                inputManager.showSoftInput(
                        mInputEditText, 0);
            } else {
                showFragment(true);
            }
        }
    }

    public void sendMessage(View view) {
        String msg;
        msg = mInputEditText.getText().toString();
        if (msg.isEmpty()) {
            return;
        }
        ChatFragment fm = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fm != null) {
            fm.sendMessage(msg);
        }
        mInputEditText.setText("");
    }

    private void showFragment(boolean isShowFragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.emo);
        if (fragment != null) {
            ft.setCustomAnimations(R.anim.fragment_slide_bottom_in,
                    R.anim.fragment_slide_bottom_out);
            if (isShowFragment) {
                mEmoView.setVisibility(View.VISIBLE);
                ft.show(fragment);
                ChatFragment fm = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.container);
                if (fm != null) {
                    fm.scrollToLast();
                }
                btnEmo.setSelected(false);
            } else {
                mEmoView.setVisibility(View.GONE);
                ft.hide(fragment);
                btnEmo.setSelected(true);
            }
        } else {
            if (isShowFragment) {
                mEmoView.setVisibility(View.VISIBLE);
                ft.setCustomAnimations(R.anim.fragment_slide_bottom_in,
                        R.anim.fragment_slide_bottom_out);
                ft.add(R.id.emo, new PagerFragment());
                btnEmo.setSelected(false);
                ChatFragment fm = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.container);
                if (fm != null) {
                    fm.scrollToLast();
                }
            }
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (mEmoView.getVisibility() == View.VISIBLE) {
            showFragment(false);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClickSticker(Uri uri) {
        boolean isTicker = true;
        boolean isMe = true;
        ChatFragment fm = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fm != null) {
            fm.sendMessage(isTicker, isMe);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public View getEmoView() {
        return findViewById(R.id.emo);
    }

    @Override
    public void onShowKeyboard() {
        Log.v(TAG, "on Show Keyboard");
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        showFragment(false);
        isShowKb = true;
        ChatFragment fm = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fm != null) {
            fm.scrollToLast();
        }
    }

    @Override
    public void onHideKeyboard() {
        Log.v(TAG, "On Hide Keyboard");
        isShowKb = false;
        if (hasShowEmo) {
            showFragment(true);
            hasShowEmo = false;
        }
    }
}
