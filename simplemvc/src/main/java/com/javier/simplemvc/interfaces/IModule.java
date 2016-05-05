package com.javier.simplemvc.interfaces;

import com.javier.simplemvc.modules.notify.NotifyMessage;

/**
 * author:Javier
 * time:2016/5/5.
 * mail:38244704@qq.com
 */
public interface IModule {
    int[] listMessage();
    void handlerMessage(NotifyMessage message);
    void onRemove();
    void onRegister();
}