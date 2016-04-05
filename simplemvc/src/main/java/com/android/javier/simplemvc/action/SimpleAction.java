package com.android.javier.simplemvc.action;

import android.content.Context;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.interfaces.IApplicationWidget;
import com.android.javier.simplemvc.interfaces.IAction;
import com.android.javier.simplemvc.interfaces.IDao;
import com.android.javier.simplemvc.interfaces.INotify;
import com.android.javier.simplemvc.net.ErrorEntity;
import com.android.javier.simplemvc.tasks.SimpleDatabaseTask;
import com.android.javier.simplemvc.tasks.SimpleNetworkTask;
import com.android.javier.simplemvc.tasks.SimpleTask;
import com.android.javier.simplemvc.interfaces.ISimpleTaskCallback;
import com.android.javier.simplemvc.tasks.TaskManager;

/**
 * Created by javier on 2016/3/26.
 */
public abstract class SimpleAction<T> implements IAction, ISimpleTaskCallback<T> {
    protected INotify notify;
    protected IApplicationWidget applicationWidget;
    protected Context context;
    protected ApplicationContext applicationContext;
    protected TaskManager taskManager;

    protected SimpleAction(Context context) {
        this.context = context;

        applicationContext = ApplicationContext.getApplicationContext(context);
        taskManager = TaskManager.getInstance();
    }

    @Override
    public void doAction(INotify notify) {
        this.notify = notify;
    }

    public void setApplicationWidget(IApplicationWidget applicationWidget) {
        this.applicationWidget = applicationWidget;
    }

    protected void notifyApplicationWidget(INotify notify) {
        if (applicationWidget != null) {
            applicationWidget.handleNotify(notify, this);
        }
    }

    protected SimpleTask getTaskById(int taskId) {
        SimpleTask task = applicationContext.getTask(taskId);
        task.setCallback(this);
        return task;
    }

    protected SimpleNetworkTask getAsyncHttpTask(int taskId) {
        try {
            SimpleNetworkTask task = (SimpleNetworkTask) getTaskById(taskId);
            return task;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected SimpleDatabaseTask getAsyncDatabaseTask(int taskId) {
        try {
            SimpleDatabaseTask task = (SimpleDatabaseTask) getTaskById(taskId);
            return task;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected IDao getDao(int daoId) {
        return applicationContext.getDao(daoId);
    }

    @Override
    public void onFailed(int code, ErrorEntity message, SimpleTask target) {
        taskManager.removeTask(target);
    }

    @Override
    public void onResult(int code, T result, SimpleTask target) {
        taskManager.removeTask(target);
    }
}