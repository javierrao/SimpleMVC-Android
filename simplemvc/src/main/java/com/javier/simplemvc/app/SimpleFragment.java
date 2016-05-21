package com.javier.simplemvc.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.core.ModuleManager;
import com.javier.simplemvc.interfaces.IAppWidget;
import com.javier.simplemvc.modules.notify.NotifyMessage;
import com.javier.simplemvc.utils.Logger;

/**
 * author:Javier
 * time:2016/5/1.
 * mail:38244704@qq.com
 */
@SuppressWarnings("unused")
public abstract class SimpleFragment extends Fragment implements IAppWidget {
    protected Logger logger = Logger.getLogger();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        addEventListener();
        initCommand();
        onInitComplete();
    }

    protected void registerCommand(Class<?> commandClass) {
        ModuleManager.getModuleManager().registerModule(commandClass);
    }

    protected void sendNotifyMessage(NotifyMessage message) {
        SimpleContext.getInstance().notifyObservers(message);
    }

    protected void sendNotifyMessage(int what, Object... param) {
        SimpleContext.getInstance().notifyObservers(new NotifyMessage(what, param));
    }

    protected void sendNotifyMessage(int what, Object param) {
        SimpleContext.getInstance().notifyObservers(new NotifyMessage(what, param));
    }

    protected void removeCommand(Class commandClass) {
        ModuleManager.getModuleManager().registerModule(commandClass);
    }

    protected View findViewById(int resId) {
        return getActivity().findViewById(resId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        removeCommand();
    }
}