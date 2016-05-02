package com.javier.simplemvc.interfaces;

import android.database.sqlite.SQLiteDatabase;

/**
 * author:Javier
 * time:2016/5/1.
 * mail:38244704@qq.com
 */
public interface IDao {
    void onCreate(SQLiteDatabase database);

    void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion);
}