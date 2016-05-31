package com.javier.simpleui.securitykeyboard;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author Make It
 * @body KeyBoardUtils 打开或关闭软键盘
 * @QQ: 347357000
 * @time 2016/2/29 15:35
 */
public class KeyBoardUtils {

    /**
     * @param mEditText 输入框
     * @param mContext  上下文
     * @body 打开某一个输入框软键盘
     * @author Make It
     * @QQ: 347357000
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * @param mContext 上下文
     * @body 隐藏整个界面中的软键盘
     * @author Make It
     * @QQ: 347357000
     */
    public static void hideInput(Context mContext) {
        if (((Activity) mContext).getCurrentFocus() == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * @param mEditText 输入框
     * @param mContext  上下文
     * @body 关闭某一个输入框的软键盘
     * @author Make It
     * @QQ: 347357000
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
