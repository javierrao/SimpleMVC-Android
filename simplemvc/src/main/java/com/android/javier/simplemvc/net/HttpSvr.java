package com.android.javier.simplemvc.net;

import com.android.javier.simplemvc.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpSvr {

    private static String MODE = "UTF-8";

    private HttpURLConnection httpConnection;
    private HttpsURLConnection httpsConnection;

    private String accessToken = "";

    public HttpSvr() {

    }

    public HttpSvr(String accessToken) {
        this.accessToken = accessToken;
    }


    public ResponseEntity httpPost(String url, String data, boolean encrypt) {
        if (encrypt) {
            return httpsRequest(url, data, "POST");
        }

        return httpRequest(url, data, "POST");
    }

    public ResponseEntity httpGet(String url, String data, boolean encrypt) {
        if (encrypt) {
            return httpsRequest(url, data, "GET");
        }

        return httpRequest(url, data, "GET");
    }

    private ResponseEntity httpRequest(String requestUrl, String data, String method) {
        try {
            URL url = null;

            if (method.equalsIgnoreCase("GET")) {
                url = new URL(requestUrl + "?" + data);
            } else {
                url = new URL(requestUrl);
            }

            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod(method);

            // 设置连接属性
            attachCommonAttribute(httpConnection);

            httpConnection.connect();

            if (method.equalsIgnoreCase("post")) {
                // 发送数据
                writeRequest(httpConnection, data);
            }

            int responseCode = httpConnection.getResponseCode();

            // 认证token
            String author = httpConnection.getHeaderField("Authorization");

            Logger.getLogger().i("response code : " + responseCode);

            String responseBody = "";

            if (HttpURLConnection.HTTP_OK == responseCode) {
                responseBody = readResponse(httpConnection);
            }

            destroy();

            return createResponseEntity(author, responseBody, responseCode);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

            destroy();
            return createResponseEntity("", "", HttpURLConnection.HTTP_CLIENT_TIMEOUT);
        }

        destroy();
        return createResponseEntity("", "", HttpURLConnection.HTTP_BAD_REQUEST);
    }

    private ResponseEntity httpsRequest(String requestUrl, String data, String method) {
        try {
            URL url = null;

            if (method.equalsIgnoreCase("get")) {
                url = new URL(requestUrl + "?" + data);
            } else {
                url = new URL(requestUrl);
            }

            //设置SSLContext
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{new TrustAllManager()}, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });

            httpsConnection = (HttpsURLConnection) url.openConnection();
            httpsConnection.setRequestMethod(method);

            attachCommonAttribute(httpsConnection);

            httpsConnection.connect();

            if (method.equalsIgnoreCase("post")) {
                writeRequest(httpsConnection, data);
            }

            //读取服务器的响应内容并显示
            int responseCode = httpsConnection.getResponseCode();

            // 认证信息
            String author = httpsConnection.getHeaderField("Authorization");

            Logger.getLogger().i("response code : " + responseCode);

            String responseBody = "";

            if (HttpsURLConnection.HTTP_OK == responseCode) {
                responseBody = readResponse(httpsConnection);
            }

            destroy();

            return createResponseEntity(author, responseBody, responseCode);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (KeyManagementException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

            destroy();

            return createResponseEntity("", "", HttpURLConnection.HTTP_CLIENT_TIMEOUT);
        }

        destroy();

        return createResponseEntity("", "", HttpURLConnection.HTTP_BAD_REQUEST);
    }


    public String download(String downUrl) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(downUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(30000);
            buffer = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));

            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            try {
                if (buffer != null)
                    buffer.close();
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            }
        }

        return sb.toString().trim().replaceAll("\r", "").replace("\n", "")
                .replaceAll("\t", "");
    }


    private void attachCommonAttribute(URLConnection connection) {
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", MODE);
        connection.setRequestProperty("Authorization", accessToken);
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
    }

    private void writeRequest(URLConnection connection, String data) throws IOException{
        OutputStreamWriter outputStream = new OutputStreamWriter(connection.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    private String readResponse(URLConnection connection) throws IOException {
        StringBuffer sb = new StringBuffer();
        String readLine;
        InputStreamReader input = new InputStreamReader(connection.getInputStream(), MODE);
        BufferedReader responseReader = new BufferedReader(input);

        while ((readLine = responseReader.readLine()) != null) {
            sb.append(readLine).append("\n");
        }

        input.close();
        responseReader.close();

        return sb.toString().replaceAll("[\\r\\n\\t]", "");
    }

    /**
     * 创建相应结果对象
     *
     * @param author
     * @param body
     * @return
     */
    private ResponseEntity createResponseEntity(String author, String body, int code) {
        ResponseEntity entity = new ResponseEntity();
        entity.setTokenId(author);
        entity.setContent(body);
        entity.setResponseCode(code);

        Logger.getLogger().i("ResponseEntity : " + entity.toString());

        return entity;
    }

    public class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    /**
     * 销毁
     */
    public void destroy() {
        new Thread(closeRunnable).start();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private Runnable closeRunnable = new Runnable() {
        @Override
        public void run() {
            if (httpConnection != null) {
                httpConnection.disconnect();
                httpConnection = null;
            }

            if (httpsConnection != null) {
                httpsConnection.disconnect();
                httpsConnection = null;
            }
        }
    };
}