package com.android.javier.simplemvc.tasks;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.entity.TaskEntity;
import com.android.javier.simplemvc.interfaces.IDao;

/**
 * Created by javier on 2016/4/4.
 *
 * 1、根据对象创建表
 * 2、根据返回的cursor对象创建对象
 */
public abstract class SimpleDatabaseTask<T> extends SimpleTask {
    public SimpleDatabaseTask(TaskEntity entity) {
        super(entity);
    }

    @Override
    protected T doInBackground(Object[] params) {
        return doDatabaseOptions(params);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

        if (callback != null) {
            callback.onResult(200, result, this);
        }
    }

    protected IDao getDao(int daoId) {
        return ApplicationContext.getApplicationContext().getDao(daoId);
    }

    protected abstract T doDatabaseOptions(Object[] params);
}
