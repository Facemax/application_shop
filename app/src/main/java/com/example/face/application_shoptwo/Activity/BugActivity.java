package com.example.face.application_shoptwo.Activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.face.application_shoptwo.Application.MyApplication;
import com.example.face.application_shoptwo.Model.Bug_Bmob;
import com.example.face.application_shoptwo.Model.UserBmob;
import com.example.face.application_shoptwo.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class BugActivity extends BaseActivity {
    String type;
    String context;
    BmobUser user;
    EditText edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug);
        Bmob.initialize(this, MyApplication.getBmobId());
        init();

    }
    public  void  init(){
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         user= BmobUser.getCurrentUser(UserBmob.class);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.btn);
        edittext=(EditText)findViewById(R.id.et_text);
        final Spinner spinner= (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        type="意见";
                        Log.e("type","1"+type);
                        break;
                    case 2:
                        type="BUG";
                        Log.e("type","2"+type);
                        break;
                    case 3:
                        type="其他";
                        Log.e("","2"+type);
                        break;
                }
                if(spinner.getSelectedItem().toString().equals("建议")){
                    type="建议";
                    Log.e("建议","0"+type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.goback);//在这才能修改NavigationIcon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("finish","ssss");
                finish();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               send();


            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.send:
                send();
                        Log.e("send","ssss");
                }
                return false;
            }

        });
        collapsingToolbarLayout.setTitle("应用反馈");
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);////

    }
    public void send(){
        if (!TextUtils.isEmpty(edittext.getText().toString())){
            context=edittext.getText().toString().trim();
            Bug_Bmob bug_bmob=new Bug_Bmob();
            bug_bmob.setUsername(user.getUsername());
            bug_bmob.setType(type);
            bug_bmob.setContext(context);
            bug_bmob.setUseremail(user.getEmail());
            bug_bmob.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        Toast.makeText(BugActivity.this, "反馈成功",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(BugActivity.this, "反馈失败",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            Toast.makeText(BugActivity.this, "请输入内容",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为toolbar创建Menu
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }
}
