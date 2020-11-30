package com.adelaide.health.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;
import com.adelaide.health.R;
import com.adelaide.health.adapter.ListAdapter;
import com.adelaide.health.bean.APKFile;
import com.adelaide.health.task.ApkUploaderTask;
import com.adelaide.health.utils.Contants;
import com.adelaide.health.utils.MD5Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Wenping(Deb) Du
 */

public class MainActivity extends Activity implements ListAdapter.MyAdapterDelegate{
    private ListAdapter mAdapter;
    private ListView mListView;
    private ArrayList<APKFile> mApkList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.main_list);
        mAdapter = new ListAdapter(this, this);
        mListView.setAdapter(mAdapter);
        //use thread to refresh the list
//        boolean isMounted = Environment.getExternalStorageState().equalsIgnoreCase("MOUNTED");
//        mApkList = Contants.getApkFiles(this, Environment.getRootDirectory());
        mApkList = Contants.getLocalApkFiles();
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 200) {
                Toast.makeText(MainActivity.this, "upload success", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 400) {
                Toast.makeText(MainActivity.this, "upload failed", Toast.LENGTH_SHORT).show();
            }
        };
    };

    @Override
    public int getCount() {
        if(mApkList != null){
            return mApkList.size();
        }
        return 0;
    }

    @Override
    public void render(ImageView icon, ImageView flag, TextView title, TextView button, int paramInt) {
        if(paramInt == 13){
            flag.setVisibility(View.VISIBLE);
        }else{
            flag.setVisibility(View.INVISIBLE);
        }
        title.setText(mApkList.get(paramInt).getName());
    }

    @Override
    public void getChecked(final int position) {
//        String path = mApkList.get(position).getPath();
//        //求hashcode
//        try {
//            String MD5String = MD5Utils.getMD5Checksum(path);
//            Toast.makeText(this, MD5String, Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
//        }
//        Json request
        requestHTTP(mApkList.get(position).getHashcode());
        //File upload
//        new ApkUploaderTask(this, mHandler, path).execute();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(position == 13){
//                    Toast.makeText(MainActivity.this, "Server already has the report!", Toast.LENGTH_LONG).show();
//                }else{
//                    Toast.makeText(MainActivity.this, "Send success! Server begins to detect!", Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, 1000L);
    }



    @Override
    public void getItemChecked(final int position) {
        if(position == 13){
            Intent i = new Intent(this, WebViewActivity.class);
            startActivity(i);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "No detection for this Apk file!", Toast.LENGTH_LONG).show();
                }
            }, 1000L);
        }
    }


    @Override
    public void getCover(ImageView icon, int position) {

    }

    /**
     * http request
     */
    private AsyncHttpClient mClient;
    private void requestHTTP(String hashCode) {
        mClient = Contants.getHttpClient(this);
        String url = Contants.SERVER_HOST_NAME + "?hashcode=" + hashCode;
        mClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String res = new String(bytes);
                    String arrs[] = res.split("body>");
                    String body = arrs[1].replace("\\r\\n", "").replace("</", "");
                    Toast.makeText(MainActivity.this, body, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "You have sent the apk to detection!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

    }
}
