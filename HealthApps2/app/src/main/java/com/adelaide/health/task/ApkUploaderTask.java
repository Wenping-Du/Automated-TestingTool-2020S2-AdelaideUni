package com.adelaide.health.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import com.adelaide.health.utils.Contants;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author Wenping(Deb) Du
 */
public class ApkUploaderTask extends AsyncTask<Void, Void, Void> {

    private final String TAG = this.getClass().getName();

    private Context mContext;
    private Handler mHandler;
    private ProgressDialog mDialog;
    private boolean isChecked;
    private String oldFilePath;

    public ApkUploaderTask(Context context, Handler mHandler, String filePath) {
        this.mContext = context;
        this.mHandler = mHandler;
        this.oldFilePath = filePath;
//        this.isChecked = isClickCheck;
        if(isChecked){
            mDialog = new ProgressDialog(mContext);
//            mDialog.setMessage(mContext.getString(R.string.please_wait_boy));
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }
    }

    private class UploadTask implements Runnable{
        @Override
        public void run() {
            try {
                java.net.URL url = new URL(Contants.SERVER_HOST_NAME);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                // 允许Input、Output，不使用Cache
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);

                con.setConnectTimeout(50000);
                con.setReadTimeout(50000);
                // 设置传送的method=POST
                con.setRequestMethod("POST");
                //在一次TCP连接中可以持续发送多份数据而不会断开连接
                con.setRequestProperty("Connection", "Keep-Alive");
                //设置编码
                con.setRequestProperty("Charset", "UTF-8");
                //apk_config/plain能上传纯文本文件的编码格式
                con.setRequestProperty("Content-Type", "application/octet-stream");

                // 设置DataOutputStream
                DataOutputStream ds = new DataOutputStream(con.getOutputStream());

                // 取得文件的FileInputStream
                FileInputStream fStream = new FileInputStream(oldFilePath);
                // 设置每次写入1024bytes
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                int length = -1;
                // 从文件读取数据至缓冲区
                while ((length = fStream.read(buffer)) != -1) {
                    // 将资料写入DataOutputStream中
                    ds.write(buffer, 0, length);
                }
                ds.flush();
                fStream.close();
                ds.close();
                if(con.getResponseCode() == 200){
                    mHandler.sendMessage(mHandler.obtainMessage(200));
                    Log.i("upload", "文件上传成功！上传文件为：" + oldFilePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendMessage(mHandler.obtainMessage(400));
                Log.e("upload", "文件上传失败！上传文件为：" + oldFilePath);
                Log.e("upload", "报错信息toString：" + e.toString());
            }
        }

    }

    @Override
    protected Void doInBackground(Void... params) {
        new UploadTask().run();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(isChecked && mDialog != null){
            mDialog.dismiss();
        }
        super.onPostExecute(result);
    }


}
