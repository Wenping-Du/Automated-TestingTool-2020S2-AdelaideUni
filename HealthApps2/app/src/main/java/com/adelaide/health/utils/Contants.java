package com.adelaide.health.utils;

import android.content.Context;
import com.adelaide.health.bean.APKFile;
import com.loopj.android.http.AsyncHttpClient;

import java.io.File;
import java.util.ArrayList;


/**
 * @author Wenping(Deb) Du
 */
public class Contants {
    public static final String FILE_TYPE = ".apk";
    public static final String SERVER_HOST_NAME = "http://10.201.18.213:8080/test/findByHashcode";

    private static AsyncHttpClient httpClient = null;
    public static synchronized AsyncHttpClient getHttpClient(Context mContext) {
        if(httpClient == null) {
            httpClient = new AsyncHttpClient();
        }
        httpClient.setTimeout(20000);
        return httpClient;
    }

    public static final String[] APK_FILE_NAMES = {
                                                "aarogya-setu_1.4.1.apk",
                                                "ContactTracer.apk",
                                                "1321.apk",
                                                "Smitte_stop1.0.3.apk",
                                                "StopCovid 1.1.2.apk",
                                                "test1.apk",
                                                "Contact Tracing 1.3.8 (1).apk",
                                                "Contact Tracing 1.3.8.apk",
                                                "Coronavirus 1.0.2.apk",
                                                "nhs-covid-19_3.0.0+%2835%29.apk",
                                                "corona-warn-app_1.2.1.apk",
                                                "COVID Alert 1.0.3.apk",
                                                "ehteraz_9.0.2.apk",
                                                "sg.ndi.sp_55_apps.evozi.com.apk",
                                                "sg.gov.tech.bluetrace_38.apk",
                                                "COVIDSafe 1.0.11.apk",
                                                "Contact Tracer 2.0.2.apk",
                                                "latest.apk",
                                                "PrivateKit 0.5.19.apk",
                                                "SwissCovid1.0.8.apk"};

    public static final String[] APK_FILE_HASHCODE = {
                                                "5ef195fdab52342564f5e12b5b5e3c03",
                                                "7bc9b17e4d8b210ecb34de17d1805222",
                                                "400dbc9a036b44a6ca802f978d99ddd2",
                                                "6a0c9753acef2580ff7248a279db14d1",
                                                "96043f1c69d645f6174d8556c2043c66",
                                                "257a388e669ab11ed2ca0277bcc27410",
                                                "b5254ec24a8d48cb5f3be60c00145587",
                                                "b5254ec24a8d48cb5f3be60c00145587",
                                                "1c26a4bb5b5cfc7b2967eedf9173d719",
                                                "34da20b74c36f2ba1b82f4f892b29212",
                                                "82526dc11896d26147d11dbb333cfd0d",
                                                "112e17392cc2a4766b5c112b9f56c8f7",
                                                "45e833d94c2a65cec169b42c96a8613a",
                                                "e29394860612f0afeb05343b68de17ce",
                                                "9779130bccaa759cb5bea80b38048e43",
                                                "9b1987abd1168d08c128b88594851704",
                                                "7bc9b17e4d8b210ecb34de17d1805222",
                                                "476da2e132fc9ecd7b25b41d74540fe6",
                                                "cb20ab20d578fdb2429c2063372cadb9",
                                                "85d17b6bfc80f7d704779ca2bad18841"};

    public static ArrayList<APKFile> getLocalApkFiles(){
        apks.clear();
        for (int i = 0;i < APK_FILE_NAMES.length; i ++){
            APKFile file = new APKFile();
            file.setHashcode(APK_FILE_HASHCODE[i]);
            file.setName(APK_FILE_NAMES[i]);
            apks.add(file);
        }

        return apks;
    }

    /**
     * to get apk file list
     */
    private static ArrayList<APKFile> apks = new ArrayList<>();
    public static ArrayList<APKFile> getApkFiles(Context mContext, File targetFile){
        if(targetFile.isDirectory()){
            File[] fa = targetFile.listFiles();
            if(fa!= null && fa.length > 0){

                for (File file : fa) {
                    // file: ends with .apk
                    if(file.isFile() && file.getName().endsWith(FILE_TYPE)){
                        APKFile a = new APKFile();
                        a.setPath(file.getAbsolutePath());
                        a.setName(file.getName());
                        apks.add(a);
                    }else{
                        //folder: go on to seek .apk
                        getApkFiles(mContext, file);
                    }
                }
            }
        }
        return apks;
    }
}
