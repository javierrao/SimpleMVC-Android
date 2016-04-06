package com.android.javier.simplemvc.interfaces;

/**
 * Created by javier on 2016/3/27.
 * <p>
 * 加解密的接口，在执行网络任务task的执行。
 * 如果请求的数据和接收的数据需要进行加密，则实现该接口
 */
public interface IEncrypt {
    /**
     * 解密
     *
     * @param encryptStr 待解密字符串
     * @return 解密后的字符串
     */
    String decrypt(String encryptStr);

    /**
     * 加密
     *
     * @param str 待加密字符串
     * @return 加密后的字符串
     */
    String encrypt(String str);
}
