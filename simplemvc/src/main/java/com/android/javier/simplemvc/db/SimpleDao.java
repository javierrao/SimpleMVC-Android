package com.android.javier.simplemvc.db;

import android.content.Context;

import com.android.javier.simplemvc.interfaces.IDao;

/**
 * Created by javier on 2016/4/5.
 */
public abstract class SimpleDao implements IDao{
    protected Context context;

    protected SimpleDao(Context context) {
        this.context = context;
    }
}
