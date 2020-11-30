package com.adelaide.autotest.utils;
/**
 * 返回统计
 * @author Wenping(Deb) Du
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class HttpsRequest{
	
	private static HttpsRequest reportRequest;
	
	public static HttpsRequest instanceRequest(){
		if(reportRequest == null){
			reportRequest = new HttpsRequest();
		}
		return reportRequest;
	}
	
	private static final String TAG = HttpsRequest.class.getSimpleName();
	
	/**
	 * 获取任务数据
	 * @param mContext
	 */
	public String requestTaskData(Context mContext){
		
//		// 创建一个http客户端
//		HttpClient client = new DefaultHttpClient();
//		// 创建一个Post请求
//		HttpPost httpPost = new HttpPost("https://172.16.20.104/invoke?app_id=xxx");
//		try {
//			// 向服务器发送请求并获取服务器返回的结果
//			HttpResponse response = client.execute(httpPost);
//			InputStream inputStream = response.getEntity().getContent();
////	        // 读取内容
//			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
//			String data = "";
//			StringBuffer sbResult = new StringBuffer();
//			if (sbResult.toString().equalsIgnoreCase("")) {
//
//				while ((data = br.readLine()) != null) {
//					sbResult.append(data);
//				}
//			}
//			inputStream.close();
//			String responseString = sbResult.toString();
//			Log.d(TAG, "----接口返回 ：" + responseString);
//
//			JSONObject obJsonObject = new JSONObject(responseString);
//			if(obJsonObject != null){
//				int code = obJsonObject.getInt("status_code");
//				if(code == 0){
//					return responseString;
//				}else{
//					Log.d(TAG, "返回码错误");
//					return "";
//				}
//			}
//		} catch (Exception e) {
//			Log.d(TAG, "连接异常");
//		}
		return "";
	}
}