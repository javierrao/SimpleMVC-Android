package com.javier.simplemvc;

import android.content.Context;
import android.util.SparseArray;

import com.javier.simplemvc.modules.notify.Observer;
import com.javier.simplemvc.dao.SimpleDatabase;
import com.javier.simplemvc.interfaces.IDao;
import com.javier.simplemvc.modules.notify.NotifyMessage;
import com.javier.simplemvc.utils.Logger;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
@SuppressWarnings("unchecked,unused")
public final class SimpleContext {

    private static final Logger logger = Logger.getLogger();

    private static SimpleContext simpleContext;

    private Context context;

    private SparseArray<ArrayList<Observer>> observerSparseArray = new SparseArray<>();
    private SparseArray<Class> daoClassArray = new SparseArray();

    /**
     * 单例模式获取SimpleContext对象
     *
     * @return SimpleContext 对象
     */
    public synchronized static SimpleContext getInstance() {
        if (simpleContext == null) {
            simpleContext = new SimpleContext();
        }

        return simpleContext;
    }

    /**
     * 初始化数据库
     *
     * @param context    上下文
     * @param dbName     数据库文件名
     * @param version    数据库版本
     * @param daoClasses 注册dao的class
     */
    public void initDatabase(Context context, String dbName, int version, SparseArray<Class> daoClasses) {
        this.daoClassArray = daoClasses;
        this.context = context;

        SimpleDatabase.getSimpleDatabase().initDatabase(context, dbName, version, daoClasses);
    }

    /**
     * 获取DAO
     *
     * @param daoId dao id
     * @return 获取dao对象
     */
    public IDao getDao(int daoId) {
        if (daoClassArray == null) {
            return null;
        }

        Class daoClass = daoClassArray.get(daoId);

        if (null == daoClass) {
            logger.e("can not find dao class by id " + daoId);
            return null;
        }

        try {
            Constructor<IDao>[] cons = daoClass.getConstructors();
            return cons[0].newInstance(context, SimpleDatabase.getSimpleDatabase().open());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 注册观察者
     *
     * @param what     message what
     * @param observer 观察者
     */
    public void registerObserver(int what, Observer observer) {
        if (observerSparseArray.get(what) == null) {
            observerSparseArray.put(what, new ArrayList<Observer>());
        }

        ArrayList<Observer> observers = observerSparseArray.get(what);
        observers.add(observer);
    }

    /**
     * 移除观察者
     *
     * @param what     消息ID
     * @param observer 观察者
     */
    public void removeObserver(int what, Observer observer) {
        ArrayList<Observer> observers = observerSparseArray.get(what);

        for (int i = 0; i < observers.size(); i++) {
            if (observers.get(i) == observer) {
                observers.remove(i);
                break;
            }
        }

        if (observers.size() == 0) {
            observerSparseArray.remove(what);
        }
    }

    /**
     * 通过观察者发送通知消息
     *
     * @param message 通知消息
     */
    public void notifyObservers(NotifyMessage message) {

        ArrayList<Observer> observers = observerSparseArray.get(message.getWhat());

        if (observers == null) {
            logger.e("message for " + message.getWhat() + " may not registered!");
            return;
        }

        for (int i = 0; i < observers.size(); i++) {
            Observer observer = observers.get(i);
            observer.notifyObserver(message);
        }
    }
}
