package com.android.javier.simplemvc.app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.javier.simplemvc.ApplicationContext;

/**
 * Created by javier on 2016/3/25.
 */
public abstract class AbstractActivity extends Activity implements IApplicationWidget {

    protected ApplicationContext applicationContext;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        init();
        initView();
        setEventListener();
        prepareComplete();
    }

    private void init() {
        applicationContext = ApplicationContext.getApplicationContext(getApplicationContext());
    }

    protected void sendNotification(String notifyId, Object body) {
        applicationContext.sendNotify(notifyId, body, this);
    }

    protected void sendNotification(int notifyResId, Object body) {
        applicationContext.sendNotify(notifyResId, body, this);
    }

    protected void sendNotification(int notifyResId, String... body) {
        applicationContext.sendNotify(notifyResId, body, this);
    }

    @Override
    public View getViewById(int resid) {
        return findViewById(resid);
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