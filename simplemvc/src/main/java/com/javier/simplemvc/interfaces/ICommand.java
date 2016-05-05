package com.javier.simplemvc.interfaces;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
public interface ICommand extends IModule {
    /**
     * 初始化task, 初始化在command需要用到的task，在子类中实现
     */
    void initTask();
}