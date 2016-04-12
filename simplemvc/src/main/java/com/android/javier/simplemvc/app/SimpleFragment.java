package com.android.javier.simplemvc.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.interfaces.IAction;
import com.android.javier.simplemvc.interfaces.IApplicationWidget;
import com.android.javier.simplemvc.interfaces.INotify;
import com.android.javier.simplemvc.util.Logger;

/**
 * Created by javier
 */
@SuppressWarnings("unused")
public abstract class SimpleFragment extends Fragment implements IApplicationWidget {

    protected ApplicationContext applicationContext;

    protected Logger logger = Logger.getLogger();

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

    @Override
    public void handleNotify(INotify notify, IAction action) {

    }

    @Override
    public void handleNotify(int notify, IAction action) {

    }

    protected abstract void initView();

    protected abstract void setEventListener();

    protected abstract void prepareComplete();
}