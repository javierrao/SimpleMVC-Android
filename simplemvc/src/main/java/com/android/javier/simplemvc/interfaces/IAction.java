package com.android.javier.simplemvc.interfaces;

import com.android.javier.simplemvc.app.IApplicationWidget;

/**
 * Created by javie on 2016/3/26.
 */
public interface IAction {
    void doAction(INotify notify);
    void setApplicationWidget(IApplicationWidget applicationWidget);
}
