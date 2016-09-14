package com.javier.simplemvc.patterns.entity;

/**
 * author:Javier
 * time:2016/5/29.
 * mail:38244704@qq.com
 * desc:描述command的对象
 */
public class CommandEntity {
    /**
     * command class， command class 必须实现ICommand接口
     */
    private Class commandClass;
    /**
     * command对象是否需要保持引用，用于在某些情况下，需要多次去调用同一个对象的方法
     * 默认不需要保持
     */
    private boolean isHolder = false;

    public CommandEntity() {

    }

    public CommandEntity(Class commandClass, boolean isHolder) {
        this.commandClass = commandClass;
        this.isHolder = isHolder;
    }

    public CommandEntity(Class commandClass) {
        this.commandClass = commandClass;
    }

    public Class getCommandClass() {
        return commandClass;
    }

    public void setCommandClass(Class commandClass) {
        this.commandClass = commandClass;
    }

    public boolean isHolder() {
        return isHolder;
    }

    public void setHolder(boolean holder) {
        isHolder = holder;
    }
}
