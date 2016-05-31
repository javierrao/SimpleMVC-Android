package com.javier.simplemvc.interfaces;

import com.javier.simplemvc.patterns.notify.NotifyMessage;

/**
 * author:Javier
 * time:2016/5/28.
 * mail:38244704@qq.com
 */
public interface IModule {
    void handlerMessage(NotifyMessage message);
    String[] listMessage();
    void onRemove();
    void onRegister();
}
