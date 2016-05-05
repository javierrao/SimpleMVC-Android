package com.javier.simplemvc.modules.task;

import android.os.AsyncTask;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.core.TaskManager;
import com.javier.simplemvc.interfaces.IEncrypt;
import com.javier.simplemvc.interfaces.ITaskCallback;
import com.javier.simplemvc.modules.notify.NotifyMessage;
import com.javier.simplemvc.utils.Logger;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
@SuppressWarnings("unused")
public abstract class SimpleTask extends AsyncTask {

    protected int taskId;
    protected IEncrypt encrypt;
    protected ITaskCallback callback;

    protected Logger logger = Logger.getLogger();

    protected SimpleTask(ITaskCallback callback) {
        this.callback = callback;
    }

    protected SimpleTask(ITaskCallback callback, IEncrypt encrypt) {
        this.callback = callback;
        this.encrypt = encrypt;
    }

    protected void release() {
        TaskManager.getInstance().removeTask(taskId);
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public IEncrypt getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(IEncrypt encrypt) {
        this.encrypt = encrypt;
    }
}