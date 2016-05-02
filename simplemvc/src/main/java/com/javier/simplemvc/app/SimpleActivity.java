package com.javier.simplemvc.app;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.core.Controller;
import com.javier.simplemvc.interfaces.IAppWidget;
import com.javier.simplemvc.interfaces.IDisplay;
import com.javier.simplemvc.modules.notify.NotifyMessage;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
public abstract class SimpleActivity extends FragmentActivity implements IDisplay, IAppWidget {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        initView();
        addEventListener();
        initCommand();
        onInitComplete();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        removeCommand();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    protected void registerCommand(Class<?> commandClass) {
        Controller.getInstance().registerCommand(commandClass, this);
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
        Controller.getInstance().removeCommand(commandClass);
    }

    @Override
    public void initCommand() {
        
    }

    @Override
    public void removeCommand() {

    }
}