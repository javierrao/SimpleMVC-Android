package com.javier.simplemvc.modules.task;

import com.javier.simplemvc.dao.SimpleDatabase;
import com.javier.simplemvc.interfaces.IDao;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
public abstract class SimpleDatabaseTask<T> extends SimpleTask {

    public IDao getDao(int daoId) {
        return simpleContext.getDao(daoId);
    }

    @Override
    protected T doInBackground(Object[] params) {

        int opt = Integer.parseInt(params[0].toString());
        Object[] tmpParams = (Object[]) params[1];

        T t = null;

        switch (opt) {
            case SimpleDatabase.INSERT:
                t = insert(tmpParams);
                break;
            case SimpleDatabase.UPDATE:
                t = update(tmpParams);
                break;
            case SimpleDatabase.DELETE:
                t = delete(tmpParams);
                break;
            case SimpleDatabase.SELECT:
                t = query(tmpParams);
                break;
        }

        return t;
    }

    protected T insert(Object[] params) {
        return null;
    }

    protected T update(Object[] params) {
        return null;
    }

    protected T delete(Object[] params) {
        return null;
    }

    protected T query(Object[] params) {
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        onResult((T) o);
    }

    protected abstract void onResult(T t);
}