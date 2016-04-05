package com.android.javier.demo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.javier.simplemvc.db.SimpleDao;
import com.android.javier.simplemvc.util.Logger;

/**
 * Created by javier on 2016/4/5.
 */
public class UserDao extends SimpleDao {

    public UserDao(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db) {

    }

    public void createUser() {
        Logger.getLogger().i("create user");
    }
}
