package com.android.javier.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.javier.demo.DemoActivity;
import com.android.javier.demo.R;
import com.javier.simplemvc.app.SimpleFragment;
import com.javier.simplemvc.modules.notify.NotifyMessage;

/**
 * author:Javier
 * time:2016/5/21.
 * mail:38244704@qq.com
 */
public class Fragment1 extends SimpleFragment<DemoActivity> {

    private View mViewHolder = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mViewHolder == null) {
            mViewHolder = inflater.inflate(R.layout.fragment_1, container, false);
        }

        return mViewHolder;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initCommand() {

    }

    @Override
    public void addEventListener() {

    }

    @Override
    public void onInitComplete() {

    }

    @Override
    public void removeCommand() {

    }

    @Override
    public int[] listMessage() {
        return new int[0];
    }

    @Override
    public void handlerMessage(NotifyMessage message) {

    }
}
