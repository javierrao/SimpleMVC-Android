package com.android.javier.simplemvc.interfaces;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by javie on 2016/4/5.
 */
public interface IDao {
    void onCreate(SQLiteDatabase db);
    void onUpgrade(SQLiteDatabase db);
}
