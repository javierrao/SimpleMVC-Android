package com.android.javier.simplemvc.interfaces;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by javier on 2016/4/5.
 * <p>
 * DAO 提供给服务器调用的接口
 */
public interface IDao {
    /**
     * 当数据库被创建的时候执行
     *
     * @param db 数据库
     */
    void onCreate(SQLiteDatabase db);

    /**
     * 当数据库升级的时候执行
     *
     * @param db 数据库
     */
    void onUpgrade(SQLiteDatabase db);
}
