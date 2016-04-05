package com.android.javier.simplemvc.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.interfaces.IAction;
import com.android.javier.simplemvc.interfaces.IApplicationWidget;
import com.android.javier.simplemvc.interfaces.INotify;

/**
 * Created by javier on 2016/4/3.
 */
public abstract class SimpleFragment extends Fragment implements IApplicationWidget {

    protected ApplicationContext applicationContext;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        initView();
        setEventListener();
        prepareComplete();
    }

    protected void init() {
        applicationContext = ApplicationContext.getApplicationContext(getContext());
    }

    protected void doActionNotify(int notifyResId, Object body) {
        applicationContext.sendNotify(notifyResId, body, this);
    }

    protected void doActionNotify(int notifyResId, String... body) {
        applicationContext.sendNotify(notifyResId, body, this);
    }

    @Override
    public View getViewById(int resid) {
        return getActivity().findViewById(resid);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    protected abstract void initView();

    protected abstract void setEventListener();

    protected abstract void prepareComplete();
}