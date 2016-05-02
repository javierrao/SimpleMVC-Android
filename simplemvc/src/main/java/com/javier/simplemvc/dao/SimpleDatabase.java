package com.javier.simplemvc.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.SparseArray;

import com.javier.simplemvc.interfaces.IDao;

import java.lang.reflect.Constructor;

/**
 * Created by javier on 2016/4/5.
 * <p/>
 * 创建数据库的类，封装了基本数据库的实现，如创建、升级、打开、关闭、销毁
 */
@SuppressWarnings("unused")
public final class SimpleDatabase {

    public final static int INSERT = 996;
    public final static int UPDATE = 997;
    public final static int DELETE = 998;
    public final static int SELECT = 999;

    /**
     * SimpleDatabase对象
     */
    private static SimpleDatabase simpleDatabase;

    /**
     * SQLiteOpenHelper 类的子类
     */
    private SimpleDatabaseHelper simpleDatabaseHelper;

    /**
     * 操作数据库的对象，SQLiteDatabase
     */
    private SQLiteDatabase database = null;

    /**
     * 保存 DAO 的 map
     */
    private SparseArray<Class> daoArray;

    /**
     * 上下文
     */
    private Context context;

    private class SimpleDatabaseHelper extends SQLiteOpenHelper {
        public SimpleDatabaseHelper(Context context, String name,
                                    CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        /**
         * 在数据库的 onCreate 被调用的时候，遍历 dao map 调用 IDAO 中的 onCreate 方法
         *
         * @param db 数据操作对象
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            if (daoArray == null)
                return;

            int key;
            try {
                for (int i = 0; i < daoArray.size(); i++) {
                    key = daoArray.keyAt(i);

                    Constructor<?>[] cons = daoArray.get(key).getConstructors();

                    IDao dao = (IDao) cons[0].newInstance(context, db);

                    dao.onCreate(db);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 在数据库的 onCreate 被调用的时候，遍历 dao map 调用 IDAO 中的 onCreate 方法
         *
         * @param db 数据操作对象
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (daoArray == null)
                return;

            int key;
            try {
                for (int i = 0; i < daoArray.size(); i++) {
                    key = daoArray.keyAt(i);

                    Constructor<?>[] cons = daoArray.get(key).getConstructors();

                    IDao dao = (IDao) cons[0].newInstance(context, db);

                    dao.onUpgrade(db, oldVersion, newVersion);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取 SimpleDatabase 的单例方法
     *
     * @return SimpleDatabase对象
     */
    public static SimpleDatabase getSimpleDatabase() {
        if (simpleDatabase == null) {
            simpleDatabase = new SimpleDatabase();
        }

        return simpleDatabase;
    }

    /**
     * 初始化数据库，非常重要，只有在数据库被初始化以后才可以进行后续的操作
     *
     * @param context 应用程序上下文
     */
    public void initDatabase(Context context, String dbName, int version, SparseArray daoClasses) {
        this.daoArray = daoClasses;
        this.context = context;

        if (simpleDatabaseHelper == null) {
            simpleDatabaseHelper = new SimpleDatabaseHelper(context, dbName, null, version);
            database = simpleDatabaseHelper.getWritableDatabase();
        }
    }

    /**
     * 打开数据库
     *
     * @return SQLiteDatabase对象
     */
    public SQLiteDatabase open() {
        if (database == null) {
            database = simpleDatabaseHelper.getWritableDatabase();
        }

        return database;
    }

    /**
     * 关闭数据库
     */
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    /**
     * 销毁数据库
     */
    public void destroy() {
        if (database != null) {
            database.close();
        }

        if (simpleDatabaseHelper != null) {
            simpleDatabaseHelper.close();
        }

        database = null;
        simpleDatabaseHelper = null;
        simpleDatabase = null;
    }
}