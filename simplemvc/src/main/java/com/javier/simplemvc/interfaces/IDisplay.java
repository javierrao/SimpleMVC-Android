package com.javier.simplemvc.interfaces;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
public interface IDisplay {
    Activity getActivity();

    Fragment getFragment();

    Context getContext();
}