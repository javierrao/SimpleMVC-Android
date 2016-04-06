package com.android.javier.simplemvc.interfaces;

/**
 * Created by javier on 2016/3/27.
 * <p>
 * 消息接口，提供统一访问Notify对象的方法
 */
public interface INotify {
    /**
     * 获取notify名称
     *
     * @return notify名称
     */
    String getName();

    /**
     * 获取 notify 内容
     *
     * @return notify 内容
     */
    Object getBody();

    /**
     * 获取 notify id
     *
     * @return notify id
     */
    int getId();
}
