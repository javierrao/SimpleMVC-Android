package com.android.javier.demo;


import android.util.SparseArray;

import com.android.javier.demo.command.LoginCommand;
import com.android.javier.demo.dao.UserDao;
import com.android.javier.demo.tasks.LoginTask;
import com.android.javier.demo.tasks.UserTask;
import com.javier.simplemvc.SimpleApplication;
import com.javier.simplemvc.interfaces.ITask;
import com.javier.simplemvc.patterns.entity.CommandEntity;
import com.javier.simplemvc.patterns.entity.DaoEntity;
import com.javier.simplemvc.patterns.model.SimpleTask;
import com.javier.simplemvc.util.Logger;

import java.util.ArrayList;

/**
 * Created by javier
 */
public class BaseApplication extends SimpleApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        startUpSimpleMVC();
        initLogger();
        initDatabase("simplemvc.db", 1);
    }

    @Override
    protected ArrayList<DaoEntity> listDao() {
        ArrayList<DaoEntity> daoEntities = new ArrayList<>();
        daoEntities.add(new DaoEntity(1, UserDao.class));

        return daoEntities;
    }

    @Override
    protected ArrayList<CommandEntity> listCommand() {
        ArrayList<CommandEntity> commandEntities = new ArrayList<>();
        commandEntities.add(new CommandEntity(LoginCommand.class));
        return commandEntities;
    }

    protected void initLogger() {
        Logger.initLogger("simplemvc", "SimpleMVC-Android", "SimpleMVC-Android.log", true, false);
    }

    @Override
    protected SparseArray<Class> listTask() {
        SparseArray<Class> tasks = new SparseArray<>();
        tasks.put(1, LoginTask.class);
        return tasks;
    }
}