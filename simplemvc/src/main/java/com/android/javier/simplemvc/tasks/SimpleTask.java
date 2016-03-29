package com.android.javier.simplemvc.tasks;

import android.os.AsyncTask;

import com.android.javier.simplemvc.entity.TaskEntity;
import com.android.javier.simplemvc.interfaces.IAsyncTaskCallback;
import com.android.javier.simplemvc.interfaces.IEncrypt;

/**
 * Created by javier on 2016/3/25.
 */
public abstract class SimpleTask extends AsyncTask {
    protected IAsyncTaskCallback callback;
    protected IEncrypt encrypt;

    private TaskEntity taskEntity;

    protected SimpleTask(TaskEntity entity) {
        this.taskEntity = entity;
    }

    public String getTid() {
        return taskEntity.getId();
    }

    public int getTaskResId() {
        return taskEntity.getResId();
    }

    public String getMetaData() {
        return taskEntity.getMetaData();
    }

    public String getType() {
        return taskEntity.getType();
    }

    public IAsyncTaskCallback getCallback() {
        return callback;
    }

    public void setCallback(IAsyncTaskCallback callback) {
        this.callback = callback;
    }

    public IEncrypt getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(IEncrypt encrypt) {
        this.encrypt = encrypt;
    }
}