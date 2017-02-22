package com.example.face.application_shoptwo.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.face.application_shoptwo.Model.Collect_Bmob;
import com.example.face.application_shoptwo.Model.UserBmob;
import com.example.face.application_shoptwo.R;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ComplexActivity_web extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_spname,tv_spprice,tv_classname,tv_sitename,tv_ziying,tv_brandname;
    private EditText et_spurl;
    private WebView webView;
    private ImageButton goback,collect,sharp,shang_btn,xia_btn;
    private UserBmob user;
    private Intent intent;
    private Collect_Bmob collect_bmob;
    private boolean flag;
    private LinearLayout linear;

    String i;
    //qq分享
    protected static final String QQ_APP_ID = "1105839136";
    protected Tencent mTencent;
    //微信分享
    protected static final String WX_APP_ID = "wxc676771dbb9f0bfd";
    private IWXAPI api;
    private AlertDialog shareDia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_web);
        user = BmobUser.getCurrentUser(UserBmob.class);
        init();
        initShareToQQ();
        initShareToWX();

    }
    public void  init(){
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tv_brandname=(TextView)findViewById(R.id.tv_brandName);
        tv_spname=(TextView)findViewById(R.id.tv_spname);
        tv_sitename=(TextView)findViewById(R.id.tv_sitename);
        tv_ziying=(TextView)findViewById(R.id.tv_ziying);
        tv_classname=(TextView)findViewById(R.id.tv_classname);
        tv_spprice=(TextView)findViewById(R.id.tv_spprice);
        webView=(WebView)findViewById(R.id.webview);
        et_spurl=(EditText)findViewById(R.id.et_spurl);
        /*goback=(ImageButton)findViewById(R.id.btn_collect);
        collect=(ImageButton)findViewById(R.id.ib_collect);
        sharp=(ImageButton)findViewById(R.id.ib_sharp);

        shang_btn=(ImageButton)findViewById(R.id.shang_btn);
        xia_btn=(ImageButton)findViewById(R.id.xia_btn);
        linear=(LinearLayout)findViewById(R.id.linear);*/


        /*goback.setOnClickListener(this);
        collect.setOnClickListener(this);
        sharp.setOnClickListener(this);
        shang_btn.setOnClickListener(this);
        xia_btn.setOnClickListener(this);*/

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collect =(FloatingActionButton)findViewById(R.id.btn_collect);
        collect.setOnClickListener(this);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitle("商品详情");
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);////
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.sharp:
                        share_choose();
                        Log.e("sharp","ssss");
                }
                return false;
            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.goback);//在这才能修改NavigationIcon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("finish","ssss");
                finish();
            }
        });


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置WebView属性，能够执行Javascript脚本
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);//支持缩放
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webView.getSettings().setJavaScriptEnabled(true);//启动缓存
        webView.getSettings().setAppCacheEnabled(true);//设置缓存模式
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webView.requestFocusFromTouch();//设置支持手势焦点
        webView.setWebViewClient(new WebViewClient());//设置Web视图
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    Toast.makeText(ComplexActivity_web.this, "加载成功", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
        intent=getIntent();
        duibi();
        String z=intent.getStringExtra("ziying");
        if(z.equals("1")){
            Log.e("ComplexActivity_web","ziying");
            tv_ziying.setText("自营");
        }
        if(z.equals("2")){
            Log.e("ComplexActivity_web","ziying");
            tv_ziying.setText("第三方");
        }

        tv_spname.setText(intent.getStringExtra("spname"));
        tv_sitename.setText(intent.getStringExtra("siteName"));
        tv_classname.setText(intent.getStringExtra("classname"));
        tv_brandname.setText(intent.getStringExtra("brandname"));
        tv_spprice.setText(intent.getStringExtra("spprice"));
        et_spurl.setText(intent.getStringExtra("webspurl"));
        webView.loadUrl(intent.getStringExtra("webspurl"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /*case R.id.btn_collect:
                Log.e("ComplexActivity_web:","finish");
                finish();
                break;*/
            case R.id.btn_collect:
                if (!flag){
                    Snackbar.make(view,"准备收藏在商品收藏中",Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveBmob();
                            Intent intent=new Intent();
                            intent.setAction("android.intent.action.updatecollect");
                            sendBroadcast(intent);
                            collect.setImageResource(R.drawable.collect_web_chose);
                            Toast.makeText(ComplexActivity_web.this,"已收藏",Toast.LENGTH_SHORT).show();
                            flag=true;
                            Log.e("ComplexActivity_web:","网址链接已收藏");
                        }
                    }).show();

                }else {
                    Toast.makeText(ComplexActivity_web.this,"您已收藏",Toast.LENGTH_SHORT).show();
                }
                break;
           /* case R.id.ib_sharp:
                share_choose();
                break;
            case R.id.shang_btn:
                linear.setVisibility(View.GONE);
                xia_btn.setVisibility(View.VISIBLE);
                shang_btn.setVisibility(View.GONE);
                Log.e("shang","unshow");
                break;
            case R.id.xia_btn:
                linear.setVisibility(View.VISIBLE);
                shang_btn.setVisibility(View.VISIBLE);
                xia_btn.setVisibility(View.GONE);
                Log.e("xia","show");
                break;*/
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为toolbar创建Menu
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
    //判断是否连接上网络和获取到手机连接管理对象
    private boolean isConnect(Context context) {
        try {
            //获取手机所有连接管理对象
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conn != null) {
                //获取网络连接管理对象
                NetworkInfo info = conn.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    //判断当前网络是否已连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void initShareToQQ() {
        //在使用Tencent分享功能之前，需要先注册一个Tecent类
        mTencent = Tencent.createInstance(QQ_APP_ID, this.getApplicationContext());
        //判断是否连接上网络和获取到手机连接管理对象
        if (isConnect(this) == false) {
            new AlertDialog.Builder(this)
                    .setTitle("网络错误!")
                    .setMessage("网络连接失败")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }
    //分享选择弹出框
    private void share_choose() {
        shareDia = new AlertDialog.Builder(ComplexActivity_web.this).create();
        shareDia.show();
        Window win = shareDia.getWindow();
        //设置自定义的对话框布局
        win.setContentView(R.layout.share_alertdialog);
        LinearLayout linear1=(LinearLayout)win.findViewById(R.id.linear1);
        LinearLayout linear2=(LinearLayout)win.findViewById(R.id.linear2);
        LinearLayout linear3=(LinearLayout)win.findViewById(R.id.linear3);
        LinearLayout linear4=(LinearLayout)win.findViewById(R.id.linear4);
        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToQQzone();
                Log.e("ComplexActivity_web","QQ空间");
                shareDia.dismiss();
            }
        });
        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToQQ();
                Log.e("ComplexActivity_web","QQ好友");
                shareDia.dismiss();
            }
        });
        linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToWX(1);
                Log.e("ComplexActivity_web","朋友圈");
                shareDia.dismiss();
            }
        });
        linear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToWX(0);
                Log.e("ComplexActivity_web","微信");
                shareDia.dismiss();
            }
        });

    }
    //QQ分享
    private void shareToQQ() {

        ComplexActivity_web.ShareListener myListener = new ComplexActivity_web.ShareListener();
        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, "来自比价");
        //分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,intent.getStringExtra("sppic"));
        //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "比价商品："+intent.getStringExtra("spname"));
        //这条分享消息被好友点击后的跳转URL
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, intent.getStringExtra("webspurl"));
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "比价软件");
        // 标识该消息的来源应用，值为应用名称+AppId
        mTencent.shareToQQ(ComplexActivity_web.this, bundle, myListener);
    }
    //QQ空间分享
    private void shareToQQzone() {

        ComplexActivity_web.ShareListener myListener = new ComplexActivity_web.ShareListener();
        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE,"来自比价");
        //分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,intent.getStringExtra("sppic"));
        //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "比价商品："+intent.getStringExtra("spname"));
        //这条分享消息被好友点击后的跳转URL
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL,intent.getStringExtra("webspurl"));
        // 标识该消息的来源应用，值为应用名称+AppId
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "比价软件");
        bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(ComplexActivity_web.this, bundle, myListener);
    }
    //QQ分享后的回调
    private class ShareListener implements IUiListener {

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            Toast.makeText(ComplexActivity_web.this,"分享取消",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            Toast.makeText(ComplexActivity_web.this,"分享成功",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            Toast.makeText(ComplexActivity_web.this,"分享失败",Toast.LENGTH_SHORT).show();
        }

    }
    private void initShareToWX() {
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        api.registerApp(WX_APP_ID);
    }
    // 微信分享：   flag：0-分享给朋友  1-分享到朋友圈
    private void shareToWX(int flag) {
        if (!api.isWXAppInstalled())
        {
            Toast.makeText(ComplexActivity_web.this,"您还未安装微信!",Toast.LENGTH_SHORT).show();
            return;
        }
        //创建一个WXWebPageObject对象，用于封装要发送的Url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl =intent.getStringExtra("webspurl") ;
        //创建一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "来自比价";
        msg.description = "比价商品："+intent.getStringExtra("spname");
        //这里替换一张自己工程里的图片资源
        Bitmap bitmap=getHttpBitmap(intent.getStringExtra("sppic"));
        msg.setThumbImage(bitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //transaction字段用于唯一标识一个请求，这个必须有，否则会出错
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;

        //表示发送给朋友圈  WXSceneTimeline  表示发送给朋友  WXSceneSession
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

        api.sendReq(req);
    }
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.e("getHttpBitmap","success");
        return bitmap;

    }

    public void saveBmob(){
        collect_bmob=new Collect_Bmob();
        collect_bmob.setUsername(user.getUsername());
        collect_bmob.setSitename(intent.getStringExtra("siteName"));
        collect_bmob.setSpname(intent.getStringExtra("spname"));
        collect_bmob.setSpprice(intent.getStringExtra("spprice"));
        collect_bmob.setSpurl(intent.getStringExtra("webspurl"));
        collect_bmob.setSppic(intent.getStringExtra("sppic"));
        collect_bmob.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Log.e("ComplexActivity_web:","收藏成功");

                }else {
                    Log.e("ComplexActivity_web:","收藏失败"+e);
                }
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return false;
    }
    public void duibi(){
        Log.e("对比","开始"+flag);
        BmobQuery<Collect_Bmob> query = new BmobQuery<Collect_Bmob>();
        String s=intent.getStringExtra("spname");
        Log.e("ComplexActivity:","spname:"+s);
        query.addWhereEqualTo("username",user.getUsername());
        query.addWhereEqualTo("spname",s);
        query.setLimit(100);
        query.findObjects(new FindListener<Collect_Bmob>() {
            @Override
            public void done(List<Collect_Bmob> list, BmobException e) {
                if(e==null){
                    for(Collect_Bmob collect_bmob:list){
                        int i=list.size();
                        Log.e("ComplexActivity_web:","i:"+i);
                        if (i!=0){
                            flag=true;
                        Log.e("ComplexActivity_web:","已收藏"+i+"flag:"+flag);
                        collect.setImageResource(R.drawable.collect_web_chose);

                        }else {
                            Log.e("ComplexActivity_web:","未收藏");
                            flag=false;
                        }
                    }
                }else{
                    Log.e("ComplexActivity_web:","对比失败");

                }
            }
        });
        Log.e("对比","结束"+flag);
    }
}
