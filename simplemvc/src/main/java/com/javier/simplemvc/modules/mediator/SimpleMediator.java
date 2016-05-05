package com.javier.simplemvc.modules.mediator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.javier.simplemvc.interfaces.IDisplay;
import com.javier.simplemvc.interfaces.IMediator;
import com.javier.simplemvc.modules.SimpleModule;
import com.javier.simplemvc.utils.Logger;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
@SuppressWarnings("unused")
public abstract class SimpleMediator extends SimpleModule implements IMediator {
    protected IDisplay display;
    protected int id;
    protected Logger logger = Logger.getLogger();

    protected SimpleMediator(int id, IDisplay display) {
        super();
        this.display = display;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void onRemove() {

    }

    @Override
    public void onRegister() {

    }

    public Activity getActivity() {
        return display.getActivity();
    }

    public Fragment getFragment() {
        return display.getFragment();
    }

    public Context getContext() {
        return display.getContext();
    }

    public void startActivity(Intent intent) {
        if (getActivity() != null) {
            getActivity().startActivity(intent);
        }
    }

    public void finish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    public View findViewById(int resId) {
        if (getActivity() != null) {
            getActivity().findViewById(resId);
        }

        return null;
    }
}