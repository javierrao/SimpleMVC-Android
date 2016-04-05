package com.android.javier.simplemvc.entity;

import com.android.javier.simplemvc.notify.Notify;

import java.util.ArrayList;

/**
 * Created by javier on 2016/3/27.
 */
public final class ActionEntity {
    private int id;
    private String name;

    private ArrayList<Notify> notifies;

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

    public ArrayList<Notify> getNotifies() {
        return notifies;
    }

    public void setNotifies(ArrayList<Notify> notifies) {
        this.notifies = notifies;
    }
}
