package com.adelaide.autotest.utils;
/**
 * @author Wenping(Deb) Du
 */
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.adelaide.autotest.MyApplication;
import com.adelaide.autotest.bean.AccountBeanData;
import com.adelaide.autotest.common.AutoPayService;

public class ControlManager {
	private static final String TAG = ControlManager.class.getSimpleName();
	
    public static final String APPLICATION_NAME = "Auto_pay_service";
    
    public static final String TASK_RECEIVER = "android.softisland.action.taskservice";
    
    private static Application mApp = null;
    
    private static Context mContext = null;
    
    public static void setApplication(Application app) {
        mApp = app;
    }

    public static Application getApplication() {
        return mApp;
    }

    public static void setApplicationContext(Context cont) {
        mContext = cont;
    }

    public static Context getApplicationContext() {
        return mContext;
    }
    
    
    public static void StartServiceRunning(Context context){
        boolean isServiceRunning = false;
        
        MyApplication app = (MyApplication) ControlManager.getApplication();
        ActivityManager manager = (ActivityManager)app.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if (AUTOPAY_SERVICE_CLASSNAME.equals(service.service.getClassName())){
                isServiceRunning = true;
                break;
            }
        }
        
        if (!isServiceRunning){
            Intent i = new Intent(context, AutoPayService.class);
            context.startService(i);
        }
    }
    
    
    private static final String AUTOPAY_SERVICE_CLASSNAME = "com.softisland.accessbility.common.AutoPaySeivice";
   
    
    
    /**
	 check network
	*/
  public static boolean checkConnect(Context mContext) {
		if (isNetworkConnected(mContext) || isWiFiActive(mContext)) 
			return true;
		return false;
	}
	
  /**
	 check network for E or 3G
	*/
	public static boolean isNetworkConnected(Context mContext) {
		boolean success = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo network = cm.getActiveNetworkInfo();
			if (network != null) {
				success = network.isAvailable();
			}
		} catch (Exception ex) {
			success = false;
		}
		return success;
	}
	
	 /**
	 check network for WIFI
	*/
	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 检测辅助功能是否开启
	 * @author Deb
	 *
	 */
	public static boolean isAccessibilitySettingsOn(Context mContext) {
	    int accessibilityEnabled = 0;
	    final String service = mContext.getPackageName() + "/" + AutoPayService.class.getCanonicalName();
	    try {
	        accessibilityEnabled = Settings.Secure.getInt(
	                mContext.getApplicationContext().getContentResolver(),
	                Settings.Secure.ACCESSIBILITY_ENABLED);
	        Log.d(ControlManager.APPLICATION_NAME, new StringBuilder(TAG).append("accessibilityEnabled = " + accessibilityEnabled).toString());
			
	    } catch (Settings.SettingNotFoundException e) {
	    	
	    	Log.d(ControlManager.APPLICATION_NAME, new StringBuilder(TAG)
	    	.append("Error finding setting, default accessibility to not found: "
	                + e.getMessage()).toString());
	    }
	    TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

	    if (accessibilityEnabled == 1) {
	        Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
	        String settingValue = Settings.Secure.getString(
	                mContext.getApplicationContext().getContentResolver(),
	                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
	        if (settingValue != null) {
	            mStringColonSplitter.setString(settingValue);
	            while (mStringColonSplitter.hasNext()) {
	                String accessibilityService = mStringColonSplitter.next();
	                Log.d(ControlManager.APPLICATION_NAME, new StringBuilder(TAG)
	    	    	.append("-------------- > accessibilityService :: " + accessibilityService + " " + service).toString());
	                if (accessibilityService.equalsIgnoreCase(service)) {
	                	Log.d(ControlManager.APPLICATION_NAME, new StringBuilder(TAG)
		    	    	.append("We've found the correct setting - accessibility is switched on!").toString());
	                    return true;
	                }
	            }
	        }
	    } else {
	        Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
	    }

	    return false;
	}
	
	//打款任务列表
	private static List<AccountBeanData> payTaskList = new ArrayList<AccountBeanData>();
	//打款汇报列表
	private static List<AccountBeanData> reportTaskList = new ArrayList<AccountBeanData>();
	
	/**
	 * 是否任务列表为空
	 * @return
	 */
	public static boolean hasTaskListEmpty(){
		if(payTaskList == null || payTaskList.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * 向任务列表中添加任务
	 */
	public static void addPayTask(AccountBeanData task){
		if(payTaskList == null){
			payTaskList = new ArrayList<AccountBeanData>();
		}
		if(payTaskList.isEmpty())
			payTaskList.add(task);
	}
	
	/**
	 * 获取当前打款任务
	 * @return
	 */
	public static AccountBeanData getCurrentPayTask(){
		if(!hasTaskListEmpty())
			return payTaskList.get(0);
		return null;
	}
	
	/**
	 * 移除任务(任务已完成或完成失败，添加任务至汇报列表)
	 */
	public static void removePayTask(){
		if(!hasTaskListEmpty()){
			payTaskList.clear();
		}
	}
	
	
	/**
	 * 是否汇报列表为空
	 * @return
	 */
	public static boolean hasReportListEmpty(){
		if(reportTaskList == null || reportTaskList.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * 向汇报列表中添加任务
	 */
	public static void addReportTask(AccountBeanData task){
		if(reportTaskList == null){
			reportTaskList = new ArrayList<AccountBeanData>();
		}
		if(reportTaskList.isEmpty())
			reportTaskList.add(task);
	}
	
	
}
