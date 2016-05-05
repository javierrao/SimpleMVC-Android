package com.javier.simplemvc.app;

import android.support.v4.app.FragmentActivity;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.core.ModuleManager;
import com.javier.simplemvc.interfaces.IAppWidget;
import com.javier.simplemvc.modules.notify.NotifyMessage;
import com.javier.simplemvc.utils.Logger;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
@SuppressWarnings("unused")
public abstract class SimpleActivity extends FragmentActivity implements IAppWidget {

    protected Logger logger = Logger.getLogger();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        init();
        initView();
        initCommand();
        addEventListener();
        onInitComplete();
    }

    private void init() {
        ModuleManager.getModuleManager().registerModule(this);
    }

    protected void registerCommand(Class commandClass) {
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
        ModuleManager.getModuleManager().removeModule(commandClass);
    }

    @Override
    public void onRemove() {

    }

    @Override
    public void onRegister() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ModuleManager.getModuleManager().removeModule(this);
    }
}