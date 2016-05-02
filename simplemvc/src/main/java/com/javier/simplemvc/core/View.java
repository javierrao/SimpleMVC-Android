package com.javier.simplemvc.core;

import android.util.SparseArray;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.interfaces.IMediator;
import com.javier.simplemvc.interfaces.IObserverFunction;
import com.javier.simplemvc.modules.notify.NotifyMessage;
import com.javier.simplemvc.utils.Logger;

import java.util.ArrayList;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 * <p/>
 * mediator的管理类，作为显示层，在mediator中处理了与UI相关的逻辑
 */
public final class View implements IObserverFunction {
    private static View view;

    private SparseArray<IMediator> mediatorSparseArray = new SparseArray<>();

    public synchronized static View getInstance() {
        if (view == null) {
            view = new View();
        }

        return view;
    }

    /**
     * 注册mediator
     *
     * @param mediator 需要注册的UI中介类对象
     */
    public void registerMediator(IMediator mediator) {
        int[] listMessage = mediator.listMessage();

        Observer observer = new Observer(this);

        for (int aListMessage : listMessage) {
            SimpleContext.getInstance().registerObserver(aListMessage, observer);

            mediatorSparseArray.put(aListMessage, mediator);
        }

        mediator.onRegister();
    }

    @Override
    public void ObserverFunction(NotifyMessage message) {
        if (mediatorSparseArray.size() == 0) {
            Logger.getLogger().e("mediator not found with " + message);
            return;
        }

        IMediator mediator = mediatorSparseArray.get(message.getWhat());
        mediator.handlerMessage(message);
    }

    /**
     * 注销mediator
     *
     * @param mediatorId 需要注销的Mediator对象的ID
     */
    public void removeMediator(int mediatorId) {
        ArrayList<Integer> keys = new ArrayList<>();

        for (int i = 0; i < mediatorSparseArray.size(); i++) {
            int keyAt = mediatorSparseArray.keyAt(i);

            if (mediatorSparseArray.get(keyAt).getId() == mediatorId) {
                keys.add(keyAt);
            }
        }

        if (null == keys)
            return;

        IMediator mediator = mediatorSparseArray.get(keys.get(0));
        mediator.onRemove();

        for (int j = 0; j < keys.size(); j++) {
            mediatorSparseArray.remove(keys.get(j));
        }
    }
}