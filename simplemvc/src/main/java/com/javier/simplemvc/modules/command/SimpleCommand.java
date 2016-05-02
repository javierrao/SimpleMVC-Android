package com.javier.simplemvc.modules.command;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.core.Task;
import com.javier.simplemvc.core.View;
import com.javier.simplemvc.interfaces.ICommand;
import com.javier.simplemvc.interfaces.IDao;
import com.javier.simplemvc.interfaces.IDisplay;
import com.javier.simplemvc.interfaces.IMediator;
import com.javier.simplemvc.modules.task.SimpleTask;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 * <p/>
 * command的父类，实现了ICommand接口，并封装了command类所用到的公用方法
 */
public abstract class SimpleCommand implements ICommand {
    /**
     * 显示调用接口
     */
    protected IDisplay display;

    /**
     * 任务task
     */
    protected Task task;

    protected View view;

    protected SimpleContext simpleContext;

    /**
     * 构造函数，通过传入显示接口初始化command对象
     *
     * @param display 显示接口对象
     */
    protected SimpleCommand(IDisplay display) {
        this.display = display;
        this.task = Task.getInstance();
        this.simpleContext = SimpleContext.getInstance();
        this.view = View.getInstance();

        initTask();
        initMediator();
    }

    /**
     * 通过调用task中的注册方法来注册task
     *
     * @param taskId taskId
     * @param task   task class 对象
     */
    protected void registerTask(int taskId, Class<?> task) {
        this.task.registerTask(taskId, task);
    }

    /**
     * 获取注册的task
     *
     * @param taskId task id
     * @return SimpleTask对象
     */
    protected SimpleTask retrieveTask(int taskId) {
        return this.task.retrieveTask(taskId);
    }

    /**
     * 注册mediator对象
     *
     * @param mediator 需要注册的mediator对象
     */
    protected void registerMediator(IMediator mediator) {
        view.registerMediator(mediator);
    }

    /**
     * 注销mediator
     *
     * @param mediatorId 需要注销的mediator对象ID
     */
    protected void removeMediator(int mediatorId) {
        view.removeMediator(mediatorId);
    }

    /**
     * 移除task
     *
     * @param taskId 需要移除的task id
     */
    protected void removeTask(int taskId) {
        task.removeTask(taskId);
    }

    /**
     * 移除所有task
     */
    protected void removeAllTask() {
        task.removeAllTask();
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

    /**
     * 初始化task, 初始化在command需要用到的task，在子类中实现
     */
    protected abstract void initTask();

    /**
     * 初始化mediator，初始化在command需要用到的mediator,在子类中实现
     */
    protected abstract void initMediator();
}