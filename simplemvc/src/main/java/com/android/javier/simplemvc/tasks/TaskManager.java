package com.android.javier.simplemvc.tasks;

import android.os.AsyncTask;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.interfaces.IEncrypt;
import com.android.javier.simplemvc.util.Logger;
import com.android.javier.simplemvc.net.RequestEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by javier on 2016/3/27.
 */
public final class TaskManager {
    private static Logger logger = Logger.getLogger();

    private static TaskManager manager;

    private HashMap<Integer, SimpleTask> executingMap = new HashMap<Integer, SimpleTask>();

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

    public void executeAsyncDatabseTask(SimpleDatabaseTask task, Object... param) {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
        executingMap.put(task.getTid(), task);
    }

    /**
     * 停止task
     *
     * @param task 需要停止的task
     */
    public void removeTask(SimpleTask task) {
        SimpleTask abstractAsyncTask = executingMap.get(task.getTid());
        abstractAsyncTask.cancel(true);

        executingMap.remove(task.getTid());
    }

    /**
     * 停止所有的task
     */
    public void removeAllTask() {
        Iterator iter = executingMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            SimpleTask task = (SimpleTask) entry.getValue();
            task.cancel(true);
        }

        executingMap.clear();
    }

    public void destroy() {
        removeAllTask();
        manager = null;
    }

    private void executeTask(SimpleNetworkTask task, String url, String method, String protocol,
                             boolean encrypt, String param, String tokenId) {
        int encoder_int = 0;

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

        executingMap.put(task.getTid(), task);
    }
}