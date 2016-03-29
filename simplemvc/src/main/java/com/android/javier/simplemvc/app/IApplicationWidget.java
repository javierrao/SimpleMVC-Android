package com.android.javier.simplemvc.app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.javier.simplemvc.interfaces.IAction;
import com.android.javier.simplemvc.interfaces.INotify;

/**
 * Created by javie on 2016/3/27.
 */
public interface IApplicationWidget {
    void handleNotify(INotify notify, IAction action);
    View getViewById(int resid);
    Activity getActivity();
    Fragment getFragment();
}