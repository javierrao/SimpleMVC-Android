package com.javier.simplemvc.interfaces;

/**
 * author:Javier
 * time:2016/5/1.
 * mail:38244704@qq.com
 */
public interface IAppWidget extends IModule {
    void initView();
    void initCommand();
    void addEventListener();
    void onInitComplete();
    void removeCommand();
}