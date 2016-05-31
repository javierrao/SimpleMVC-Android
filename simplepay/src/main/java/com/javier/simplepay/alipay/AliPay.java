package com.javier.simplepay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.javier.simplepay.ParamParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;


/**
 * 支付宝支付
 */
@SuppressWarnings("unused")
public class AliPay {
	private static String tag = "AliPayUtil";

	// 商户PID
	private static String PARTNER = "";
	// 商户收款账号
	private static String SELLER = "";
	// 商户私钥，pkcs8格式
	private static String RSA_PRIVATE = "";
	// 服务器通知页面
	private static String NOTIFY_URL = "";

	// 是否准备工作已完成
	private boolean isPayPrepared = false;

	// 交易超时时间，单位ms
	private static final int PAY_TIMEOUT = 30;

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	// 返回支付结果的CODE
	// 支付成功
	public static final String PAY_SUCCESS = "9000";
	// 支付待确认
	public static final String PAY_WAIT_CONFIRM = "8000";
	// 支付失败
	public static final String PAY_FAILED = "4000";
	// 用户中途取消
	public static final String PAY_CANCEL = "6001";
	// 网络连接错误
	public static final String PAY_NETWORK_ERROR = "6002";
	// 支付结果验证失败
	public static final String PAY_RESULT_VALIDATE_FAILDE = "7000";

	// 唯一订单号
	private String mOrderNo;

	// 商品名称
	private String mProductName;

	// 商品详情
	private String mProductSubject;

	// 交易金额
	private float mOrderPrice;

	// 上下文
	private Activity mActivityContext;

	private OnAliPayResultListener mListener;

	public interface OnAliPayResultListener {
		void onPayResult(String code);
	}

	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				if (resultInfo == null || resultInfo.equals("")) {
					callback(PAY_CANCEL);
					return;
				}

				HashMap<String, String> resultMap = ParamParser
						.alipayResultParser(resultInfo);

				if (!resultMap.get("partner").equals(PARTNER)
						|| !resultMap.get("seller_id").equals(SELLER)
						|| !resultMap.get("out_trade_no").equals(mOrderNo)
						|| !resultMap.get("notify_url").equals(NOTIFY_URL)
						|| !resultMap.get("it_b_pay").equals(
								String.valueOf(PAY_TIMEOUT))) {
					callback(PAY_RESULT_VALIDATE_FAILDE);
					return;
				}

				String resultStatus = payResult.getResultStatus();

				callback(resultStatus);

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				// if (TextUtils.equals(resultStatus, "9000")) {
				// callback(PAY_SUCCESS);
				// } else {
				// // 判断resultStatus 为非“9000”则代表可能支付失败
				// //
				// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				// if (TextUtils.equals(resultStatus, "8000")) {
				// Toast.makeText(mActivityContext, "支付结果确认中",
				// Toast.LENGTH_SHORT).show();
				// } else {
				// // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
				// Toast.makeText(mActivityContext, "支付失败",
				// Toast.LENGTH_SHORT).show();
				// }
				// }
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(mActivityContext, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		}
	};

	public AliPay(Activity context) {
		this.mActivityContext = context;
	}

	public AliPay(Activity context, OnAliPayResultListener listener) {
		this(context);
		this.mListener = listener;
	}

	/**
	 * 准备支付
	 * @param pid				商户ID
	 * @param seller			商户收款账号
	 * @param rsaprivate		RAS私钥
	 * @param notifyUrl		支付完成后通知的URL
	 */
	public void payPrepare(String pid, String seller, String rsaprivate, String notifyUrl) {
		PARTNER = pid;
		SELLER = seller;
		RSA_PRIVATE = rsaprivate;
		NOTIFY_URL = notifyUrl;

		isPayPrepared = true;
	}

	public void setOnAliPayResultListener(OnAliPayResultListener listener) {
		this.mListener = listener;
	}

	public void pay(String orderNo, String pName, String subject,
			float mOrderPrice) {
		if (!isPayPrepared) {
			Log.e(tag , "alipay is not parpared, did't you call payPrepare() method first?");
			return;
		}

		this.mOrderNo = orderNo;
		this.mProductName = pName;
		this.mProductSubject = subject;
		this.mOrderPrice = mOrderPrice;

		if (mOrderNo.equals("") || mProductName.equals("")
				|| mProductSubject.equals("")) {
			Log.e(tag , "alipay error. order infomation not full!");
			return;
		}

		// 创建订单
		String orderInfo = createOrderInfo();

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(mActivityContext);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * create the order info. 创建订单信息
	 */
	private String createOrderInfo() {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + mOrderNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + mProductName + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + mProductSubject + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + mOrderPrice + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + NOTIFY_URL + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"" + PAY_TIMEOUT + "\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private void callback(String code) {
		if (mListener != null) {
			mListener.onPayResult(code);
		}
	}
}