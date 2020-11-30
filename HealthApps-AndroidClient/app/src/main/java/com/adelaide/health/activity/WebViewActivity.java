package com.adelaide.health.activity;

import android.widget.RelativeLayout;
import com.adelaide.health.R;
import com.adelaide.health.views.BridgeWebView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;


/**
 * @author Wenping(Deb) Du
 */
public class WebViewActivity extends Activity{
	private  WebView gameWeb;
	private ProgressBar progressbar;
	private RelativeLayout loadingView;
	private String gameUrl = "";//游戏url



    /**
     * 退回到上一级
     */
    public void returnToHome(View v){
    	this.finish();
    }

	private void initView() {
    	Intent inte = getIntent();

    	progressbar = (ProgressBar) findViewById(R.id.web_progress);
    	gameWeb = (WebView) findViewById(R.id.web_view_page);
        loadingView = findViewById(R.id.web_view_loading);

//    	gameUrl = inte.getStringExtra("gameUrl")/* + "?t=" + System.currentTimeMillis()*/;
		gameUrl = "http://10.201.18.213:8080/test/report";
		gameWeb.loadUrl(gameUrl);
		gameWeb.setWebChromeClient(new WebChromeClient());

	}
	
	public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                loadingView.setVisibility(View.GONE);
                progressbar.setVisibility(View.GONE);
            } else {
                loadingView.setVisibility(View.VISIBLE);
                if (progressbar.getVisibility() == View.GONE)
                    progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_web);
    	initView();
    }

}
