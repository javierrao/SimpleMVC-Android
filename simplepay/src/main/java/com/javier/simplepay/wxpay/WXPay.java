package com.javier.simplepay.wxpay;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 微信支付
 */
public class WXPay {
    private static String tag = "WXPay";
    // appid
    public static String APP_ID = "";// yum

    // 商户号
    public static String MCH_ID = "";// yum

    // 通知的URL
    public static String NOTIFY_URL = "";

    // API KEY
    public static String API_KEY = "";

    // 是否准备好微信支付
    private boolean isWxPayPrepare = false;

    private Context mContext;

    private PayReq req;
    private IWXAPI api = null;

    private Map<String, String> resultunifiedorder;
    private WXPayListener mWXListener;

    private String mBody, mOrderNo;
    private int mTotalFee;

    public interface WXPayListener {
        void onCreatePrepayOrder();
    }

    public WXPay(Context context) {
        this.mContext = context;

        api = WXAPIFactory.createWXAPI(mContext, null);
    }

    public WXPay(Context context, WXPayListener listener) {
        this(context);
        this.mWXListener = listener;
    }

    /**
     * 准备微信支付
     *
     * @param appid     app id
     * @param pid       商户号
     * @param notifyUrl 通知的URL
     * @param apiKey    api key
     */
    public void wxPayPrepare(String appid, String pid, String notifyUrl, String apiKey) {
        APP_ID = appid;
        MCH_ID = pid;
        NOTIFY_URL = notifyUrl;
        API_KEY = apiKey;

        isWxPayPrepare = true;
    }

    public void setOnWXPayListener(WXPayListener listener) {
        this.mWXListener = listener;
    }

    /**
     * 微信支付，对外接口
     *
     * @param out_trade_no 商品描述
     * @param total_fee    商品详情
     */
    public void wx_pay(String out_trade_no, int total_fee, String wxpay_body) {
        if (!isWxPayPrepare) {
            Log.e(tag, "wx pay is not prepared, did't you forget call wxPayPrepare() method first?");
            return;
        }

        this.mOrderNo = out_trade_no;
        this.mTotalFee = total_fee;
        this.mBody = String.format(wxpay_body, mOrderNo);

        createPrepay();
    }

    // 获取预支付订单
    private void createPrepay() {
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();
    }

    // 生成预支付参数
    private void createPrePayReq() {
        genPayReq();
    }

    // 调启支付
    private void sendPayReq() {
        api.registerApp(APP_ID);
        api.sendReq(req);
    }

    /*
     * 创建预支付订单所需参数
     */
    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid",
                    APP_ID));
            packageParams.add(new BasicNameValuePair("body", mBody));
            packageParams.add(new BasicNameValuePair("mch_id",
                    MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url",
                    NOTIFY_URL));
            packageParams.add(new BasicNameValuePair("out_trade_no", mOrderNo));
            packageParams.add(new BasicNameValuePair("spbill_create_ip",
                    "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", String
                    .valueOf(mTotalFee)));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);

            return new String(xmlstring.toString().getBytes(), "ISO8859-1");

            // return xmlstring;

        } catch (Exception e) {
            return null;
        }
    }

    /*
     * 创建XML对象
     */
    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e(tag , sb.toString());
        return sb.toString();
    }

    /*
     * 解析XML
     */
    private Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e(tag, e.toString());
        }
        return null;

    }

    /*
     * 请求参数签名
     */
    private void genPayReq() {
        req = new PayReq();
        req.appId = APP_ID;
        req.partnerId = MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        Log.i(tag, "req.sign" + req.sign);
    }

    /**
     * 生成签名
     */
    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        Log.e(tag, "package sign : " + packageSign);
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);

        Log.i(tag, "sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        return appSign;
    }

    // 创建随机数
    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    // 创建时间戳
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    // 生成预支付订单的异步请求
    private class GetPrepayIdTask extends
            AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            Log.i(tag, "wx pay prepay_id : " + result.get("prepay_id"));
            resultunifiedorder = result;

            Toast.makeText(mContext, "获取订单中...", Toast.LENGTH_SHORT).show();

			// 回调
			if (mWXListener != null) {
				mWXListener.onCreatePrepayOrder();
			}

			// 签名支付参数
			createPrePayReq();

			// 调启微信支付
			sendPayReq();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {
            String url = String
                    .format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();
            Log.i(tag, "get prepay order request : " + entity);

            byte[] buf = WXPayConstants.httpPost(url, entity);

            String content = new String(buf);
            Log.i(tag, "get prepay order response : " + content);

            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }
}