package com.example.face.application_shoptwo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Face on 2016/11/2.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity",getClass().getSimpleName());
        CollectorActivity.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CollectorActivity.removeActivity(this);
    }
}
