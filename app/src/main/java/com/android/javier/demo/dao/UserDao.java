package com.android.javier.demo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.javier.demo.entities.UserLoginEntity;
import com.javier.simplemvc.database.SimpleDao;

/**
 * Created by javier
 */
public class UserDao extends SimpleDao {

    public UserDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String user_sql = "CREATE TABLE IF NOT EXISTS user (account TEXT, id INTEGER)";
        database.execSQL(user_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE user");
        onCreate(database);
    }

    public void createUser(UserLoginEntity entity) {
        contentValues.clear();

        contentValues.put("account", entity.getAccount());
        contentValues.put("id", entity.getId());
        database.insert("user", null, contentValues);
    }
}