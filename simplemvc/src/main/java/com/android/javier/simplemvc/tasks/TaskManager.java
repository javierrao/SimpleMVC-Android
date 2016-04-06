package com.android.javier.simplemvc.tasks;

import android.os.AsyncTask;
import android.util.SparseArray;

import com.android.javier.simplemvc.interfaces.IEncrypt;
import com.android.javier.simplemvc.util.Logger;
import com.android.javier.simplemvc.net.RequestEntity;

/**
 * Created by javier on 2016/3/27.
 * <p>
 * 管理task的类，所有task的执行都在该类中进行，并且管理task的整个生命周期，直至被销毁
 * 需要在确保task无用以后销毁或在出现异常中断执行的时候需要清空资源。
 * <p>
 * 例如：在一个网络请求的任务执行过程中，当task被强制终止以后，需要确保http资源被释放
 */
@SuppressWarnings("unused")
public final class TaskManager {
    private static Logger logger = Logger.getLogger();

    private static TaskManager manager;

    private SparseArray<SimpleTask> executingTaskArray = new SparseArray<>();

    public static TaskManager getInstance() {
        if (manager == null) {
            manager = new TaskManager();
        }

        return manager;
    }

    /**
     * 执行POST请求
     *
     * @param task  需要执行的任务
     * @param url   请求的url
     * @param param 请求参数
     */
    public void executeTaskHttpPost(SimpleNetworkTask task, String url, String param, String tokenId) {
        if (url.startsWith("https") || url.startsWith("HTTPS")) {
            executeTask(task, url, "POST", "https", false, param, tokenId);
        } else {
            executeTask(task, url, "POST", "http", false, param, tokenId);
        }
    }

    /**
     * 执行加密POST请求
     *
     * @param task  需要执行的任务
     * @param url   请求的url
     * @param param 请求参数
     */
    public void executeTaskHttpPostEncrypt(SimpleNetworkTask task, String url, String param, IEncrypt encrypt, String tokenId) {
        task.setEncrypt(encrypt);

        if (url.startsWith("https") || url.startsWith("HTTPS")) {
            executeTask(task, url, "POST", "https", true, param, tokenId);
        } else {
            executeTask(task, url, "POST", "http", true, param, tokenId);
        }
    }

    /**
     * 执行get请求
     *
     * @param task  需要执行的任务
     * @param url   请求的url
     * @param param 请求参数
     */
    public void executeTaskHttpGet(SimpleNetworkTask task, String url, String param, String tokenId) {
        if (url.startsWith("https") || url.startsWith("HTTPS")) {
            executeTask(task, url, "GET", "https", false, param, tokenId);
        } else {
            executeTask(task, url, "GET", "http", false, param, tokenId);
        }
    }

    /**
     * 执行加密get请求
     *
     * @param task  需要执行的任务
     * @param url   请求的url
     * @param param 请求参数
     */
    public void executeTaskHttpGetEncrypt(SimpleNetworkTask task, String url, String param, IEncrypt encrypt, String tokenId) {
        task.setEncrypt(encrypt);

        if (url.startsWith("https") || url.startsWith("HTTPS")) {
            executeTask(task, url, "GET", "https", true, param, tokenId);
        } else {
            executeTask(task, url, "GET", "http", true, param, tokenId);
        }
    }

    /**
     * 异步执行数据库操作的任务
     *
     * @param task  执行异步数据库操作的任务
     * @param param 参数
     */
    public void executeAsyncDatabaseTask(SimpleDatabaseTask task, Object... param) {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
        executingTaskArray.put(task.getTid(), task);
    }

    /**
     * 停止task
     *
     * @param task 需要停止的task
     */
    public void removeTask(SimpleTask task) {
        SimpleTask abstractAsyncTask = executingTaskArray.get(task.getTid());

        if (abstractAsyncTask != null) {
            abstractAsyncTask.cancel(true);
            executingTaskArray.remove(task.getTid());
        }
    }

    /**
     * 停止所有的task
     */
    public void removeAllTask() {
        int key;
        for (int i = 0; i < executingTaskArray.size(); i++) {
            key = executingTaskArray.keyAt(i);

            SimpleTask task = executingTaskArray.get(key);
            task.cancel(true);
        }

        executingTaskArray.clear();
    }

    /**
     * 关闭所有的task，并清空资源
     */
    public void destroy() {
        removeAllTask();
        manager = null;
    }

    /**
     * 执行异步网络请求的任务
     *
     * @param task     需要执行的任务
     * @param url      请求的url
     * @param method   请求方式 get / post
     * @param protocol 请求协议 http / https
     * @param encrypt  是否加密
     * @param param    请求参数
     * @param tokenId  认证ID
     */
    private void executeTask(SimpleNetworkTask task, String url, String method, String protocol,
                             boolean encrypt, String param, String tokenId) {
        int encoder_int;

        if (encrypt) {
            encoder_int = 1;
        } else {
            encoder_int = 0;
        }

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setRequestUrl(url);
        requestEntity.setContent(param);
        requestEntity.setMethod(method);
        requestEntity.setProtocol(protocol);
        requestEntity.setEncrypt(encoder_int);
        requestEntity.setTokenId(tokenId);

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestEntity);

        executingTaskArray.put(task.getTid(), task);
    }
}