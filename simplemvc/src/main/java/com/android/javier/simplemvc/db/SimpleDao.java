package com.android.javier.simplemvc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.javier.simplemvc.interfaces.IDao;
import com.android.javier.simplemvc.interfaces.INotify;

/**
 * Created by javier on 2016/4/5.
 * <p/>
 * 抽象类，为所有 DAO 的父类，实现了 IDao 接口，定义了 Dao 的构造方法
 */
public abstract class SimpleDao implements IDao {
    protected Context context;
    protected SQLiteDatabase database;
    protected ContentValues contentValues;

    protected SimpleDao(Context context, SQLiteDatabase database) {
        this.context = context;
        this.database = database;

        contentValues = new ContentValues();
    }
}