package com.android.javier.simplemvc.interfaces;

/**
 * Created by javie on 2016/3/26.
 */
public interface IAction {
    void doAction(INotify notify);
    void setApplicationWidget(IApplicationWidget applicationWidget);
}
