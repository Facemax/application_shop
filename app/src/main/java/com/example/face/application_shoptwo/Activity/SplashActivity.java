package com.example.face.application_shoptwo.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.face.application_shoptwo.R;

public class SplashActivity extends AppCompatActivity {
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startLogin_Avtivity();
    }
    private void startLogin_Avtivity() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                intent=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();//结束本Activity
            }
        }, 2500);//设置执行时间
    }
}
