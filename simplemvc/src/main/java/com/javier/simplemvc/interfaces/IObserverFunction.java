package com.javier.simplemvc.interfaces;

import com.javier.simplemvc.modules.notify.NotifyMessage;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 * <p/>
 * 观察者回调接口，需要观察消息的对象，都需要实现该接口
 * 当对应的消息被发出的时候，Observer调用注册的观察者对象ObserverFunction方法
 */
public interface IObserverFunction {
    /**
     * 观察者回调函数
     *
     * @param message 发出的消息
     */
    void ObserverFunction(NotifyMessage message);
}
