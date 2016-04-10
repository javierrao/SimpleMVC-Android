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

    /**
     * 发送同步无参action消息
     *
     * @param notifyResId 消息ID
     */
    protected Object doActionNotifySync(int notifyResId) {
        return applicationContext.sendNotifySync(notifyResId, null, this);
    }

    /**
     * 发送同步有参action消息
     * @param notifyResId 消息ID
     * @param body 参数
     * @return
     */
    protected Object doActionNotifySync(int notifyResId, Object... body) {
        return doActionNotifySync(notifyResId, body, this);
    }

    /**
     * 发送异步无参action消息
     *
     * @param notifyResId 消息ID
     */
    protected void doActionNotifyAsync(int notifyResId) {
        applicationContext.sendNotifyAsync(notifyResId, null, this);
    }

    /**
     * 发送异步无参action消息
     * @param notifyResId 消息ID
     * @param body 参数
     */
    protected void doActionNotifyAsync(int notifyResId, Object... body) {
        doActionNotifyAsync(notifyResId, body, this);
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