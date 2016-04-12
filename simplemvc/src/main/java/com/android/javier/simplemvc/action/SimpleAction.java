package com.android.javier.simplemvc.action;

import android.content.Context;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.interfaces.IAction;
import com.android.javier.simplemvc.interfaces.IApplicationWidget;
import com.android.javier.simplemvc.interfaces.IDao;
import com.android.javier.simplemvc.interfaces.INotify;
import com.android.javier.simplemvc.interfaces.ISimpleTaskCallback;
import com.android.javier.simplemvc.net.ErrorEntity;
import com.android.javier.simplemvc.tasks.SimpleDatabaseTask;
import com.android.javier.simplemvc.tasks.SimpleNetworkTask;
import com.android.javier.simplemvc.tasks.SimpleTask;
import com.android.javier.simplemvc.tasks.TaskManager;
import com.android.javier.simplemvc.util.Logger;

@SuppressWarnings("unused ")
public abstract class SimpleAction<T> implements IAction, ISimpleTaskCallback<T> {
    protected IApplicationWidget applicationWidget;
    protected Context context;
    protected ApplicationContext applicationContext;
    protected TaskManager taskManager;

    protected Logger logger = Logger.getLogger();

    protected SimpleAction(Context context) {
        this.context = context;

        applicationContext = ApplicationContext.getApplicationContext(context);
        taskManager = TaskManager.getInstance();
    }

    @Override
    public Object doActionSync(INotify notify) {
        return null;
    }

    @Override
    public void doActionAsync(INotify notify) {
    }

    public void setApplicationWidget(IApplicationWidget applicationWidget) {
        this.applicationWidget = applicationWidget;
    }

    protected void notifyApplicationWidget(INotify notify) {
        if (applicationWidget != null) {
            applicationWidget.handleNotify(notify, this);
        }
    }

    protected void notifyApplicationWidget(int notify) {
        if (applicationWidget != null) {
            applicationWidget.handleNotify(notify, this);
        }
    }

    protected SimpleTask getTaskById(int taskId) {
        SimpleTask task = applicationContext.getTask(taskId);
        if (task != null) {
            task.setCallback(this);
        }
        return task;
    }

    protected SimpleNetworkTask getAsyncHttpTask(int taskId) {
        try {
            return (SimpleNetworkTask) getTaskById(taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected SimpleDatabaseTask getAsyncDatabaseTask(int taskId) {
        try {
            return (SimpleDatabaseTask) getTaskById(taskId);
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