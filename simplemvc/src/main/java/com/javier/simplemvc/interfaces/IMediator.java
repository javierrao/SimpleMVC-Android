package com.javier.simplemvc.interfaces;

import com.javier.simplemvc.modules.notify.NotifyMessage;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
public interface IMediator {
    int[] listMessage();

    int getId();

    void handlerMessage(NotifyMessage message);

    void onRemove();

    void onRegister();
}
