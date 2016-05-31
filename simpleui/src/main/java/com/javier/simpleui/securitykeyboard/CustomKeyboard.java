/**
 * Copyright 2013 Maarten Pennings
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * If you use this software in a product, an acknowledgment in the product
 * documentation would be appreciated but is not required.
 */

package com.javier.simpleui.securitykeyboard;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.javier.simpleui.R;

/**
 * When an activity hosts a keyboardView, this class allows several EditText's
 * to register for it.
 *
 * @author Maarten Pennings
 * @date 2012 December 23
 */
public class CustomKeyboard {

    private KeyboardView mKeyboardView;

    private EditText mEditText;
    private Activity mActivity;

    private OnKeyboardListener onKeyboardListener;

    public void setOnKeyboardListener(OnKeyboardListener onKeyboardListener) {
        this.onKeyboardListener = onKeyboardListener;
    }

    private OnKeyboardActionListener mOnKeyboardActionListener = new OnKeyboardActionListener() {

        public final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
        public final static int CodeCancel = -3; // Keyboard.KEYCODE_CANCEL

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (mEditText == null)
                return;
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            // Apply the key to the edittext
            if (primaryCode == CodeCancel) {
                // hideCustomKeyboard();
                if (onKeyboardListener != null)
                    onKeyboardListener.onCancelKey();
            } else if (primaryCode == CodeDelete) {
                if (onKeyboardListener != null)
                    onKeyboardListener.onDelKey();
                if (editable != null && start > 0)
                    editable.delete(start - 1, start);
            } else { // insert character
                if (onKeyboardListener != null)
                    onKeyboardListener.onKey(primaryCode, keyCodes);
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }

        @Override
        public void onPress(int arg0) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeUp() {
        }
    };

    /**
     * 使用KeyboardView创建一个自定义的键盘
     *
     * @param activity     The hosting activity.
     * @param keyboardView 继承KeyboardView.
     */
    public CustomKeyboard(Activity activity, KeyboardView keyboardView) {
        mActivity = activity;
        mKeyboardView = keyboardView;
        mKeyboardView.setKeyboard(new Keyboard(activity, R.xml.keyboard));
        mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview
        // balloons
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        // Hide the standard keyboard initially
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Returns whether the CustomKeyboard is visible.
     */
    public boolean isCustomKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    /**
     * Make the CustomKeyboard visible, and hide the system keyboard for view v.
     */
    public void showCustomKeyboard(View v) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if (v != null)
            ((InputMethodManager) mActivity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Make the CustomKeyboard invisible.
     */
    public void hideCustomKeyboard() {
        mKeyboardView.setVisibility(View.INVISIBLE);
        mKeyboardView.setEnabled(false);
    }

    /**
     * Register <var>EditText<var> with resource id <var>resid</var> (on the
     * hosting activity) for using this custom keyboard.
     *
     * @param edittext The resource id of the EditText that registers to the custom
     *                 keyboard.
     */
    public void registerEditText(EditText edittext) {
        this.mEditText = edittext;
        // Make the custom keyboard appear
        edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showCustomKeyboard(v);
                else
                    hideCustomKeyboard();
            }
        });
        edittext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomKeyboard(v);
            }
        });
        edittext.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType(); // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard
                // keyboard
                edittext.onTouchEvent(event); // Call native handler
                edittext.setInputType(inType); // Restore input type
                return true; // Consume touch event
            }
        });
        edittext.setInputType(edittext.getInputType()
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    public interface OnKeyboardListener {
        void onKey(int primaryCode, int[] keyCodes);

        void onCancelKey();

        void onDelKey();
    }
}