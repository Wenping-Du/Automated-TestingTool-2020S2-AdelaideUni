package com.adelaide.autotest.utils;
/**
 * @author Wenping(Deb) Du
 */
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utils{

	public static String signature(String source) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			md.reset();
			md.update(source.getBytes());

			byte[] mdbytes = md.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				String hex = Integer.toHexString(0xff & mdbytes[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//密码加密 与php加密一致
    public static String md5(String input) throws NoSuchAlgorithmException {
	    String result = input;
	    if(input != null) {
		    MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
		    md.update(input.getBytes());
		    BigInteger hash = new BigInteger(1, md.digest());
		    result = hash.toString(16);
		    while(result.length() < 32) {
		    	result = "0" + result;
		    }
	    }
	    return result;
    }

    /**
	 * 获取mac地址
	 * @param mContext
	 * @return
	 */
	public static String getMacAddress(Context mContext){
		WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
	
	/**
	 * 获取IP地址
	 * @param mContext
	 * @return
	 */
	public static String getIPAddress(Context mContext){
		WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return intToIp(info.getIpAddress());
	}
	
	/**
	 * 拼组ip
	 * @param i
	 * @return
	 */
	private static String intToIp(int i) {       
        return (i & 0xFF ) + "." +       
        		((i >> 8 ) & 0xFF) + "." +       
        		((i >> 16 ) & 0xFF) + "." +       
        		( i >> 24 & 0xFF) ;  
   }  
	
	/** 
    * 获取当前程序的版本号 
    */ 
    public static String getVersionName(Context mContext) { 
		try {
			//获取packagemanager的实例 
			PackageManager packageManager = mContext.getPackageManager(); 
			//getPackageName()是你当前类的包名，0代表是获取版本信息 
			PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
			return packInfo.versionName; 
		} catch (NameNotFoundException e) {
			return "1.0.1";
		} 
    }
    
    public static String getResponseAllLine(InputStream is) throws Exception  {
        String strResult = null;
        
        BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(2);
        // 取前两个字节
        byte[] header = new byte[2];
        int result = bis.read(header);
        // reset输入流到开始位置
        bis.reset();
        // 判断是否是GZIP格式
        int headerData = getShort(header);
        // Gzip 流 的前两个字节是 0x1f8b
        if (result != -1 && headerData == 0x1f8b) {
            Log.d(ControlManager.APPLICATION_NAME, new StringBuilder("Utils").append(" use GZIPInputStream") + "");
            is = new GZIPInputStream(bis);
        } else {
        	Log.d(ControlManager.APPLICATION_NAME, new StringBuilder("Utils").append(" not use GZIPInputStream") + "");
            is = bis;
        }
        InputStreamReader reader = new InputStreamReader(is, "utf-8");
        char[] data = new char[100];
        int readSize;
        StringBuffer sb = new StringBuffer();
        while ((readSize = reader.read(data)) > 0) {
            sb.append(data, 0, readSize);
        }
        strResult = sb.toString();
        bis.close();
        reader.close();
 
        return strResult;
    }
	private static int getShort(byte[] data) {
      return (int)((data[0]<<8) | data[1]&0xFF);
	}
    
	
	
	
	
	//代理
	private static String proxyHost = "";
	private static int proxyPort = 80;
	private static final String RequestPropertyContentType = "Content-Type";
	private static final String ContentTypeJson = "application/json; charset=utf-8";
	private static final String AcceptEncoding = "gzip";
	private static final String RequestPropertyAcceptEncoding = "Accept-Encoding";
	
	public static HttpURLConnection getHttpUrlConnection(URL url, String method, boolean hasProxy) throws Exception {
		HttpURLConnection http = null;
		if (hasProxy) {
			http = (HttpURLConnection)url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
		} else {
			http = (HttpURLConnection)url.openConnection();
		}
		http.setRequestMethod(method);
		http.setDoOutput(false);
//		if (method.equals(HttpPost.METHOD_NAME)) {
//			http.setRequestMethod(HttpPost.METHOD_NAME);
//			http.setDoOutput(true);
//		}
		http.setDoInput(true);
		http.setConnectTimeout(20000);
		http.setReadTimeout((int)20000);
		http.setRequestProperty(RequestPropertyContentType, ContentTypeJson);
		http.setRequestProperty(RequestPropertyAcceptEncoding, AcceptEncoding);
		
		return http;
	}
	
	/**
	 * 检查安装包正确性
	 * @param context
	 * @param filePath
	 * @return
	 */
	public static boolean getUninatllApkInfo(Context context, String filePath) {
		boolean result = false;
		try {
			PackageManager pm = context.getPackageManager();
			Log.e("archiveFilePath", filePath);
			PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
			if (info != null) {
				result = true;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	//获取屏幕分辨
	public static void getDisplayMetrics(Activity context){
		int screenheight;//屏幕高度（竖直状态下的高）
		int screenwidth;//屏幕宽度
		//之后在程序中通过以下代码获取设备屏幕分辨率
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenwidth = dm.widthPixels;
		screenheight = dm.heightPixels;
		
		SharedPreferences preferences = context.getSharedPreferences("Display_show", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("screenheight", screenheight);
		editor.putInt("screenwidth", screenwidth);
		editor.commit();
	}
	
	/**
	 * 获取设备高
	 * @param mContext
	 */
	public static int getShowHeight(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences("Display_show", Context.MODE_PRIVATE);
		return preferences.getInt("screenheight", 0);
	}
	
	/**
	 * 获取设备宽
	 * @param mContext
	 */
	public static int getShowWidth(Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences("Display_show", Context.MODE_PRIVATE);
		return preferences.getInt("screenwidth", 0);
	}
}