package com.android.javier.simplemvc.interfaces;

/**
 * Created by javier on 2016/3/26.
 * <p>
 * 调用action的统一接口
 */
public interface IAction {
    /**
     * 调用action， 并且传递INotify的对象
     *
     * @param notify INotify对象
     */
    void doAction(INotify notify);

    /**
     * 在action中设置IApplicationWidget的对象，用于action和ApplicationWidget的交互
     *
     * @param applicationWidget IApplicationWidget 对象
     * @see com.android.javier.simplemvc.interfaces.IApplicationWidget
     */
    void setApplicationWidget(IApplicationWidget applicationWidget);
}