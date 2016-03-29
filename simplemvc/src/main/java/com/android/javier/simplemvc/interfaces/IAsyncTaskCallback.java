package com.android.javier.simplemvc.interfaces;

import com.android.javier.simplemvc.net.ErrorEntity;
import com.android.javier.simplemvc.tasks.SimpleTask;

/**
 * Created by javie on 2016/3/26.
 */
public interface IAsyncTaskCallback<T> {
    void onResult(int code, T result, SimpleTask target);
    void onFailed(int code, ErrorEntity error, SimpleTask target);
}
