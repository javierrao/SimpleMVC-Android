package com.javier.simplemvc.modules.command;

import com.javier.simplemvc.core.TaskManager;
import com.javier.simplemvc.interfaces.ICommand;
import com.javier.simplemvc.interfaces.IDao;
import com.javier.simplemvc.interfaces.IEncrypt;
import com.javier.simplemvc.interfaces.ITaskCallback;
import com.javier.simplemvc.modules.SimpleModule;
import com.javier.simplemvc.modules.task.SimpleTask;
import com.javier.simplemvc.utils.Logger;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 * <p>
 * command的父类，实现了ICommand接口，并封装了command类所用到的公用方法
 */
@SuppressWarnings("unused")
public abstract class SimpleCommand extends SimpleModule implements ICommand {
    /**
     * 任务task
     */
    protected TaskManager taskManager;
    /**
     * 日志
     */
    protected Logger logger = Logger.getLogger();

    /**
     * 构造函数，通过传入显示接口初始化command对象
     */
    protected SimpleCommand() {
        super();
        taskManager = TaskManager.getInstance();

        initTask();
    }

    /**
     * 注册task,并且在数据发送和接收的时候需要通过调用加密接口进行加解密
     *
     * @param taskId    task id
     * @param taskClass task class
     * @param callback  回调接口对象
     * @param encrypt   加密接口对象
     */
    protected void registerTask(int taskId, Class<?> taskClass, ITaskCallback callback, IEncrypt encrypt) {
        this.taskManager.registerTask(taskId, taskClass, callback, encrypt);
    }

    /**
     * 注册task
     *
     * @param taskId    task id
     * @param taskClass task class对象
     * @param callback  回调接口对象
     */
    protected void registerTask(int taskId, Class<?> taskClass, ITaskCallback callback) {
        taskManager.registerTask(taskId, taskClass, callback);
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

    /**
     * 获取DAO对象
     *
     * @param daoId dao对象ID
     * @return
     */
    protected IDao getDao(int daoId) {
        return simpleContext.getDao(daoId);
    }

    @Override
    public void onRemove() {

    }

    @Override
    public void onRegister() {

    }
}