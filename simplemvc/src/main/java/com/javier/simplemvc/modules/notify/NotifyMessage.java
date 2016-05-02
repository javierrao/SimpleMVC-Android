package com.javier.simplemvc.modules.notify;

import android.os.Bundle;

/**
 * author:Javier
 * time:2016/5/1.
 * mail:38244704@qq.com
 */
public class NotifyMessage {
    private int what;
    private Object[] params;
    private Object param;
    private Bundle bundle;

    public NotifyMessage() {

    }

    public NotifyMessage(int what, Object... param) {
        this.what = what;
        this.params = param;
    }

    public NotifyMessage(int what, Object object) {
        this.what = what;
        this.param = object;
    }

    public NotifyMessage(int what, Bundle bundle) {
        this.what = what;
        this.bundle = bundle;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
