package com.android.javier.demo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.javier.demo.entities.UserLoginEntity;
import com.android.javier.simplemvc.db.SimpleDao;
import com.android.javier.simplemvc.util.Logger;

/**
 * Created by javier
 */
public class UserDao extends SimpleDao {

    public UserDao(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String user_sql = "CREATE TABLE IF NOT EXISTS user (account TEXT, password TEXT)";
        db.execSQL(user_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE user");
        onCreate(db);
    }

    public void createUser(UserLoginEntity entity) {
        contentValues.clear();

        contentValues.put("account", entity.getAccount());
        contentValues.put("password", entity.getPassword());
        database.insert("user", null, contentValues);
    }
}