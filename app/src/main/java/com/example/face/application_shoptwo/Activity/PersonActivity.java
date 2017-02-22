package com.example.face.application_shoptwo.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.face.application_shoptwo.Application.MyApplication;
import com.example.face.application_shoptwo.Model.Collect_Bmob;
import com.example.face.application_shoptwo.Model.History;
import com.example.face.application_shoptwo.Model.History_Bmob;
import com.example.face.application_shoptwo.Model.UserBmob;
import com.example.face.application_shoptwo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PersonActivity extends BaseActivity implements View.OnClickListener{
    private UserBmob user;
    private BmobFile iv_photo;
    private Dialog pwd_Dialog,email_Dialog,message_Dialog;
    private  BmobFile photo;
    private String pwd,newpwd,newpwds,change_pwd,change_email,change_message;
    private TextView tv_username,tv_num;
    private LinearLayout line1,line2,line3;
    private Button btn;
    private ImageView headphoto;
    private TextView tv_history,tv_collect;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),500 X 500的正方形。
    private static int output_X = 500;
    private static int output_Y = 500;
    private Bitmap BM_photo;
    private MyReceiver receiver;
    private MyReceivers receivers;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__personactivity);
        Bmob.initialize(this, MyApplication.getBmobId());
        user = BmobUser.getCurrentUser(UserBmob.class);
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        init();
    }
    public void init(){
        tv_username=(TextView)findViewById(R.id.tv_username);
        tv_num=(TextView)findViewById(R.id.tv_num);
        tv_collect=(TextView)findViewById(R.id.tv_collect);
        tv_history=(TextView)findViewById(R.id.tv_history);
        tv_username.setText(user.getUsername());//设置用户名
        Number();
        CollectNumber();
        setheadphoto(user.getUsername());
        headphoto=(ImageView)findViewById(R.id.iv_head);
        registerForContextMenu(headphoto);//注册上下文菜单
        line1=(LinearLayout)findViewById(R.id.line1);
        line2=(LinearLayout)findViewById(R.id.line2);
        line3=(LinearLayout)findViewById(R.id.line3);
        btn=(Button)findViewById(R.id.btn);
        line1.setOnClickListener(this);
        line2.setOnClickListener(this);
        line3.setOnClickListener(this);
        btn.setOnClickListener(this);
        //注册接收器
        receiver= new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.updatenumber");
        PersonActivity.this.registerReceiver(receiver,filter);
        //注册接收器
        receivers= new MyReceivers();
        IntentFilter filters=new IntentFilter();
        filters.addAction("android.intent.action.updatecollect");
        PersonActivity.this.registerReceiver(receivers,filters);
        sp = this.getSharedPreferences("userInfo",MODE_PRIVATE);//只能给本程序读取

    }
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("PersonActivity","onReceive start  Number()");
            Number();
            Log.e("PersonActivity","onReceive success  Number()");

        }
    }
    public class MyReceivers extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("PersonActivity","onReceives start CollectNumber()");
            CollectNumber();

            Log.e("PersonActivity","onReceives success CollectNumber()");

        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.line1:
                change_password();
                Log.e("PersonActivity","修改密码");
                break;
            case R.id.line2:
                change_email();
                Log.e("PersonActivity","修改邮箱");
                break;
            case R.id.line3:
                change_message();
                Log.e("PersonActivity","修改信息");
                break;
            case R.id.btn:
                Intent intent=new Intent();
                intent.setClass(PersonActivity.this,LoginActivity.class);
                sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                startActivity(intent);
                finish();
                Log.e("PersonActivity","退出登录");
                break;
        }
    }
    public void CollectNumber(){
            BmobQuery<Collect_Bmob> bmobQuery=new BmobQuery<Collect_Bmob>();
        bmobQuery.addWhereEqualTo("username",user.getUsername());
        bmobQuery.setLimit(100);
        bmobQuery.findObjects(new FindListener<Collect_Bmob>() {
            @Override
            public void done(List<Collect_Bmob> list, BmobException e) {
                if (e==null){
                    for (Collect_Bmob collect_bmob:list){
                        tv_collect.setText(""+list.size());
                    }
                    Log.e("CollectNumber","success"+list.size());
                }else{
                    Log.e("Getbmob_collect","查询失败"+e.getMessage());
                }
            }
        });
    }
    public void change_message(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout mLayout = (LinearLayout) inflater.inflate(R.layout.person_message_content, null);
        message_Dialog = new Dialog(PersonActivity.this);
        message_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        message_Dialog.addContentView(mLayout, new LinearLayout.LayoutParams(-3,-3));
        message_Dialog.show();
        final EditText et_newmessage= (EditText) message_Dialog.getWindow().findViewById(R.id.et_newmessage);
        TextView tv_message=(TextView)message_Dialog.getWindow().findViewById(R.id.tv_message);
        Log.e("PersonActivity","oldmessage:"+user.getMessage());

        tv_message.setText(user.getMessage());
        message_Dialog.getWindow().findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("PersonActivity","change_message()-start");
                if (TextUtils.isEmpty(et_newmessage.getText().toString())){
                    Toast.makeText(PersonActivity.this, "请输入您的个性签名",Toast.LENGTH_LONG).show();
                }else {
                    change_message=et_newmessage.getText().toString().trim();
                    final UserBmob userBmob=new UserBmob();
                    userBmob.setMessage(change_message);
                    userBmob.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Intent intent=new Intent();
                                intent.setAction("android.intent.action.update");
                                sendBroadcast(intent);
                                user.setMessage(userBmob.getMessage());
                                Toast.makeText(PersonActivity.this, "修改个性签名成功",Toast.LENGTH_LONG).show();
                            }else {

                                Toast.makeText(PersonActivity.this, "修改个性签名失败",Toast.LENGTH_LONG).show();
                                Log.e("PersonActivity","change_message()-error:"+e.getMessage());
                            }
                        }
                    });
                    /*user.setMessage(change_message);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Intent intent=new Intent();
                                intent.setAction("android.intent.action.update");
                                sendBroadcast(intent);
                                Toast.makeText(PersonActivity.this, "修改个性签名成功",Toast.LENGTH_LONG).show();
                            }else {

                                Toast.makeText(PersonActivity.this, "修改个性签名失败",Toast.LENGTH_LONG).show();
                                Log.e("PersonActivity","change_message()-error:"+e.getMessage());
                            }
                        }
                    });*/
                }
                Log.e("PersonActivity","change_message()-success");
                message_Dialog.dismiss();
            }
        });
    }
    public void change_email(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout mLayout = (LinearLayout) inflater.inflate(R.layout.person_email_content, null);
        email_Dialog = new Dialog(PersonActivity.this);
        email_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        email_Dialog.addContentView(mLayout, new LinearLayout.LayoutParams(-3,-3));
        email_Dialog.show();
        final EditText et_newemail= (EditText) email_Dialog.getWindow().findViewById(R.id.et_newemail);
        TextView tv_mail=(TextView)email_Dialog.getWindow().findViewById(R.id.tv_email);
        Log.e("PersonActivity","oldemail:"+user.getEmail());
        tv_mail.setText(user.getEmail());
        email_Dialog.getWindow().findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("PersonActivity","change_email()-start");

                if (TextUtils.isEmpty(et_newemail.getText().toString())){
                    Toast.makeText(PersonActivity.this, "请输入您的电子邮箱",Toast.LENGTH_LONG).show();
                }else {
                    change_email=et_newemail.getText().toString().trim();
                    final UserBmob userBmob=new UserBmob();

                    userBmob.setEmail(change_email);
                    userBmob.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                user.setEmail(userBmob.getEmail());
                                Toast.makeText(PersonActivity.this, "修改电子邮箱成功,请重新登录",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent();
                                intent.setClass(PersonActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(PersonActivity.this, "修改电子邮箱失败",Toast.LENGTH_LONG).show();
                                Log.e("PersonActivity","change_email()-error:"+e.getMessage());
                            }
                        }
                    });

                }
                Log.e("PersonActivity","change_email()-succes");
                email_Dialog.dismiss();
            }
        });

    }
    public void change_password(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        RelativeLayout mLayout = (RelativeLayout) inflater.inflate(R.layout.person_pwd_content, null);
         pwd_Dialog = new Dialog(PersonActivity.this);
        pwd_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pwd_Dialog.addContentView(mLayout, new LinearLayout.LayoutParams(-3,-3));
        pwd_Dialog.show();
        final EditText et_pwd= (EditText) pwd_Dialog.getWindow().findViewById(R.id.pwd);
        final EditText et_newpwd= (EditText) pwd_Dialog.getWindow().findViewById(R.id.newpwd);
        final EditText et_newpwds= (EditText) pwd_Dialog.getWindow().findViewById(R.id.newpwds);
        pwd_Dialog.getWindow().findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.e("PersonActivity","change_password()-start");
                if (TextUtils.isEmpty(et_pwd.getText().toString())){
                    Toast.makeText(PersonActivity.this, "请输入您的旧密码",Toast.LENGTH_LONG).show();

                }else {
                    pwd=et_pwd.getText().toString().trim();
                    Log.e("PersonActivity","pwd"+pwd);
                }
                if (TextUtils.isEmpty(et_newpwd.getText().toString())){
                    Toast.makeText(PersonActivity.this, "请输入您的新密码",Toast.LENGTH_LONG).show();

                }else {
                    newpwd=et_newpwd.getText().toString().trim();
                    Log.e("PersonActivity","newpwd"+newpwd);
                }
                if (TextUtils.isEmpty(et_newpwds.getText().toString())){
                    Toast.makeText(PersonActivity.this, "请再次输入您的新密码",Toast.LENGTH_LONG).show();

                }else {
                    newpwds=et_newpwds.getText().toString().trim();
                    Log.e("PersonActivity","newpwds"+newpwds);
                }
                if (!TextUtils.isEmpty(newpwd)&&!TextUtils.isEmpty(newpwds)){
                    if (newpwd.equals(newpwds)){
                        change_pwd=newpwd;
                        Log.e("PersonActivity","change_pwd"+change_pwd);
                        user.updateCurrentUserPassword(pwd, change_pwd, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    Toast.makeText(PersonActivity.this, "修改密码成功,请重新登录",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent();
                                    intent.setClass(PersonActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(PersonActivity.this, "修改密码失败",Toast.LENGTH_LONG).show();
                                    Log.e("PersonActivity","error"+e.getMessage());
                                }
                            }
                        });

                    }else {
                        Toast.makeText(PersonActivity.this, "两次输入新密码不一致，请重新输入",Toast.LENGTH_LONG).show();
                    }
                }
                Log.e("PersonActivity","change_password()-succes");
            }
        });

    }
    public void Number(){
        BmobQuery<History_Bmob> bmobQuery = new BmobQuery<History_Bmob>();
        bmobQuery.addWhereEqualTo("username",user.getUsername());
        bmobQuery.setLimit(100);
        bmobQuery.findObjects(new FindListener<History_Bmob>() {
            @Override
            public void done(List<History_Bmob> list, BmobException e) {
                if (e==null){
                    for (History_Bmob historyBmob:list){
                        Log.e("Number","success"+list.size());
                        int i=list.size()*2+1;
                        tv_num.setText(""+i);
                        tv_history.setText(""+list.size());
                    }
                }else{
                    Log.e("Getbmob_history","查询失败"+e.getMessage());
                }
            }
        });


    }
    public void setheadphoto(final String username){
        BmobQuery<UserBmob> query = new BmobQuery<UserBmob>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<UserBmob>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)//把背景去掉
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
                    ImageLoader.getInstance().displayImage(""+photo.getFileUrl(), (ImageView) findViewById(R.id.iv_head), options);
                }else {
                    Log.e("test","error"+e);
                }
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=new MenuInflater(PersonActivity.this);
        inflater.inflate(R.menu.photo_contextmenu,menu);
        Log.e("PersonActivity","onCreateContextMenu");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.e("PersonActivity","MenuItem item");
        switch (item.getItemId()){
            case R.id.g:
                choseHeadImageFromGallery();
                Log.e("PersonActivity","打开相册");
                break;
            case R.id.c:
                choseHeadImageFromCameraCapture();
                Log.e("PersonActivity","打开摄像机");
                break;
            default:
                Log.e("PersonActivity","MenuItem item over");
                break;

        }
        return super.onContextItemSelected(item);
    }
    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        getParent().startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);

    }
    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        getParent().startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    protected void handleActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            Log.e("PersonActivity","用户没有进行有效的设置操作，返回");

            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());

                break;
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;
            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */

    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);
        getParent().startActivityForResult(intent, CODE_RESULT_REQUEST);
        Log.e("PersonActivity","cropRawPhoto");
    }
    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            BM_photo = extras.getParcelable("data");
            saveMyBitmap(BM_photo);
            Log.e("PersonActivity","跳入saveMyBitmap(Bitmap mBitmap)");
        }
    }
    public void saveMyBitmap(Bitmap mBitmap)  {
        File f = new File( "/sdcard/headview"+ ".jpg");

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        photo = new BmobFile(f);
        photo.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.e("test",""+user.getUsername());
                    Log.e("test",""+user.getObjectId());
                    user.setHeadphoto(photo);
                    user.update( new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){

                                Log.e("PersonActivity","上传头像成功");

                            }else {
                                Log.e("PersonActivity","上传头像失败"+e.getMessage());
                            }
                        }
                    });
                    Toast.makeText(PersonActivity.this, "头像设置成功", Toast.LENGTH_SHORT).show();
                    headphoto.setImageBitmap(BM_photo);
                    Intent intent=new Intent();
                    intent.setAction("android.intent.action.update");
                    sendBroadcast(intent);
                    Log.e("PersonActivity","uploadsuccess"+ photo.getFileUrl());

                }else{
                    Log.e("PersonActivity",""+e.getMessage());
                }
            }

        });
    }

}
