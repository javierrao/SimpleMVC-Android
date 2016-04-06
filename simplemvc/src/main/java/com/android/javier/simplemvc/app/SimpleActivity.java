package com.android.javier.simplemvc.app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.interfaces.IApplicationWidget;

@SuppressWarnings("unused")
public abstract class SimpleActivity extends Activity implements IApplicationWidget {

    protected ApplicationContext applicationContext;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        init();
        initView();
        setEventListener();
        prepareComplete();
    }

    protected void init() {
        applicationContext = ApplicationContext.getApplicationContext(getApplicationContext());
    }

    protected void doActionNotify(int notifyResId, Object body) {
        applicationContext.sendNotify(notifyResId, body, this);
    }

    protected void doActionNotify(int notifyResId, String... body) {
        applicationContext.sendNotify(notifyResId, body, this);
    }

    @Override
    public View getViewById(int resId) {
        return findViewById(resId);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    protected abstract void initView();

    protected abstract void setEventListener();

    protected abstract void prepareComplete();
}