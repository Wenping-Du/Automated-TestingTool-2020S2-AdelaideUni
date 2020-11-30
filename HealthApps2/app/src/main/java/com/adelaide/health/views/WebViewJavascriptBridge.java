package com.adelaide.health.views;

/**
 * @author Wenping(Deb) Du
 */
public interface WebViewJavascriptBridge {
	
	public void send(String data);
	public void send(String data, CallBackFunction responseCallback);
	
}
