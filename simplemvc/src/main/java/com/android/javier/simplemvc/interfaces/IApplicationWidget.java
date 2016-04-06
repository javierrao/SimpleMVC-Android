package com.android.javier.simplemvc.interfaces;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by javier on 2016/3/27.
 * <p>
 * ApplicationWidget的接口，activity和fragment都实现了该接口。提供action操作UI界面的接口
 */
@SuppressWarnings("unused")
public interface IApplicationWidget {
    /**
     * 回调消息到UI
     *
     * @param notify 消息
     * @param action 回调的action
     */
    void handleNotify(INotify notify, IAction action);

    /**
     * 提供在action中通过id查找UI的view
     *
     * @param resId 组件ID
     * @return 组件
     */
    View getViewById(int resId);

    /**
     * 获取activity对象
     *
     * @return activity对象
     */
    Activity getActivity();

    /**
     * 获取fragment对象
     *
     * @return fragment对象
     */
    Fragment getFragment();
}