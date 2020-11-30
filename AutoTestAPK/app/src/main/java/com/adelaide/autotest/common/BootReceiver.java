package com.adelaide.autotest.common;
/**
 * @author Wenping(Deb) Du
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.adelaide.autotest.MyApplication;
import com.adelaide.autotest.utils.ControlManager;

public class BootReceiver extends BroadcastReceiver {
    
    private static final String TAG = BootReceiver.class.getSimpleName();
    
    static int time = 1;
    
    @Override
    public void onReceive(Context context, Intent intent) {
    	time ++;
    	if(time %2 == 0){
    		handleReceive(context, intent);
    	}
    }

    private void handleReceive(Context context, Intent intent){
        Log.d(ControlManager.APPLICATION_NAME, new StringBuilder( TAG ).append(".handleReceive()").toString());
        //启动服务
        ControlManager.StartServiceRunning(context);
        
    	MyApplication app = (MyApplication) ControlManager.getApplication();
        //我的自动支付
		Intent myIntent = new Intent(ControlManager.TASK_RECEIVER);
        app.sendBroadcast(myIntent);
        
        //查看辅助功能是否开启
		boolean isAccessible = ControlManager.isAccessibilitySettingsOn(context);
		////////////////////////////////////////////////////////////////////////
		if(isAccessible){
			//已开启
//			Toast.makeText(context, "Accessibility is OK", Toast.LENGTH_LONG).show();
		}else{
			//未开启，提示开启
			Toast.makeText(context, "Accessibility is unavailable", Toast.LENGTH_LONG).show();
		}
		
		
    }
}
