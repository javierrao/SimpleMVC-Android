package com.javier.simplemvc.modules.task;

import com.javier.simplemvc.interfaces.IEncrypt;
import com.javier.simplemvc.interfaces.ITaskCallback;
import com.javier.simplemvc.net.ErrorEntity;
import com.javier.simplemvc.net.RequestEntity;
import com.javier.simplemvc.net.ResponseEntity;
import com.javier.simplemvc.net.SimpleHttp;

import java.net.HttpURLConnection;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
@SuppressWarnings("unchecked")
public abstract class SimpleNetTask<T> extends SimpleTask {

    protected SimpleHttp httpSvr;

    protected SimpleNetTask(ITaskCallback callback) {
        super(callback);
    }

    protected SimpleNetTask(ITaskCallback callback, IEncrypt encrypt) {
        super(callback, encrypt);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        RequestEntity requestEntity = (RequestEntity) params[0];

        String param = requestEntity.getContent();

        if (requestEntity.getEncrypt() == 1 && getEncrypt() != null) {
            param = getEncrypt().encrypt(param);
        } else {
            logger.e("this request must be encrypt but IEncrypt object is null. Please use IEncrypt object to register task");
            return null;
        }

        httpSvr = new SimpleHttp();

        if (requestEntity.getTokenId() != null && !requestEntity.getTokenId().equals("")) {
            httpSvr.setAccessToken(requestEntity.getTokenId());
        }

        ResponseEntity responseEntity = new ResponseEntity();

        boolean isHttps = false;
        if (requestEntity.getProtocol().equals("https")) {
            isHttps = true;
        }

        if (requestEntity.getMethod().equalsIgnoreCase("post")) {
            responseEntity = httpSvr.post(requestEntity.getRequestUrl(), param, isHttps, requestEntity.getHeaders());
        } else if (requestEntity.getMethod().equalsIgnoreCase("get")) {
            responseEntity = httpSvr.get(requestEntity.getRequestUrl(), param, isHttps, requestEntity.getHeaders());
        }

        responseEntity.setTag(requestEntity);

        return responseEntity;
    }

    @Override
    protected void onPostExecute(Object o) {
        if (o == null) {
            return;
        }

        ResponseEntity responseEntity = (ResponseEntity) o;
        RequestEntity requestEntity = (RequestEntity) responseEntity.getTag();

        String resultContent = responseEntity.getContent();

        try {
            if (responseEntity.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (requestEntity.getEncrypt() == 1) {
                    resultContent = encrypt.decrypt(resultContent);
                    responseEntity.setContent(resultContent);
                }

                if (null != callback) {
                    T t = onResponse(responseEntity);
                    callback.onResult(responseEntity.getResponseCode(), t, this);
                } else {
                    logger.e("task callback object is null.");
                }

            } else {
                if (requestEntity.getEncrypt() == 1) {
                    resultContent = encrypt.decrypt(resultContent);
                    responseEntity.setContent(resultContent);
                }

                responseEntity.setContent(resultContent);
                ErrorEntity error = onResponseError(responseEntity);

                if (null != callback) {
                    callback.onFailed(responseEntity.getResponseCode(), error, this);
                } else {
                    logger.e("task callback object is null.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            ErrorEntity errorEntity = new ErrorEntity();
            errorEntity.setCode(5000);
            errorEntity.setMessage("客户端应用内部错误");

            if (callback != null) {
                callback.onFailed(errorEntity.getCode(), errorEntity, this);
            } else {
                logger.e("onPostExecute exception, And callback is null.");
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

    /**
     * 当回调对象不为空，并且正确响应的时候调用，在子类中实现，返回的结果通过回调的方式返回给回调对象
     *
     * @param responseEntity 响应结果对象
     * @return 解析响应对象的结果
     * @throws Exception 解析异常
     */
    protected T onResponse(ResponseEntity responseEntity) throws Exception {
        return null;
    }

    /**
     * 当回调对象不空，请求结果错误的时候调用，在子类中实现，返回结果通过回调的方式返回给回调对象
     *
     * @param responseEntity 响应结果
     * @return 错误
     */
    protected ErrorEntity onResponseError(ResponseEntity responseEntity) {
        return null;
    }
}