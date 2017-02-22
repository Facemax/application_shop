package com.example.face.application_shoptwo.Activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.face.application_shoptwo.Application.MyApplication;
import com.example.face.application_shoptwo.Model.UserBmob;
import com.example.face.application_shoptwo.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_forgetemail;

    private Button btn;
    private ImageButton iv_goback;
    private UserBmob user;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, MyApplication.getBmobId());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget);
        init();

    }
    public void  init(){
        et_forgetemail=(EditText)findViewById(R.id.et_forgetemail);

        iv_goback=(ImageButton)findViewById(R.id.iv_goback);
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
        iv_goback.setOnClickListener(this);
        user=new UserBmob();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                if (!TextUtils.isEmpty(et_forgetemail.getText().toString())){
                    email=et_forgetemail.getText().toString().trim();
                    user.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                Toast.makeText(ForgetActivity.this, "重置密码请求成功，请到"+email+"邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(ForgetActivity.this, "重置密码请求失败", Toast.LENGTH_SHORT).show();
                                Log.i(" ForgetActivity","error:"+e.getMessage());
                                finish();
                            }
                        }
                    });

                }else {
                    Toast.makeText(ForgetActivity.this, "请输入注册邮箱地址", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_goback:
                Snackbar.make(view,"是否返回登录界面",Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("TEST","返回login");
                        finish();
                    }
                }).show();
                break;
        }


    }
}
