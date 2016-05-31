package com.javier.simpleui.securitykeyboard;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.javier.simpleui.R;
import com.javier.simpleui.securitykeyboard.KeyBoardUtils;

/**
 * @author Make It
 * @body 安全密码输入框
 * @QQ: 347357000
 * Created at 2016/1/19 15:00
 */
public class SecurityPasswordEditText extends LinearLayout {
    private final int mPwdLength = 6;
    private EditText mEditText;
    private ImageView oneTextView;
    private ImageView twoTextView;
    private ImageView threeTextView;
    private ImageView fourTextView;
    private ImageView fiveTextView;
    private ImageView sixTextView;
    LayoutInflater inflater;
    ImageView[] imageViews;
    View contentView;

    public SecurityPasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            inflater = LayoutInflater.from(context);
            builder = new StringBuilder();
            initWidget();
        }
    }

    public String getText() {
        return builder.toString();
    }

    private void initWidget() {
        if (!isInEditMode()) {
            contentView = inflater.inflate(R.layout.num6_pwd_widget, null);
            mEditText = (EditText) contentView
                    .findViewById(R.id.num6_edit_ecurity);
            oneTextView = (ImageView) contentView
                    .findViewById(R.id.num6_pwd_one_img);
            twoTextView = (ImageView) contentView
                    .findViewById(R.id.num6_pwd_two_img);
            fourTextView = (ImageView) contentView
                    .findViewById(R.id.num6_pwd_four_img);
            fiveTextView = (ImageView) contentView
                    .findViewById(R.id.num6_pwd_five_img);
            sixTextView = (ImageView) contentView
                    .findViewById(R.id.num6_pwd_six_img);
            threeTextView = (ImageView) contentView
                    .findViewById(R.id.num6_pwd_three_img);
            LayoutParams lParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            mEditText.addTextChangedListener(mTextWatcher);
            mEditText.setOnKeyListener(keyListener);
            imageViews = new ImageView[]{oneTextView, twoTextView, threeTextView,
                    fourTextView, fiveTextView, sixTextView};
            this.addView(contentView, lParams);
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.toString().length() == 0) {
                return;
            }

            if (builder.length() < mPwdLength) {
                builder.append(s.toString());
                setTextValue();
            }
            s.delete(0, s.length());
        }

    };

    OnKeyListener keyListener = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_DEL
                    && event.getAction() == KeyEvent.ACTION_UP) {
                delTextValue();
                return true;
            }
            return false;
        }
    };

    private void setTextValue() {

        String str = builder.toString();
        int len = str.length();

        if (len <= mPwdLength) {
            imageViews[len - 1].setVisibility(View.VISIBLE);
        }
        if (len == mPwdLength) {
            Log.i("test", "回调");
            Log.i("test", "支付密码" + str);
            if (mListener != null) {
                mListener.onNumCompleted(str);
            }
            Log.i("test", builder.toString());
            KeyBoardUtils.closeKeybord(mEditText, getContext());
        }
    }

    public void delTextValue() {
        String str = builder.toString();
        int len = str.length();
        if (len == 0) {
            return;
        }
        if (len > 0 && len <= mPwdLength) {
            builder.delete(len - 1, len);
        }
        imageViews[len - 1].setVisibility(View.INVISIBLE);

        if ((len == mPwdLength) && mListener != null) {
            mListener.unCompleted(str);
        }
    }

    StringBuilder builder;

    public interface SecurityEditCompleListener {
        public void onNumCompleted(String num);

        public void unCompleted(String num);
    }

    public SecurityEditCompleListener mListener;

    public void setSecurityEditCompleListener(
            SecurityEditCompleListener mListener) {
        this.mListener = mListener;
    }

    public void clearSecurityEdit() {
        if (builder != null) {
            if (builder.length() == mPwdLength) {
                builder.delete(0, mPwdLength);
            }
        }
        for (ImageView tv : imageViews) {
            tv.setVisibility(View.INVISIBLE);
        }

    }

    public EditText getEditText() {
        return this.mEditText;
    }
}  