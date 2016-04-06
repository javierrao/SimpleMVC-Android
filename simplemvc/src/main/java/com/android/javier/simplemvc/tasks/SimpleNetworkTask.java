package com.android.javier.simplemvc.tasks;

import com.android.javier.simplemvc.entity.TaskEntity;
import com.android.javier.simplemvc.net.ErrorEntity;
import com.android.javier.simplemvc.net.HttpSvr;
import com.android.javier.simplemvc.net.RequestEntity;
import com.android.javier.simplemvc.net.ResponseEntity;
import com.android.javier.simplemvc.util.Logger;

import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by javier on 2016/3/25.
 * <p>
 * 异步执行网络请求操作的父类
 */
public abstract class SimpleNetworkTask<T> extends SimpleTask {

    private HttpSvr httpSvr;

    public SimpleNetworkTask(TaskEntity entity) {
        super(entity);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        RequestEntity requestEntity = (RequestEntity) params[0];

        String param = requestEntity.getContent();

        if (requestEntity.getEncrypt() == 1) {
            param = getEncrypt().encrypt(param);
        }

        httpSvr = new HttpSvr();

        if (requestEntity.getTokenId() != null && !requestEntity.getTokenId().equals("")) {
            httpSvr.setAccessToken(requestEntity.getTokenId());
        }

        ResponseEntity responseEntity = null;

        if (requestEntity.getProtocol().equalsIgnoreCase("http")) {
            if (requestEntity.getMethod().equalsIgnoreCase("post")) {
                responseEntity = httpSvr.httpPost(requestEntity.getRequestUrl(), param, false);
            } else if (requestEntity.getMethod().equalsIgnoreCase("get")) {
                responseEntity = httpSvr.httpGet(requestEntity.getRequestUrl(), param, false);
            }
        } else {
            if (requestEntity.getMethod().equalsIgnoreCase("post")) {
                responseEntity = httpSvr.httpPost(requestEntity.getRequestUrl(), param, true);
            } else if (requestEntity.getMethod().equalsIgnoreCase("get")) {
                responseEntity = httpSvr.httpGet(requestEntity.getRequestUrl(), param, true);
            }
        }

        responseEntity.setTag(requestEntity);

        return responseEntity;
    }

    @Override
    protected void onPostExecute(Object o) {
        ResponseEntity responseEntity = (ResponseEntity) o;
        RequestEntity requestEntity = (RequestEntity) responseEntity.getTag();

        String resultContent = responseEntity.getContent();

        if (callback == null) {
            return;
        }

        try {
            if (responseEntity.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (requestEntity.getEncrypt() == 1) {
                    resultContent = encrypt.decrypt(resultContent);
                    responseEntity.setContent(resultContent);
                }

                if (getMetaData().equalsIgnoreCase("json")) {
                    responseEntity.setJsonContent(new JSONObject(resultContent));
                }

                T t = onResponse(responseEntity);

                callback.onResult(responseEntity.getResponseCode(), t, this);
            } else {
                if (requestEntity.getEncrypt() == 1) {
                    resultContent = encrypt.decrypt(resultContent);
                    responseEntity.setContent(resultContent);
                }

                if (getMetaData().equalsIgnoreCase("json")) {
                    responseEntity.setJsonContent(new JSONObject(resultContent));
                }

                responseEntity.setContent(resultContent);
                ErrorEntity error = onResponseError(responseEntity);

                callback.onFailed(responseEntity.getResponseCode(), error, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (callback != null) {
                ErrorEntity errorEntity = new ErrorEntity();
                errorEntity.setCode(500);
                errorEntity.setMessage("APP 内部错误");
                callback.onFailed(errorEntity.getCode(), errorEntity, this);
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        if (httpSvr != null) {
            httpSvr.destroy();
        }
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);

        if (httpSvr != null) {
            httpSvr.destroy();
        }
    }

    protected abstract T onResponse(ResponseEntity responseEntity) throws Exception;

    protected abstract ErrorEntity onResponseError(ResponseEntity responseEntity);
}