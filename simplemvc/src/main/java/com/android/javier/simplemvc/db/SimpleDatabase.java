package com.android.javier.simplemvc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.SparseArray;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.entity.DataSourceEntity;
import com.android.javier.simplemvc.interfaces.IDao;

/**
 * Created by javier on 2016/4/5.
 * <p/>
 * 创建数据库的类，封装了基本数据库的实现，如创建、升级、打开、关闭、销毁
 */
@SuppressWarnings("unused")
public final class SimpleDatabase {

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
    private SparseArray<String> daoArray = new SparseArray<>();

    /**
     * ApplicationContext对象，用于获取 DAO MAP
     */
    private ApplicationContext applicationContext = null;

    private class SimpleDatabaseHelper extends SQLiteOpenHelper {
        public SimpleDatabaseHelper(Context context, String name,
                                    CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        /**
         * 在数据库的 onCreate 被调用的时候，遍历 dao map 调用 IDAO 中的 onCreate 方法
         *
         * @param db 数据操作对象
         * @see com.android.javier.simplemvc.interfaces.IDao#onCreate(SQLiteDatabase)
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            int key;
            for (int i = 0; i < daoArray.size(); i++) {
                key = daoArray.keyAt(i);

                SimpleDao simpleDao = applicationContext.getDao(key);

                if (simpleDao != null) {
                    simpleDao.onCreate(db);
                }
            }
        }

        /**
         * 在数据库的 onCreate 被调用的时候，遍历 dao map 调用 IDAO 中的 onCreate 方法
         *
         * @param db 数据操作对象
         * @see com.android.javier.simplemvc.interfaces.IDao#onUpgrade(SQLiteDatabase)
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            int key;
            for (int i = 0; i < daoArray.size(); i++) {
                key = daoArray.keyAt(i);

                SimpleDao simpleDao = applicationContext.getDao(key);

                if (simpleDao != null) {
                    simpleDao.onUpgrade(db);
                }
            }
        }
    }

    /**
     * 构造方法，在构造方法中获取 application 对象
     */
    protected SimpleDatabase() {
        applicationContext = ApplicationContext.getApplicationContext();
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
     * @param context          应用程序上下文
     * @param dataSourceEntity 描述dao的实体
     */
    public void initDatabase(Context context, DataSourceEntity dataSourceEntity) {
        this.daoArray = dataSourceEntity.getDaoArray();

        if (simpleDatabaseHelper == null) {
            simpleDatabaseHelper = new SimpleDatabaseHelper(context, dataSourceEntity.getDbName(), null, dataSourceEntity.getVersion());
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