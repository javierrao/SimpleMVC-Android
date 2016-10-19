package com.javier.simplemvc.patterns.command;

import android.content.Context;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.core.NotifyManager;
import com.javier.simplemvc.core.TaskManager;
import com.javier.simplemvc.interfaces.ICommand;
import com.javier.simplemvc.interfaces.IDao;
import com.javier.simplemvc.interfaces.IEncrypt;
import com.javier.simplemvc.interfaces.ITaskCallback;
import com.javier.simplemvc.patterns.model.SimpleTask;
import com.javier.simplemvc.util.Logger;

/**
 * author:Javier
 * time:2016/5/28.
 * mail:38244704@qq.com
 */
@SuppressWarnings("unused")
public abstract class SimpleCommand implements ICommand {

    protected Logger logger = Logger.getLogger();
    protected SimpleContext simpleContext = SimpleContext.getSimpleContext();
    protected TaskManager taskManager = TaskManager.getInstance();
    protected NotifyManager notifyManager = NotifyManager.getInstance();
    protected Context mContext;

    protected SimpleCommand(Context context) {
        this.mContext = context;
    }

    protected SimpleTask bindTask(int taskId, ITaskCallback callback, IEncrypt encrypt) {
        return taskManager.bindTask(taskId, callback, encrypt);
    }

    protected SimpleTask bindTask(int taskId, ITaskCallback callback) {
        return taskManager.bindTask(taskId, callback, null);
    }

    protected void unBindTask(int taskId) {
        taskManager.unBindTask(taskId);
    }

    /**
     * 获取注册的task
     *
     * @param taskId task id
     * @return SimpleTask对象
     */
    protected SimpleTask retrieveTask(int taskId) {
        return taskManager.retrieveTask(taskId);
    }

    /**
     * 移除task
     *
     * @param taskId 需要移除的task id
     */
    protected void removeTask(int taskId) {
        taskManager.removeTask(taskId);
    }

    /**
     * 移除所有task
     */
    protected void removeAllTask() {
        taskManager.removeAllTask();
    }

    protected IDao getDao(int daoId) {
        return simpleContext.getDao(daoId);
    }


    @Override
    public void onRegister() {

    }

    @Override
    public void onRemove() {

    }
}