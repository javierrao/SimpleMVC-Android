package com.android.javier.simplemvc.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.interfaces.IApplicationWidget;

/**
 * Created by javier
 */
@SuppressWarnings("unused")
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

    protected void doActionNotifyAsync(int notifyResId) {
        applicationContext.sendNotifyAsync(notifyResId, null, this);
    }

    protected Object doActionNotifySync(int notifyResId, String... body) {
        return applicationContext.sendNotifySync(notifyResId, body, this);
    }

    @Override
    public View getViewById(int resId) {
        return getActivity().findViewById(resId);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    protected abstract void initView();

    protected abstract void setEventListener();

    protected abstract void prepareComplete();
}