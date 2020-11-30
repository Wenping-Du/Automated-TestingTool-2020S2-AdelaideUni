package com.adelaide.autotest;
/**
 * @author Wenping(Deb) Du
 */

import java.util.HashMap;
import java.util.Map;

import android.view.KeyEvent;

public class ENV {
	/**
	 * keycode 值列举
	 */
	public static int[] KEYCODE_INTEGER = {
			KeyEvent.KEYCODE_0,
			KeyEvent.KEYCODE_1,
			KeyEvent.KEYCODE_2,
			KeyEvent.KEYCODE_3,
			KeyEvent.KEYCODE_4,
			KeyEvent.KEYCODE_5,
			KeyEvent.KEYCODE_6,
			KeyEvent.KEYCODE_7,
			KeyEvent.KEYCODE_8,
			KeyEvent.KEYCODE_9,

			KeyEvent.KEYCODE_A,
			KeyEvent.KEYCODE_B,
			KeyEvent.KEYCODE_C,
			KeyEvent.KEYCODE_D,
			KeyEvent.KEYCODE_E,
			KeyEvent.KEYCODE_F,
			KeyEvent.KEYCODE_G,
			KeyEvent.KEYCODE_H,
			KeyEvent.KEYCODE_I,
			KeyEvent.KEYCODE_J,
			KeyEvent.KEYCODE_K,
			KeyEvent.KEYCODE_L,
			KeyEvent.KEYCODE_M,
			KeyEvent.KEYCODE_N,
			KeyEvent.KEYCODE_O,
			KeyEvent.KEYCODE_P,
			KeyEvent.KEYCODE_Q,
			KeyEvent.KEYCODE_R,
			KeyEvent.KEYCODE_S,
			KeyEvent.KEYCODE_T,
			KeyEvent.KEYCODE_U,
			KeyEvent.KEYCODE_V,
			KeyEvent.KEYCODE_W,
			KeyEvent.KEYCODE_X,
			KeyEvent.KEYCODE_Y,
			KeyEvent.KEYCODE_Z,

			KeyEvent.KEYCODE_A,
			KeyEvent.KEYCODE_B,
			KeyEvent.KEYCODE_C,
			KeyEvent.KEYCODE_D,
			KeyEvent.KEYCODE_E,
			KeyEvent.KEYCODE_F,
			KeyEvent.KEYCODE_G,
			KeyEvent.KEYCODE_H,
			KeyEvent.KEYCODE_I,
			KeyEvent.KEYCODE_J,
			KeyEvent.KEYCODE_K,
			KeyEvent.KEYCODE_L,
			KeyEvent.KEYCODE_M,
			KeyEvent.KEYCODE_N,
			KeyEvent.KEYCODE_O,
			KeyEvent.KEYCODE_P,
			KeyEvent.KEYCODE_Q,
			KeyEvent.KEYCODE_R,
			KeyEvent.KEYCODE_S,
			KeyEvent.KEYCODE_T,
			KeyEvent.KEYCODE_U,
			KeyEvent.KEYCODE_V,
			KeyEvent.KEYCODE_W,
			KeyEvent.KEYCODE_X,
			KeyEvent.KEYCODE_Y,
			KeyEvent.KEYCODE_Z,
			KeyEvent.KEYCODE_AT,
			KeyEvent.KEYCODE_PERIOD,
	};

	/**
	 * 可以输入的keycode
	 */
	public static char[] KEYCODE_CHAR = {
			'0','1','2','3','4','5','6','7','8','9',
			'a','b','c','d','e','f','g',
			'h','i','j','k','l','m','n',
			'o','p','q','r','s','t',
			'u','v','w','x','y','z',

			'A','B','C','D','E','F','G',
			'H','I','J','K','L','M','N',
			'O','P','Q','R','S','T',
			'U','V','W','X','Y','Z',
			'@','.',
	};



	public static String[] PAY_ALIPAYACCOUNTS = {"dwpyy@qq.com", "2851820609@qq.com",};
	public static String[] PAY_ALIPAYPSW = {"890523","yd5252"};


	//keycode键值
	private static Map<String, Integer> keycodeMap;

	public static void initKeyCodeHashMap(){
		keycodeMap = new HashMap<String, Integer>();

		int keySize = KEYCODE_INTEGER.length;
		for(int i = 0; i < keySize; i++){
			keycodeMap.put(KEYCODE_CHAR[i] + "", KEYCODE_INTEGER[i]);
		}
	}

	/**
	 * 通过char获取系统keycode的值
	 * @param charStr
	 * @return
	 */
	public static int getKeyCodeInteger(String charStr){
		int keycode = -10000;
		if(keycodeMap != null && !keycodeMap.isEmpty()){
			keycode = keycodeMap.get(charStr);
		}
		return keycode;
	}

	/**
	 * 通过字符串获取keycode 数组
	 * @param charStrs
	 * @return
	 */
	public static int[] getIntegersFromChar(String charStrs){
		int len = charStrs.length();
		int keycodes[] = new int[len];
		char[] keys = charStrs.toCharArray();
		for (int i = 0; i < len; i ++) {
			keycodes[i] = keycodeMap.get(keys[i] + "");
		}
		return keycodes;
	}

	//accounts map
	private static Map<String, String> accountMap;

	/**
	 * 初始化付款账户数组
	 */
	public static void initAccountHashMap(){
		accountMap = new HashMap<String, String>();

		int keySize = PAY_ALIPAYACCOUNTS.length;
		for(int i = 0; i < keySize; i++){
			accountMap.put(PAY_ALIPAYACCOUNTS[i] + "", PAY_ALIPAYPSW[i]);
		}
	}

	/**
	 * 通过账户获取密码
	 * @param charStr
	 * @return
	 */
	public static String getAccountPsw(String charStr){
		if(accountMap != null && !accountMap.isEmpty()){
			return accountMap.get(charStr);
		}
		return "";
	}


	//收款信息
	public static String col_account = "";//收款账户
	public static String pay_money = "";//收款金额
	public static String pay_psw = "";//付款密码

	public static int stepFlag = -1;//step

	//取单url
	public static final String TASK_URL = "https://172.16.20.104/invoke";
	//报告url
	public static final String REPORT_URL = "https://172.16.20.104/callback";
	//日志url
	public static final String LOG_URL = "https://172.16.20.104/log";
}
