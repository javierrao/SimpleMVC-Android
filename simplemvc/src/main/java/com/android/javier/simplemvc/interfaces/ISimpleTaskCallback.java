package com.android.javier.simplemvc.interfaces;

import com.android.javier.simplemvc.net.ErrorEntity;
import com.android.javier.simplemvc.tasks.SimpleTask;

/**
 * Created by javier on 2016/3/26.
 * <p>
 * task的统一回调接口，当task执行完成以后调用
 */
public interface ISimpleTaskCallback<T> {
    /**
     * 正确的时候调用
     *
     * @param code   标示
     * @param result 返回内容
     * @param target 调用者
     */
    void onResult(int code, T result, SimpleTask target);

    /**
     * 正确的时候调用
     *
     * @param code   标示
     * @param error  描述错误的实体对象
     * @param target 调用者
     */
    void onFailed(int code, ErrorEntity error, SimpleTask target);

    /**
     * 异步执行数据库操作后返回
     * @param result    返回的结果
     * @param target    调用者
     */
    void onDatabaseResult(T result, SimpleTask target);
}
