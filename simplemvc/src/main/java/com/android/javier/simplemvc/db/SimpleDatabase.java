package com.android.javier.simplemvc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.entity.DataSourceEntity;
import com.android.javier.simplemvc.interfaces.IDao;
import com.android.javier.simplemvc.tasks.SimpleTask;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by javier on 2016/4/5.
 */
public final class SimpleDatabase {

    private static SimpleDatabase simpleDatabase;

    private SimpleDatabaseHelper simpleDatabaseHelper;
    private SQLiteDatabase database = null;

    private HashMap<Integer, String> daoMap = new HashMap<Integer, String>();

    private ApplicationContext applicationContext = null;

    private class SimpleDatabaseHelper extends SQLiteOpenHelper {
        public SimpleDatabaseHelper(Context context, String name,
                                    CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Iterator iter = daoMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                int daoId = Integer.parseInt(entry.getKey().toString());

                IDao idao = applicationContext.getDao(daoId);
                idao.onCreate(db);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Iterator iter = daoMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                int daoId = Integer.parseInt(entry.getKey().toString());

                IDao idao = applicationContext.getDao(daoId);
                idao.onUpgrade(db);
            }
        }
    }

    protected SimpleDatabase() {
        applicationContext = ApplicationContext.getApplicationContext();
    }

    public static SimpleDatabase getSimpleDatabase() {
        if (simpleDatabase == null) {
            simpleDatabase = new SimpleDatabase();
        }

        return simpleDatabase;
    }

    public void initDatabase(Context context, DataSourceEntity dataSourceEntity) {
        this.daoMap = dataSourceEntity.getDaos();

        if (simpleDatabaseHelper == null) {
            simpleDatabaseHelper = new SimpleDatabaseHelper(context, dataSourceEntity.getDbName(), null, dataSourceEntity.getVersion());
            database = simpleDatabaseHelper.getWritableDatabase();
        }
    }

    public void destroy() {
        if (database != null) {
            database.close();
        }

        if (simpleDatabaseHelper == null) {
            simpleDatabaseHelper.close();
        }

        database = null;
        simpleDatabaseHelper = null;
        simpleDatabase = null;
    }
}