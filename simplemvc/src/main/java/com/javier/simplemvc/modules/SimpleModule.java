package com.javier.simplemvc.modules;

import android.os.Bundle;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.modules.notify.NotifyMessage;

/**
 * author:Javier
 * time:2016/5/4.
 * mail:38244704@qq.com
 */
public class SimpleModule {

    protected SimpleContext simpleContext;

    public SimpleModule() {
        simpleContext = SimpleContext.getInstance();
    }

    /**
     * 发送带参数的消息
     * @param what  消息唯一标识
     * @param param 参数
     */
    protected void sendNotifyMessage(int what, Object param) {
        simpleContext.notifyObservers(new NotifyMessage(what, param));
    }

    /**
     * 发送不带参数消息
     * @param what  消息唯一标识
     */
    protected void sendNotifyMessage(int what) {
        simpleContext.notifyObservers(new NotifyMessage(what));
    }

    /**
     * 发送带多个参数的消息
     * @param what  消息唯一标识
     * @param param 参数数组
     */
    protected void sendNotifyMessage(int what, Object... param) {
        simpleContext.notifyObservers(new NotifyMessage(what, param));
    }

    /**
     * 发送带有bundle参数的消息
     * @param what  消息唯一标识
     * @param bundle    Bundle对象
     */
    protected void sendNotifyMessage(int what, Bundle bundle) {
        simpleContext.notifyObservers(new NotifyMessage(what, bundle));
    }
}
