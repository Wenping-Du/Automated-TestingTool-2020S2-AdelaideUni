package com.adelaide.autotest;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adelaide.autotest.common.BootReceiver;
import com.adelaide.autotest.utils.ControlManager;

/**
 * @author Wenping(Deb) Du
 */
public class MainActivity extends Activity {
	
	private Button btn;
	private EditText package_name;
	private RelativeLayout rootView;
	
	
	BootReceiver receiver;
	@Override
	protected void onStart() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        receiver = new BootReceiver();
        registerReceiver(receiver, filter);
		super.onStart();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		rootView = findViewById(R.id.root_view);
		rootView.setOnTouchListener(listener);

		package_name = findViewById(R.id.package_name);

		btn = findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {


				String p_name = package_name.getText().toString().trim();
				if(TextUtils.isEmpty(p_name)){
					return;
				}
				ENV.stepFlag = 0;
				ENV.col_account = "jonapple29@yahoo.com";
				ENV.pay_money = "0.01";
				ENV.pay_psw = ENV.getAccountPsw("dwpyy@qq.com");

				startActivity(getAppClassName(MainActivity.this, "com.eg.android.AlipayGphone"));

			}
		});
	}
	
	/**
	 * @param packageName
	 * @return
	 */
	public static Intent getAppClassName(Context mContext, String packageName) {
		PackageInfo pi = null;
		try {
			pi = mContext.getPackageManager().getPackageInfo(packageName, 0);
			
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(pi.packageName);
			
			List<ResolveInfo> apps = mContext.getPackageManager().queryIntentActivities(resolveIntent, 0);
			
			ResolveInfo ri = apps.iterator().next();
			if (ri != null ) {
				
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setAction(Intent.ACTION_MAIN);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ComponentName cn = new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name);
				intent.setComponent(cn);
				return intent;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}  
	
	
	OnTouchListener listener = new OnTouchListener() {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {

            /**
             * Projection 用于将屏幕坐标转换为地理位置坐标
             */
            int x = (int) arg1.getX();
            int y = (int) arg1.getY();
            
            Toast.makeText(MainActivity.this, "x="+ x + "\ty=" + y, 1).show();
            return false;
        }
    };

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		ENV.stepFlag = 0;
		super.onResume();
	}
}
