package com.android.javier.simplemvc.notify;

import com.android.javier.simplemvc.interfaces.INotify;

/**
 * Created by javier on 2016/3/27.
 */
public class Notify implements INotify {
    private int id;
    private String name;
    private Object body;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}