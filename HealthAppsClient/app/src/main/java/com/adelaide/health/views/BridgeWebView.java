package com.adelaide.health.views;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * @author Wenping(Deb) Du
 */
@SuppressLint("SetJavaScriptEnabled")
public class BridgeWebView extends WebView implements WebViewJavascriptBridge {

	private final String TAG = "BridgeWebView";

//	String toLoadJs = null;
	Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();
	Map<String, BridgeHandler> messageHandlers = new HashMap<String, BridgeHandler>();
	BridgeHandler defaultHander = new DefaultHandler();

	List<Message> startupMessage = new ArrayList<Message>();
	long uniqueId = 0;

	
	private Context mContext;
	public BridgeWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init(context);
	}

	public BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init(context);
	}

	public BridgeWebView(Context context) {
		super(context);
		this.mContext = context;
		init(context);
	}

	/**
	 * 
	 * @param toLoadedJsUrl
	 *            要注入的js http地址
	 * @param handler
	 *            默认的handler,负责处理js端没有指定handlerName的消息,若js端有指定handlerName,
	 *            则由native端注册的指定处理
	 */
	public void initContext(String toLoadedJsUrl, BridgeHandler handler) {
//		if (toLoadedJsUrl != null) {
//			this.toLoadJs = toLoadedJsUrl;
//		}
		if (handler != null) {
			this.defaultHander = handler;
		}
	}

	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context) {
		this.getSettings().setLoadsImagesAutomatically(false);
		this.setVerticalScrollBarEnabled(false);
		this.setHorizontalScrollBarEnabled(false);
		this.getSettings().setJavaScriptEnabled(true);
		this.setWebViewClient(new BridgeWebViewClient());
		this.getSettings().setDomStorageEnabled(true); 
		this.getSettings().setSupportZoom(true);
		this.getSettings().setUseWideViewPort(true); 
		this.getSettings().setLoadWithOverviewMode(true); 
		this.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);  
		this.getSettings().setBuiltInZoomControls(false);
		this.setInitialScale(25);
		this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
	}
	
	/**
	 * 图片自动加载设置
	 */
	private void initImageAuto() {
		if(!this.getSettings().getLoadsImagesAutomatically()) {
			this.getSettings().setLoadsImagesAutomatically(true);
	    }
	}
	
	/**
	 * 关闭硬件加速
	 */
	@SuppressLint("NewApi")
	private void setLayerType() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
	}

	private void handlerReturnData(String url) {
		String functionName = BridgeUtil.getFunctionFromReturnUrl(url);
		CallBackFunction func = responseCallbacks.get(functionName);
		String data = BridgeUtil.getDataFromReturnUrl(url);
		Log.i(TAG, "handlerReturnData " + "functionName = " + functionName + " f = " + func + " data = " + data);
		if (func != null) {
			//解析url 数据
			//[{"handlerName":"userShare","data":{"title":"搞定丈母娘","url":"http://apptest.game.zhaopian.com/wydlz_game/gameing.html?appid=34&g_type=3&re_id=927","content":"我刚才盖了13层楼房，得到了丈母娘的欢喜，你也来试试吧，还可以赢豆抽话费哦！","icon":"http://apptest.game.zhaopian.com/games/gdzmn/icon.png","score":13},"callbackId":"cb_2_1416471892083"}]
//			try {
//				int start = data.indexOf("[");
//				int end = data.indexOf("]");
//				data = data.substring(start + 1, end);
//				String jsonObj = new JSONObject(data).getString("data");
//				Log.i(TAG, "解析url 数据：" + jsonObj);
				func.onCallBack(data);
				responseCallbacks.remove(functionName);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
			return;
		}
	}

	class BridgeWebViewClient extends WebViewClient {
		
		@SuppressLint("NewApi")
		public WebResourceResponse shouldInterceptRequest(WebView view, String url){
			
			Log.v("KupingGamesCookies", "shouldInterceptRequest="+url);
			
			String ajaxUrl = url;
//			ajaxUrl += ("&token=" + Constants.getLoginTokenData(mContext));
			
		    return super.shouldInterceptRequest(view, ajaxUrl); 
		}
		
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			try {
				url = URLDecoder.decode(url, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.i(TAG, "shouldOverrideUrlLoading, url = " + url);
			
			if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
				handlerReturnData(url);
				return true;
			} else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
				flushMessageQueue();
				return true;
			} else {
				return super.shouldOverrideUrlLoading(view, url);
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			//关闭硬件加速
			setLayerType();
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			initImageAuto();
//			if (toLoadJs != null) {
//				BridgeUtil.webViewLoadJs(view, toLoadJs);
//			}

			if (startupMessage != null) {
				for (Message m : startupMessage) {
					dispatchMessage(m);
				}
				startupMessage = null;
			}
		}
		
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			view.loadUrl("about:blank");
			Toast.makeText(view.getContext(), "Network error", Toast.LENGTH_SHORT).show();
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	@Override
	public void send(String data) {
		send(data, null);
	}

	@Override
	public void send(String data, CallBackFunction responseCallback) {
		doSend(data, responseCallback, null);
	}

	private void doSend(String data, CallBackFunction responseCallback, String handlerName) {
		Message m = new Message();
		if (!TextUtils.isEmpty(data)) {
			m.setData(data);
		}
		if (responseCallback != null) {
			String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT,
					(++ uniqueId) + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
			responseCallbacks.put(callbackStr, responseCallback);
			m.setCallbackId(callbackStr);
		}
		if (!TextUtils.isEmpty(handlerName)) {
			m.setHandlerName(handlerName);
		}
		queueMessage(m);
	}

	private void queueMessage(Message m) {
		if (startupMessage != null) {
			startupMessage.add(m);
		} else {
			dispatchMessage(m);
		}
	}

	private void dispatchMessage(Message m) {
		String messageJson = m.toJson();
		String javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
		Log.i(TAG, "dispatchMessage " + javascriptCommand);
		if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
			this.loadUrl(javascriptCommand);
		}
	}

	public void flushMessageQueue() {
		if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
			loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA, new CallBackFunction() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void onCallBack(String data) {
					// deserializeMessage
					List<Message> list = null;
					try {
						list = Message.toArrayList(data);
					} catch (Exception e) {
						return;
					}
					if (list == null || list.size() == 0) {
						return;
					}
					for (int i = 0; i < list.size(); i++) {
						Message m = list.get(i);
						String responseId = m.getResponseId();
						// 是否是response
						if (!TextUtils.isEmpty(responseId)) {
							CallBackFunction fuction = responseCallbacks.get(responseId);
							fuction.onCallBack(m.getResponseData());
							responseCallbacks.remove(responseId);
						} else {
							CallBackFunction responseFunction = null;
							// 是否是callbackId
							final String callbackId = m.getCallbackId();
							if (!TextUtils.isEmpty(callbackId)) {
								responseFunction = new CallBackFunction() {
									private static final long serialVersionUID = -4787493242699247218L;

									@Override
									public void onCallBack(String data) {
										Log.i(TAG, "responseFunction " + data);
										try {
											String responseData = URLEncoder.encode(data, "utf-8");
											Message responseMsg = new Message();
											responseMsg.setResponseId(callbackId);
											responseMsg.setResponseData(responseData);
											queueMessage(responseMsg);				
										} catch (UnsupportedEncodingException e) {
											e.printStackTrace();
										}
									}
								};
							} else {
								responseFunction = new CallBackFunction() {
									private static final long serialVersionUID = 5561403908374127125L;
									@Override
									public void onCallBack(String data) {
										// do nothing
									}
								};
							}
							BridgeHandler handler;
							if (!TextUtils.isEmpty(m.getHandlerName())) {
								handler = messageHandlers.get(m.getHandlerName());
							} else {
								handler = defaultHander;
							}
							handler.handler(m.getData(), responseFunction);
						}
					}
				}
			});
		}
	}

	public void loadUrl(String jsUrl, CallBackFunction returnCallback) {
		this.loadUrl(jsUrl);
		responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), returnCallback);
		Log.i(TAG, "BridgeWebView put map key = " + BridgeUtil.parseFunctionName(jsUrl) + " value = " + returnCallback);
	}

	/**
	 * 注册handler,方便web调用
	 * 
	 * @param handlerName
	 * @param handler
	 */
	public void registerHandler(String handlerName, BridgeHandler handler) {
		if (handler != null) {
			messageHandlers.put(handlerName, handler);
		}
	}

	/**
	 * 调用web的handler
	 * 
	 * @param data
	 * @param callBack
	 * @param handlerName
	 */
	public void callHandler(String data, CallBackFunction callBack, String handlerName) {
		doSend(data, callBack, handlerName);
	}
	
	@JavascriptInterface
	public void finishThisPage(){
	}
}
