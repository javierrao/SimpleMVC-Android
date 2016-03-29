package com.android.javier.simplemvc.notify;

import com.android.javier.simplemvc.interfaces.INotify;

/**
 * Created by javie on 2016/3/27.
 */
public class Notify implements INotify {
    private String id;
    private int resId;
    private String name;
    private Object body;
    private Class<?> type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}