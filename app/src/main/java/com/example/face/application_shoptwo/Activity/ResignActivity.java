package com.example.face.application_shoptwo.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.face.application_shoptwo.Application.MyApplication;
import com.example.face.application_shoptwo.Model.UserBmob;
import com.example.face.application_shoptwo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ResignActivity extends AppCompatActivity implements View.OnClickListener {
    private String name, password, message,email;
    private UserBmob user;
    private BmobFile photo;
    private Bitmap BM_photo;
    private Intent intent;

    private ImageButton iv_goback;
    private ImageView iv_head;
    private EditText re_et_name, re_et_password, re_et_message,  re_et_email;
    private Button re_btn_resign;
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
    private String path;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resign);
        Bmob.initialize(this, MyApplication.getBmobId());
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
    }

    public void init() {
        iv_goback = (ImageButton) findViewById(R.id.iv_goback);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        re_et_name = (EditText) findViewById(R.id.re_et_name);
        re_et_password = (EditText) findViewById(R.id.re_et_password);
        re_et_message = (EditText) findViewById(R.id.re_et_message);
        re_et_email = (EditText) findViewById(R.id.re_et_email);
        re_btn_resign = (Button) findViewById(R.id.re_btn_resign);


        iv_goback.setOnClickListener(this);

        re_btn_resign.setOnClickListener(this);
        View cv = getWindow().getDecorView();
        registerForContextMenu(iv_head);//注册上下文菜单
        Snackbar.make(cv,"通知：长按头像并设置",Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TEST","点击设置头像");


            }
        }).show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_goback:
                Snackbar.make(view,"是否取消注册账号",Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("TEST","返回login");
                        finish();
                    }
                }).show();
                break;
            case R.id.re_btn_resign:
                resign();
                break;
        }
    }
    public void resign(){
        progressDialog=new ProgressDialog(this,R.style.dialog);
        progressDialog.setMessage("正在注册，请稍后...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        name=re_et_name.getText().toString().trim();
        password=re_et_password.getText().toString().trim();
        message=re_et_message.getText().toString().trim();
        email=re_et_email.getText().toString().trim();

        if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(message)&&!TextUtils.isEmpty(email)){
            user=new UserBmob();
            user.setUsername(name);
            user.setPassword(password);
            user.setMessage(message);
            user.setEmail(email);
            try {
                if (photo.getFileUrl()!=null){
                    user.setHeadphoto(photo);
                }
            }catch (NullPointerException e){
                Log.e("ResignActivity","头像照片未设置"+e.getMessage());
                Toast.makeText(ResignActivity.this, "您的头像信息未设置", Toast.LENGTH_SHORT).show();
            }

            user.signUp(new SaveListener<UserBmob>() {
                @Override
                public void done(UserBmob userBmob, BmobException e) {
                    if (e==null){
                        progressDialog.dismiss();
                        Toast.makeText(ResignActivity.this, "用户注册成功", Toast.LENGTH_SHORT).show();
                        intent=new Intent();
                        intent.putExtra("name",name);
                        intent.putExtra("change_password",password);
                        setResult(1,intent);
                        Log.e("test","从注册带数据到登录");
                        ResignActivity.this.finish();
                        Log.e("ResignActivity","用户注册成功");
                    }else {
                        Toast.makeText(ResignActivity.this, "注册失败:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ResignActivity","用户注册失败"+e.getMessage());
                    }

                }
            });

        }else {
            progressDialog.dismiss();
            Toast.makeText(ResignActivity.this, "以上信息必须填写", Toast.LENGTH_SHORT).show();

        }



    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=new MenuInflater(ResignActivity.this);
        inflater.inflate(R.menu.photo_contextmenu,menu);
        Log.e("ResignActivity","onCreateContextMenu");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.e("ResignActivity","MenuItem item");
        switch (item.getItemId()){
            case R.id.g:
                choseHeadImageFromGallery();
                Log.e("ResignActivity","打开相册");
                break;
            case R.id.c:
                choseHeadImageFromCameraCapture();
                Log.e("ResignActivity","打开摄像机");
                break;
            default:
                Log.e("ResignActivity","MenuItem item over");
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
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);

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

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            Log.e("test","用户没有进行有效的设置操作，返回");

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
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }
    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
             BM_photo = extras.getParcelable("data");
            saveMyBitmap(BM_photo);
            Log.e("ResignActivity","跳入saveMyBitmap(Bitmap mBitmap)");
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
                    /*user.setHeadphoto(photo);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.e("ResignActivity","success");
                            }else {
                                Log.e("ResignActivity",""+e);}
                        }
                    });*/
                    Log.e("ResignActivity","success"+ photo.getFileUrl());

                }else{
                    Log.e("ResignActivity",""+e.getMessage());
                }
            }
            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                if (value==100){
                    Toast.makeText(ResignActivity.this, "头像设置成功", Toast.LENGTH_SHORT).show();
                    iv_head.setImageBitmap(BM_photo);
                    Log.e("ResignActivity","上传头像成功"+value);
                }


            }
        });
    }

}

