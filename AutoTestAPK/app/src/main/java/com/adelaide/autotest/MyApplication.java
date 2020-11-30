package com.adelaide.autotest;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.adelaide.autotest.common.BootReceiver;
import com.adelaide.autotest.common.CrashHandler;
import com.adelaide.autotest.utils.ControlManager;

/**
 * @author Wenping(Deb) Du
 */
public class MyApplication extends Application {
	
	@Override
	public void onCreate() {

		super.onCreate();
		CrashHandler catchHandler = CrashHandler.getInstance();
		catchHandler.init(getApplicationContext());

		ControlManager.setApplication(this);
		ControlManager.setApplicationContext(this.getApplicationContext());

		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
		BootReceiver receiver = new BootReceiver();
		registerReceiver(receiver, filter);

	}
}

