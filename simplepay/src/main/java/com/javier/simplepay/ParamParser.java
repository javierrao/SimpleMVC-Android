package com.javier.simplepay;

import java.util.HashMap;

/**
 * author:Javier
 * time:2016/5/24.
 * mail:38244704@qq.com
 */
public class ParamParser {
    /**
     * 解析支付宝支付返回结果
     *
     * @param result
     * @return
     */
    public static HashMap<String, String> alipayResultParser(String result) {
        HashMap<String, String> map = new HashMap<String, String>();

        String[] params = result.split("&");

        for (int i = 0; i < params.length; i++) {
            String param = params[i];

            int index = param.indexOf("=");

            String key = param.substring(0, index);
            String value = param.substring(index + 1, param.length()).replaceAll("\"", "");

            map.put(key, value);
        }

        return map;
    }

    /**
     * 解析返回结果并返回需要验签的部分
     *
     * @param result
     * @return
     */
    public static String alipayValidataSignParser(String result) {
        int index = result.indexOf("&sign_type");

        String vaildateStr = result.substring(0, index);

        return vaildateStr;
    }
}
