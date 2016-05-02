package com.android.javier.demo.mediator;

import android.app.AlertDialog;

import com.android.javier.demo.R;
import com.javier.simplemvc.interfaces.IDisplay;
import com.javier.simplemvc.modules.mediator.SimpleMediator;
import com.javier.simplemvc.modules.notify.NotifyMessage;

/**
 * author:Javier
 * time:2016/5/2.
 * mail:38244704@qq.com
 */
public class LoginMediator extends SimpleMediator {
    public LoginMediator(int id, IDisplay display) {
        super(id, display);
    }

    @Override
    public int[] listMessage() {
        return new int[]{R.integer.msg_login_success, R.integer.msg_login_failed};
    }

    @Override
    public void handlerMessage(NotifyMessage message) {
        switch (message.getWhat()) {
            case R.integer.msg_login_success:
                AlertDialog dialog = new AlertDialog.Builder(display.getActivity()).create();
                dialog.setMessage("Sign in success");
                dialog.show();
                break;
            case R.integer.msg_login_failed:
                AlertDialog error = new AlertDialog.Builder(display.getActivity()).create();
                error.setMessage("Sign in failed");
                error.show();
                break;
        }
    }
}
