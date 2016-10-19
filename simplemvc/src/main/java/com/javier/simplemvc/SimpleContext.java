package com.javier.simplemvc;

import android.content.Context;
import android.util.SparseArray;

import com.javier.simplemvc.core.CommandManager;
import com.javier.simplemvc.core.DaoManager;
import com.javier.simplemvc.core.ObserverManager;
import com.javier.simplemvc.core.TaskManager;
import com.javier.simplemvc.core.ViewManager;
import com.javier.simplemvc.data.database.SimpleDatabase;
import com.javier.simplemvc.interfaces.IDao;
import com.javier.simplemvc.interfaces.IView;
import com.javier.simplemvc.patterns.entity.CommandEntity;
import com.javier.simplemvc.patterns.entity.DaoEntity;
import com.javier.simplemvc.patterns.notify.NotifyMessage;
import com.javier.simplemvc.patterns.observer.Observer;

import java.util.ArrayList;

/**
 * author:Javier
 * time:2016/5/28.
 * mail:38244704@qq.com
 */
public final class SimpleContext {
    private static SimpleContext simpleContext;

    private CommandManager commandManager;
    private TaskManager taskManager;
    private ViewManager viewManager;
    private ObserverManager observerManager;
    private DaoManager daoManager;

    private Context context;

    public static SimpleContext getSimpleContext(Context context) {
        if (simpleContext == null) {
            simpleContext = new SimpleContext(context);
        }

        return simpleContext;
    }

    public static SimpleContext getSimpleContext() {
        return simpleContext;
    }

    public SimpleContext(Context context) {
        this.context = context;
        commandManager = CommandManager.getInstance(context);
        taskManager = TaskManager.getInstance();
        viewManager = ViewManager.getInstance();
        observerManager = ObserverManager.getInstance();
        daoManager = DaoManager.getInstance();
    }

    public void initDatabase(String dbName, int version, ArrayList<DaoEntity> daoEntities) {
        SimpleDatabase.getSimpleDatabase().initDatabase(context, dbName, version, daoEntities);
        registerDao(daoEntities);
    }

    public void registerCommand(ArrayList<CommandEntity> commandClasses) {
        commandManager.registerCommand(commandClasses);
    }

    public void registerTask(SparseArray<Class> tasks) {
        for (int i=0; i<tasks.size(); i++) {
            int key = tasks.keyAt(i);
            Class value = tasks.get(key);

            taskManager.registerTask(key, value, null);
        }
    }

    public void registerView(IView view) {
        viewManager.registerView(view);
    }

    public void registerObserver(String message, Observer observer) {
        observerManager.registerObserver(message, observer);
    }

    public void destroy() {
        commandManager.destroy();
        viewManager.destroy();
        daoManager.destroy();
        observerManager.destroy();
    }

    public void registerDao(ArrayList<DaoEntity> daoArray) {
        daoManager.registerDao(daoArray);
    }

    public IDao getDao(int id) {
        return daoManager.getDao(context, id);
    }

    public void notifyObserver(NotifyMessage message) {
        observerManager.notifyObserver(message);
    }

    public void removeObserver(String message) {
        observerManager.removeObserver(message);
    }

    public void removeView(IView view) {
        viewManager.removeView(view);
    }
}