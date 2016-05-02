package com.javier.simplemvc.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.javier.simplemvc.interfaces.IAppWidget;
import com.javier.simplemvc.interfaces.IDisplay;

/**
 * author:Javier
 * time:2016/5/1.
 * mail:38244704@qq.com
 */
@SuppressWarnings("unused")
public abstract class SimpleFragment extends Fragment implements IDisplay, IAppWidget {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        addEventListener();
        initCommand();
        onInitComplete();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        removeCommand();
    }
}
