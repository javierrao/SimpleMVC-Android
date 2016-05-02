package com.javier.simplemvc.interfaces;

import android.os.Message;

import com.javier.simplemvc.modules.notify.NotifyMessage;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
public interface ICommand {
    void execute(NotifyMessage message);

    int[] listMessage();

    void onRemove();

    void onRegister();
}