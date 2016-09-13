package com.android.javier.demo;


import android.util.SparseArray;

import com.android.javier.demo.dao.UserDao;
import com.javier.simplemvc.SimpleApplication;
import com.javier.simplemvc.patterns.entity.CommandEntity;
import com.javier.simplemvc.patterns.entity.DaoEntity;

import java.util.ArrayList;

/**
 * Created by javier
 */
public class BaseApplication extends SimpleApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initDatabase("simplemvc.db", 1);
        initLogger("simplemvc", "SimpleMVC-Android", "SimpleMVC-Android.log", true, false);
    }

    @Override
    public SparseArray<Class> getDaoClass() {
        SparseArray daoArray = new SparseArray();
        daoArray.put(R.id.ids_dao_user, UserDao.class);

        return daoArray;
    }

    @Override
    protected void initLogger() {

    }

    @Override
    protected ArrayList<DaoEntity> listDao() {
        return null;
    }

    @Override
    protected ArrayList<CommandEntity> listCommand() {
        return null;
    }
}