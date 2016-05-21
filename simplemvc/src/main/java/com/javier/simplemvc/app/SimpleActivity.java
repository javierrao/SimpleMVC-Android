package com.javier.simplemvc.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

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

    protected FragmentManager mFragmentManager;

    private FragmentTransaction mTransaction;
    private String[] mFragmentTags = null;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        init();
        initView();
        addEventListener();
        initCommand();
        onInitComplete();
    }

    private void init() {
        ModuleManager.getModuleManager().registerModule(this);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTags = getFragmentTags();
    }

    /**
     * 切换fragment
     *
     * @param tag      需要切换到的fragment的tag
     * @param layoutId 显示fragment的布局id
     */
    protected void switchFragment(String tag, int layoutId) {
        if (mFragmentTags == null) {
            logger.w("fragment tags is null");
            return;
        }

        beginTransaction();

        for (String fTag : mFragmentTags) {
            if (tag.equalsIgnoreCase(fTag)) {
                Fragment attachFragment = loadFragment(tag);

                if (attachFragment == null) {
                    logger.e("can not find fragment by tag : " + tag);
                    return;
                }

                attachFragment(layoutId, attachFragment, tag);
            } else {
                Fragment detachFragment = loadFragment(fTag);

                if (detachFragment == null) {
                    logger.e("can not find fragment by tag : " + fTag);
                    return;
                }

                detachFragment(detachFragment);
            }
        }

        commitTransaction();
    }

    /**
     * 开始事物
     *
     * @return FragmentTransaction
     */
    protected FragmentTransaction beginTransaction() {
        if (mTransaction == null) {
            mTransaction = mFragmentManager.beginTransaction();
        }

        return mTransaction;
    }


    /**
     * 提交事务
     */
    protected void commitTransaction() {
        if (mTransaction != null && !mTransaction.isEmpty()) {
            mTransaction.commit();
            mTransaction = null;
        }
    }

    /**
     * attach fragment
     *
     * @param layoutId 显示fragment的布局ID
     * @param f        需要attach的fragment
     * @param tag      fragment的tag
     */
    protected void attachFragment(int layoutId, Fragment f, String tag) {
        if (f != null) {
            if (f.isDetached()) {
                mTransaction.attach(f);
            } else if (!f.isAdded()) {
                mTransaction.add(layoutId, f, tag);
            }
        }
    }

    /**
     * detach fragment
     *
     * @param f 需要detach的fragment
     */
    private void detachFragment(Fragment f) {
        if (f != null && !f.isDetached()) {
            mTransaction.detach(f);
        }
    }

    private Fragment loadFragment(String tag) {
        Fragment f = mFragmentManager.findFragmentByTag(tag);

        if (f == null) {
            f = getFragment(tag);
        }

        return f;
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

    protected String[] getFragmentTags() {
        return null;
    }

    protected SimpleFragment getFragment(String tag) {
        return null;
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