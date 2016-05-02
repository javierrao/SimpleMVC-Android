package com.javier.simplemvc.modules.task;

import android.os.AsyncTask;
import android.os.Message;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.core.Task;
import com.javier.simplemvc.interfaces.IEncrypt;
import com.javier.simplemvc.interfaces.ITaskCallback;
import com.javier.simplemvc.modules.notify.NotifyMessage;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
@SuppressWarnings("unused")
public abstract class SimpleTask extends AsyncTask {

    protected int taskId;
    protected IEncrypt encrypt;

    protected SimpleContext simpleContext;

    public SimpleTask() {
        simpleContext = SimpleContext.getInstance();
    }

    protected void sendNotifyMessage(NotifyMessage message) {
        simpleContext.notifyObservers(message);
    }

    protected void sendNotifyMessage(int what, Object... param) {
        simpleContext.notifyObservers(new NotifyMessage(what, param));
    }

    protected void sendNotifyMessage(int what, Object param) {
        SimpleContext.getInstance().notifyObservers(new NotifyMessage(what, param));
    }

    protected void release() {
        Task.getInstance().removeTask(taskId);
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