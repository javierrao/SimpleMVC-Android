package com.android.javier.simplemvc.tasks;

import com.android.javier.simplemvc.entity.TaskEntity;

/**
 * author:Javier
 * time:2016/04/09
 * mail:38244704@qq.com
 */
public abstract class SimpleCustomTask<T> extends SimpleTask {

    public SimpleCustomTask(TaskEntity entity) {
        super(entity);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return doCustomOptions(params);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

        if (callback != null) {
            callback.onTaskResult(result, this);
        }
    }

    protected abstract T doCustomOptions(Object[] params);
}