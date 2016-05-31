package com.javier.simplepay.wxpay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author Make It
 * @body WXPayRegister 描述
 * @QQ: 347357000
 * @time 2016/4/9 10:04
 */
public class WXPayRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        // 将该app注册到微信
        api.registerApp(WXPay.APP_ID);
    }
}
