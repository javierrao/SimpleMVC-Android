package com.javier.simplemvc.patterns.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.core.NotifyManager;
import com.javier.simplemvc.interfaces.IView;
import com.javier.simplemvc.patterns.notify.NotifyMessage;
import com.javier.simplemvc.util.Logger;

/**
 * author:Javier
 * time:2016/5/28.
 * mail:38244704@qq.com
 */
public abstract class SimpleFragment<T> extends Fragment implements IView {
    protected Logger logger = Logger.getLogger();

    protected SimpleContext simpleContext;
    protected NotifyManager notifyManager = NotifyManager.getInstance();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        simpleContext = SimpleContext.getSimpleContext();

        onInitView();
        onSetEventListener();
        onInitComplete();
    }


    @Override
    public void onResume() {
        super.onResume();

        simpleContext.registerView(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        simpleContext.removeView(this);
    }

    protected T findActivity() {
        return (T) super.getActivity();
    }

    protected View findViewById(int resId) {
        return getActivity().findViewById(resId);
    }


    @Override
    public void onRemove() {

    }

    @Override
    public void onRegister() {

    }

    @Override
    public String[] listMessage() {
        return new String[0];
    }

    @Override
    public void handlerMessage(NotifyMessage message) {

    }
}