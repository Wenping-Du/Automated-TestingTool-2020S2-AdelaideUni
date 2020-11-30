package com.adelaide.health.views;

import android.util.Log;

/**
 * @author Wenping(Deb) Du
 */
public class DefaultHandler implements BridgeHandler{

	String TAG = "DefaultHandler";
	
	@Override
	public void handler(String data, CallBackFunction function) {
		Log.i(TAG, "receive data" + data);
		if(function != null){
			function.onCallBack("DefaultHandler response data");
		}
	}

}
