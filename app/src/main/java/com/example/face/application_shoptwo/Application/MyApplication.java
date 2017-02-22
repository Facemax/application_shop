package com.example.face.application_shoptwo.Application;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Face on 2016/11/3.
 */

public class MyApplication extends Application{
   private static Context context;
    private final static String BMOB_ID = "5a8538b48fbb8ea5e34b6739c5a7147d";//Bmob后端云的ID
    public static final String APPKEY ="74f1f965183ecfd8d82368b7bd3eb0f4";//Juhe配置您申请的KEY

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        context=getApplicationContext();

        super.onCreate();
    }

    public static String getBmobId() {
        return BMOB_ID;
    }

    public static String getAPPKEY() {
        return APPKEY;
    }
}

