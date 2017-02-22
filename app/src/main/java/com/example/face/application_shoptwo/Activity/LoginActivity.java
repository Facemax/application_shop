package com.example.face.application_shoptwo.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.face.application_shoptwo.Application.MyApplication;
import com.example.face.application_shoptwo.Model.UserBmob;
import com.example.face.application_shoptwo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private EditText log_et_user_name, log_et_user_password;
    private TextView tv_forget;
    private CheckBox cb_remember_login,cb_remember;
    private Button log_btn_login,log_btn_resign;
    private SharedPreferences sp;
    private String user_name,user_password;
    private UserBmob user;
    private  BmobFile photo;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, MyApplication.getBmobId());
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        IsCheckBox();
    }
    public void init(){
        log_et_user_name=(EditText)findViewById(R.id.log_et_name);
        log_et_user_password=(EditText)findViewById(R.id.log_et_password);
        tv_forget=(TextView)findViewById(R.id.tv_forget);
        cb_remember=(CheckBox)findViewById(R.id.cb_remember);
        cb_remember_login=(CheckBox)findViewById(R.id.cb_remember_login);
        cb_remember.setOnCheckedChangeListener(this);
        cb_remember_login.setOnCheckedChangeListener(this);
        log_btn_login=(Button)findViewById(R.id.log_btn_login);
        log_btn_resign=(Button)findViewById(R.id.log_btn_resign);
        log_btn_login.setOnClickListener(this);
        log_btn_resign.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        sp = this.getSharedPreferences("userInfo",MODE_PRIVATE);//只能给本程序读取


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_btn_login:
                //Login();
                Login_email();
                Log.e("loginActivity","登录");
                break;
            case R.id.log_btn_resign:
                Intent intent=new Intent(this,ResignActivity.class);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                Log.e("loginActivity","注册");
                break;
            case R.id.tv_forget:
                Intent intent1=new Intent(this,ForgetActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                Log.e("loginActivity","忘记密码");
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean ischeck) {
        switch (compoundButton.getId()) {
            case R.id.cb_remember:
                if (cb_remember.isChecked()){
                    sp.edit().putBoolean("ISCHECK", true).commit();
                    Log.e("loginActivity","记住密码多选框已被选中");
                }else {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER_NAME","");
                    editor.putString("PASSWORD","");

                    editor.commit();
                    sp.edit().putBoolean("ISCHECK", false).commit();
                    Log.e("loginActivity","记住密码多选框无选中");
                }
                Log.e("loginActivity","记住密码");
                break;
            case R.id.cb_remember_login:
                if (cb_remember_login.isChecked()){
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
                    Log.e("loginActivity","自动登录多选框已被选中");
                }else {
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                    Log.e("loginActivity","自动登录多选框无选中");
                }
                Log.e("loginActivity","自动登录");
                break;

        }

    }
    public void Login(){
        user_name=log_et_user_name.getText().toString().trim();
        user_password=log_et_user_password.getText().toString().trim();
        if (!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_password))
        {
            user = new UserBmob();
            user.setUsername(user_name);
            user.setPassword(user_password);
            user.login(new SaveListener<UserBmob>() {
                @Override
                public void done(UserBmob userBmob, BmobException e) {
                    if(e==null){
                        if (userBmob.getEmailVerified()){
                        //记住用户名和密码
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USER_NAME",user_name);
                        editor.putString("PASSWORD",user_password);

                        editor.commit();
                        user = BmobUser.getCurrentUser(UserBmob.class);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("pwd",user_password);
                        startActivity(intent);
                        LoginActivity.this.finish();
                        //登录成功
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Log.e("test",""+user.getUsername());
                        Log.e("loginActivity","登录成功");
                        }else {
                            Toast.makeText(LoginActivity.this, "注册电子邮箱未验证", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        Log.e("loginActivity","登录失败"+e.getMessage());
                    }
                }
            });
        }else {
            Toast.makeText(LoginActivity.this, "用户名与密码必须填写", Toast.LENGTH_SHORT).show();
        }
    }
    public void Login_email(){
         progressDialog=new ProgressDialog(this,R.style.dialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在登录，请稍后...");
        progressDialog.show();
        user_name=log_et_user_name.getText().toString().trim();
        user_password=log_et_user_password.getText().toString().trim();
        if (!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_password))
        {
            user = new UserBmob();
            user.loginByAccount(user_name, user_password, new LogInListener<UserBmob>() {
                @Override
                public void done(UserBmob userBmob, BmobException e) {
                    if(userBmob!=null){
                        if(userBmob.getEmailVerified()){
                            progressDialog.dismiss();
                        //记住用户名和密码
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USER_NAME",user_name);
                        editor.putString("PASSWORD",user_password);
                        editor.putString("objectId",userBmob.getObjectId());
                        editor.commit();
                        user = BmobUser.getCurrentUser(UserBmob.class);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("pwd",user_password);
                        startActivity(intent);
                        LoginActivity.this.finish();
                        //登录成功
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Log.e("test",""+user.getUsername());
                        Log.e("loginActivity","登录成功");
                        }else {
                            Toast.makeText(LoginActivity.this, "注册电子邮箱未验证", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        Log.e("loginActivity","登录失败"+e.getMessage());
                    }
                }
            });

        }else {
            Toast.makeText(LoginActivity.this, "用户名与密码必须填写", Toast.LENGTH_SHORT).show();
        }
    }
    public void IsCheckBox(){

            if (sp.getBoolean("ISCHECK",false)) {
                //设置默认是记录密码状态
                cb_remember.setChecked(true);
                log_et_user_name.setText(sp.getString("USER_NAME", ""));
                log_et_user_password.setText(sp.getString("PASSWORD",""));
                log_et_user_name.setSelection(log_et_user_name.length());
                log_et_user_name.setGravity(Gravity.CENTER);
                log_et_user_password.setGravity(Gravity.CENTER);
                setheadphoto(sp.getString("objectId", ""));
                if (sp.getBoolean("AUTO_ISCHECK",false)){
                    cb_remember_login.setChecked(true);
                    Login_email();
                }else {
                    cb_remember_login.setChecked(false);
                }



            }else {
                cb_remember.setChecked(false);
                log_et_user_name.setText(sp.getString("USER_NAME", ""));
                log_et_user_name.setSelection(log_et_user_name.length());
                log_et_user_name.setGravity(Gravity.CENTER);

            }


    }
    public void setheadphoto(String objectId){
        BmobQuery<UserBmob> query = new BmobQuery<UserBmob>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(new FindListener<UserBmob>() {
            @Override
            public void done(List<UserBmob> list, BmobException e) {
                if (e==null){
                    Log.e("test","sucess");
                    for (UserBmob user:list){
                        photo=user.getHeadphoto();
                        String s=photo.getFileUrl();
                        Log.e("test","sucess"+s);
                    }
                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.ic_launcher)   //加载中显示的正在加载中的图片
                            .showImageOnFail(R.mipmap.ic_launcher)      //为了方便，加载失败也显示加载中的图片
                            .cacheInMemory(true)                      //在内存中缓存图片
                            .cacheOnDisk(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .imageScaleType(ImageScaleType.EXACTLY)   //设置图片以如何的编码方式显示
                            .build();
                    //异步加载图片，并渲染到指定的控件上
                    try {

                        ImageLoader.getInstance().displayImage(""+photo.getFileUrl(), (ImageView) findViewById(R.id.iv_head), options);
                    }catch (NullPointerException e1){
                        Log.e("test","error1"+e1);
                    }

                }else {
                    Log.e("test","error"+e);
                }
            }
        });
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:
                if (resultCode==1){
                    String name=data.getStringExtra("name");
                    String password=data.getStringExtra("change_password");
                    Log.e("test","从注册界面带数据返回登录界面name:"+name);
                    Log.e("test","从注册界面带数据返回登录界面password:"+password);
                    setheadphoto(name);

                    log_et_user_name.setText(name);
                    log_et_user_password.setText(password);
                    Log.e("test","从注册界面带数据返回登录界面");
                }
                break;
        }

    }

}
