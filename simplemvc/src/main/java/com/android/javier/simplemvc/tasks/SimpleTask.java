package com.android.javier.simplemvc.tasks;

import android.os.AsyncTask;

import com.android.javier.simplemvc.entity.TaskEntity;
import com.android.javier.simplemvc.interfaces.ISimpleTaskCallback;
import com.android.javier.simplemvc.interfaces.IEncrypt;

/**
 * Created by javier on 2016/3/25.
 */
public abstract class SimpleTask extends AsyncTask {
    protected ISimpleTaskCallback callback;
    protected IEncrypt encrypt;

    private TaskEntity taskEntity;

    protected SimpleTask(TaskEntity entity) {
        this.taskEntity = entity;
    }

    public int getTid() {
        return taskEntity.getId();
    }

    public String getMetaData() {
        return taskEntity.getMetaData();
    }

    public String getType() {
        return taskEntity.getType();
    }

    public ISimpleTaskCallback getCallback() {
        return callback;
    }

    public void setCallback(ISimpleTaskCallback callback) {
        this.callback = callback;
    }

    public IEncrypt getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(IEncrypt encrypt) {
        this.encrypt = encrypt;
    }
}