package com.adelaide.autotest.common;
/**
 * @author Wenping(Deb) Du
 */
import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import com.adelaide.autotest.ENV;
import com.adelaide.autotest.RootShellCmd;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class AutoPayService extends AccessibilityService {
	final String TAG = AutoPayService.class.getSimpleName();

	private static int times = 0;//输入支付账户
	private static int payInputTimes = 0;//输入金额
	private static int pwdInputTimes = 0;//输入密码

	@Override
	protected void onServiceConnected(){
		super.onServiceConnected();
		ENV.initAccountHashMap();
//		可用代码配置当前Service的信息
		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK; //监听哪些行为
		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN; //反馈
		info.notificationTimeout = 100; //通知的时间
		info.packageNames = new String[]{"com.eg.android.AlipayGphone", "com.adelaide.autotest"};
		setServiceInfo(info);

	}

	/**
	 * 输入账户后  点击下一步
	 */

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			if(msg.what == INPUT_ACCOUNT_TYPE){
				//点击下一步
				List<AccessibilityNodeInfo> nexts = getRootInActiveWindow().findAccessibilityNodeInfosByText("下一步");
				for (int j = 0; j < nexts.size(); j ++) {
					AccessibilityNodeInfo nodeBtn = nexts.get(j);
					if ("android.widget.Button".equals(nodeBtn.getClassName()) && nodeBtn.isEnabled()) {
						times = 0;
						nodeBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						ENV.stepFlag = 3;
					}
				}
			} else if(msg.what == INPUT_MONEY_TYPE){
				//点击确认转账
				List<AccessibilityNodeInfo> nexts = getRootInActiveWindow().findAccessibilityNodeInfosByText("确认转账");
				if(nexts == null || nexts.size() <= 0){
					return;
				}
				for (int j = 0; j < nexts.size(); j ++) {
					AccessibilityNodeInfo nodeBtn = nexts.get(j);
					if ("android.widget.Button".equals(nodeBtn.getClassName())) {
						payInputTimes = 0;
						nodeBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						ENV.stepFlag = 4;
					}
				}
			} else if(msg.what == 0x0012){
				//退出栈
				for(int i = 0; i < 3; i ++){
					AutoPayService.this.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
				}
			}
		};
	};

	//输入数字密码
	@SuppressLint("NewApi")
	private void inputNumberPwdPerformAction(){
		if(getRootInActiveWindow()==null)
			return;
		if(ENV.stepFlag != 4){
			return;
		}
		try {
			List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText("输入密码");
			for (int i = 0; i < nodes.size(); i++) {
				AccessibilityNodeInfo node = nodes.get(i);
				if ("android.widget.TextView".equals(node.getClassName()) && pwdInputTimes < 1) {
					pwdInputTimes ++;
					RootShellCmd.getInstance().simulateSomeStrs(ENV.pay_psw);
					ENV.stepFlag = 5;
				}
			}

		} catch (Exception e) {
		}
	}

	//进入支付宝主页   点击转账项
	private void step1PerformAction(){
		if(getRootInActiveWindow()==null)
			return;
		if(ENV.stepFlag != 0){
			return;
		}
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> tab1 = getRootInActiveWindow().findAccessibilityNodeInfosByText("Home");
		if(tab1 == null || tab1.size() <= 0){
			return;
		}

		tab1.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText("Transfer");
		if(nodes == null || nodes.size() <= 0){
			return;
		}
		int size = nodes.size();
		for (int i = 0; i < size; i++) {
			AccessibilityNodeInfo node = nodes.get(i);
			// 执行按钮点击行为
			if ("android.widget.TextView".equals(node.getClassName())) {
				if(node.isEnabled()){
					//父类
					times = 0;
					node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
					ENV.stepFlag = 1;
					return;
				}
			}
		}
	}


	//进入转账页面  点击转账到支付宝
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private void step2PerformAction(){
		if(getWindows()==null)
			return;
		if(ENV.stepFlag != 1){
			return;
		}
		List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText("最近");

		if(nodes == null || nodes.size() <= 0){
			return;
		}
		Toast.makeText(this, "page 2", Toast.LENGTH_LONG).show();

		int size = nodes.size();
		for (int i = 0; i < size; i++) {
			AccessibilityNodeInfo node = nodes.get(i);
			// 执行按钮点击行为
			if ("android.widget.TextView".equals(node.getClassName())) {
				if(node.isEnabled()){
					//父类
					times = 0;
					node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
					ENV.stepFlag = 2;
					return;
				}
			}
		}
	}

	//进入转账到支付宝页面  输入对方账户
	private void step3PerformAction(){
		if(getRootInActiveWindow()==null)
			return;
		if(ENV.stepFlag != 2){
			return;
		}
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText("Account");
		if(nodes == null || nodes.size() <= 0){
			return;
		}
		int size = nodes.size();
		for (int i = 0; i < size; i++) {
			AccessibilityNodeInfo node = nodes.get(i);
			if ("android.widget.TextView".equals(node.getClassName())) {
				AccessibilityNodeInfo parent = node.getParent();
				if(parent == null){
					return;
				}
				int childSize = parent.getChildCount();
				if(childSize > 2){
					AccessibilityNodeInfo edit = parent.getChild(1);
					if ("android.widget.EditText".equals(edit.getClassName()) && times < 1) {
						times ++;
						edit.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
						new InputKeysAsyncTask(ENV.col_account).execute();//500381a00ka.cdb@sina.cn
					}
				}
			}
		}
	}

	private static final int INPUT_ACCOUNT_TYPE = 0x001; ///输入支付宝账户
	private static final int INPUT_MONEY_TYPE = 0x002; ///输入打款金额

	/**
	 *
	 * 模拟键盘输入
	 *
	 */
	class InputKeysAsyncTask extends AsyncTask<Void, Void, Void>{

		private String cmdStr;

		public InputKeysAsyncTask(String mCmdStr){
			this.cmdStr = mCmdStr;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			RootShellCmd.getInstance().simulateSomeStrs(cmdStr);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mHandler.sendEmptyMessageDelayed(INPUT_ACCOUNT_TYPE, 2000);
			super.onPostExecute(result);
		}
	}


	/**
	 *
	 * 模拟键盘输入
	 *
	 */
	class InputNumAsyncTask extends AsyncTask<Void, Void, Void>{

//		private int[] keys;
//
//		public InputNumAsyncTask(int[] mKeys){
//			this.keys = mKeys;
//		}

		private String cmdStr;

		public InputNumAsyncTask(String mCmdStr){
			this.cmdStr = mCmdStr;
		}
		//
		@Override
		protected Void doInBackground(Void... arg0) {
//			RootShellCmd.getInstance().simulatePayKeys(keys);
			RootShellCmd.getInstance().simulateSomeStrs(cmdStr);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mHandler.sendEmptyMessageDelayed(INPUT_MONEY_TYPE, 2000);
			super.onPostExecute(result);
		}
	}


	//进入转账到支付宝页面  输入金额
	private void step4PerformAction(){
		if(getRootInActiveWindow()==null)
			return;
		if(ENV.stepFlag != 3){
			return;
		}
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText("Amount");
		int size = nodes.size();
		for (int i = 0; i < size; i++) {
			AccessibilityNodeInfo node = nodes.get(i);
			if ("android.widget.TextView".equals(node.getClassName())) {
				AccessibilityNodeInfo parent = node.getParent();
				if(parent == null){
					return;
				}
				int childSize = parent.getChildCount();
				if(childSize > 5){
					//遍历 找到第一个edittext  输入金额
					for (int j = 0; j < childSize; j ++) {
						AccessibilityNodeInfo pay = parent.getChild(j);
						if("android.widget.EditText".equals(pay.getClassName()) && payInputTimes < 1){
							pay.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
							payInputTimes ++;
							new InputNumAsyncTask(ENV.pay_money).execute();
						}else if(pay.getChildCount() != 0){

							int payChildSize = pay.getChildCount();
							for (int k = 0; k < payChildSize; k ++) {
								AccessibilityNodeInfo payEdit = pay.getChild(k);
								if("android.widget.EditText".equals(payEdit.getClassName()) && payInputTimes < 1){
									payEdit.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
									payInputTimes ++;

									new InputNumAsyncTask(ENV.pay_money).execute();
								}
							}
						}
					}
				}
			}
		}
	}


	@SuppressLint("NewApi")
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event){
//		Toast.makeText(this, "-coming in-" , Toast.LENGTH_LONG).show();
		if (event.getSource() != null) {
			try {

				//转账
				step1PerformAction();
				//选择支付宝转账
				step2PerformAction();
				//输入支付宝账户
				step3PerformAction();
				//输入金额
				step4PerformAction();
				//输入密码
				inputNumberPwdPerformAction();
				//付款成功
				isPaySuccessPerformAction();
				//获取订单号
				getDetailsPerformAction();
				getOrderDetailsPerformAction();
				getOrderIDPerformAction();
			} catch (Exception e) {
//				Toast.makeText(this, e.toString(), 1).show();
			}
		}
	}

	/**
	 * 付款成功
	 */
	private void isPaySuccessPerformAction(){
		if(getRootInActiveWindow()==null)
			return;
		if(ENV.stepFlag != 5){
			return;
		}
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> tab = getRootInActiveWindow().findAccessibilityNodeInfosByText("付款成功");
		if(tab == null || tab.size() <= 0){
			return;
		}
		ENV.stepFlag = 6;
	}


	/**
	 * 点击查看转账详情
	 */
	private void getDetailsPerformAction(){
		if(ENV.stepFlag != 6){
			return;
		}
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> tab = getRootInActiveWindow().findAccessibilityNodeInfosByText("已收到对方转账");//对方已收到转账
		if(tab == null || tab.size() <= 0){
			return;
		}
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText("转账");
		if(nodes == null || nodes.size() <= 0){
			return;
		}

		int size = nodes.size();
		for (int i = 0; i < size; i++) {
			AccessibilityNodeInfo node = nodes.get(i);
			// 执行按钮点击行为
			if ("android.widget.TextView".equals(node.getClassName())) {
				if(node.isEnabled()){
					//父类
					node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
					ENV.stepFlag = 7;
					return;
				}
			}
		}
	}

	/**
	 * 点击详情更多查看订单号相关
	 */
	private void getOrderDetailsPerformAction(){
		if(ENV.stepFlag != 7){
			return;
		}
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> tab = getRootInActiveWindow().findAccessibilityNodeInfosByText("账单详情");
		if(tab == null || tab.size() <= 0){
			return;
		}
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText("更多");
		if(nodes == null || nodes.size() <= 0){
			return;
		}

		int size = nodes.size();
		for (int i = 0; i < size; i++) {
			AccessibilityNodeInfo node = nodes.get(i);
			// 执行按钮点击行为
			if ("android.widget.Button".equals(node.getClassName())) {
				if(node.isEnabled()){
					//父类
					node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					ENV.stepFlag = 8;
					return;
				}
			}
		}
	}

	/**
	 * 查看订单号
	 */
	private void getOrderIDPerformAction(){
		if(ENV.stepFlag != 8){
			return;
		}
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> tab = getRootInActiveWindow().findAccessibilityNodeInfosByText("订单信息");
		if(tab == null || tab.size() <= 0){
			return;
		}
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText("订单号");
		if(nodes == null || nodes.size() <= 0){
			return;
		}
		AccessibilityNodeInfo node = nodes.get(0).getParent();
		if(node != null){
			int childCount = node.getChildCount();
			if(childCount >= 2){
				AccessibilityNodeInfo orderIdNode = node.getChild(1);
				Toast.makeText(this, "--" + orderIdNode.getText().toString(), Toast.LENGTH_LONG).show();
				ENV.stepFlag = 9;

				mHandler.sendEmptyMessageDelayed(0x0012, 1000);
			}
		}
	}

	@Override
	public void onInterrupt() {
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(this, "-我挂了！-", 1).show();
		return super.onUnbind(intent);
	}
}
