package com.example.face.application_shoptwo.Activity;

import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.face.application_shoptwo.Model.Adapter.HistoryAdapter;
import com.example.face.application_shoptwo.Model.Adapter.ShopnameAdapter;
import com.example.face.application_shoptwo.Application.MyApplication;
import com.example.face.application_shoptwo.Model.JavaBean.ShopName;
import com.example.face.application_shoptwo.Model.History;
import com.example.face.application_shoptwo.Model.History_Bmob;
import com.example.face.application_shoptwo.Model.Shop_name;
import com.example.face.application_shoptwo.Model.Shopname_Bmob;
import com.example.face.application_shoptwo.Model.UserBmob;
import com.example.face.application_shoptwo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /*
 * 网络加载api数据初始化
 * */
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Widndows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    /*
   *以下是ViewPager
   * */
    Context context=null;
    LocalActivityManager manager = null;
    private int SelectedColor;//选中的颜色
    private int UnSelectedColor;//未选中的颜色
    private ViewPager pager=null;//页卡内容
    private ImageView cursor;// 动画图片
    private ImageView I1,I2,I3,I4;// 页卡头标
    private TextView t1, t2, t3,t4;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private UserBmob user;
    private TextView toolbar_tv;//toolbar
    private View headerLayout;
    List<Shop_name> Shop_namelist;

    List<History> Historylist;
    private AlertDialog myDialog = null;
    private Window window_support,window_search,window_service;//dialog获取使用
    private TextView tv_user,tv_message;
    private ImageView imageView;
    private BmobFile iv_photo;
    private MyReceiver receiver;
    private SharedPreferences sp;
    private Boolean destory=false;


    public ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__main);

        Bmob.initialize(this, MyApplication.getBmobId());
        /*viewpager
        * */

        context = MainActivity.this;
        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
        user = BmobUser.getCurrentUser(UserBmob.class);
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        init();


    }
    private void init(){
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        InitImageView();/*初始化动画*/
        InitTextViewAImageView();/*初始化头标和获取用户信息*/
        initPagerViewer();/*初始化PageViewer*/
        toolbar_tv=(TextView)findViewById(R.id.tv) ;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);//抽屉界面初始化
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.home);//在这才能修改NavigationIcon
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);//导航界面初始化
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextColor(null);
        navigationView.setItemIconTintList(null);//使图标无格式
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_activity__main);//导航界面初始化头部
        tv_user=(TextView)headerLayout.findViewById(R.id.tv_user);
        tv_message=(TextView)headerLayout.findViewById(R.id.tv_message);
        imageView=(ImageView)headerLayout.findViewById(R.id.imageView);
        tv_user.setText(user.getUsername());
        tv_message.setText(user.getMessage());
        iv_photo=user.getHeadphoto();
        setheadphoto(iv_photo);
        sp = this.getSharedPreferences("userInfo",MODE_PRIVATE);//只能给本程序读取
        //注册接收器
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.update");
        MainActivity.this.registerReceiver(receiver,filter);

    }

    private Handler handler=new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    /*GridView gridview=(GridView)window_support.findViewById(R.id.gridview);
                    gridview.setAdapter(new ShopnameAdapter(MainActivity.this,Shop_namelist));
                    Log.e("test","ky");*/
                    toolbar_tv.setText("比价一下，你就知道");
                    break;
                case 2:
                    toolbar_tv.setText("复杂比价");
                    break;
                case 3:
                    toolbar_tv.setText("收藏商品");
                    break;
                case 4:
                    toolbar_tv.setText("个人信息");
                    break;
                case 5:
                    GridView gridview=(GridView)window_support.findViewById(R.id.gridview);
                    gridview.setAdapter(new ShopnameAdapter(MainActivity.this,Shop_namelist));
                    Log.e("test","加载支持商家");
                    break;
                case 6:
                    ListView listView=(ListView) window_search.findViewById(R.id.listview);
                    listView.setAdapter(new HistoryAdapter(MainActivity.this,Historylist));
                    Log.e("test","加载搜索历史");
                    break;
                default:
                    break;
            }

        }
    };
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("MainActivity","onReceive start");
            UserBmob userBmob=new UserBmob();
            userBmob.setUsername(user.getUsername());
            userBmob.setPassword(getIntent().getStringExtra("pwd"));
            userBmob.login(new SaveListener<UserBmob>() {
                @Override
                public void done(UserBmob userBmob, BmobException e) {
                    if(e==null){
                        tv_message.setText(userBmob.getMessage());
                        setheadphoto(userBmob.getHeadphoto());
                        Log.e("MainActivity","onReceive success change");
                    }else {
                        Log.e("MainActivity","onReceive loginusererror:"+e.getMessage());
                    }
                }
            });
            Log.e("MainActivity","onReceive success");


        }
    }

    /*
    * 导航菜单点击事件
    * */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        MyOnPageChangeListener myOnPageChangeListener=new MyOnPageChangeListener();
        if (id == R.id.simple_shop) {
            pager.setCurrentItem(0);
            Log.e("test","simple_shop");
        } else if (id == R.id.complex_shop) {
            pager.setCurrentItem(1);


            Log.e("test","complex_shop");
        } else if (id == R.id.support_business) {
            myDialog = new AlertDialog.Builder(MainActivity.this).create();
            myDialog.show();
            window_support = myDialog.getWindow();
            window_support.setContentView(R.layout.support_business_content);

            //GetData();
            Getbmob_shopname();

            myDialog.getWindow().findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            Log.e("test","support_business");
        } else if(id==R.id.collect){
            pager.setCurrentItem(2);
            Log.e("test","collect");
        } else if (id == R.id.search) {
            myDialog = new AlertDialog.Builder(MainActivity.this).create();
            myDialog.show();
            window_search=myDialog.getWindow();
            window_search.setContentView(R.layout.search_history_content);
            Getbmob_history();
            myDialog.getWindow().findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            Log.e("test","search搜索历史");
        } else if (id == R.id.personal_information) {


            pager.setCurrentItem(3);
            Log.e("test","person");
        } else if (id == R.id.customer_service_center) {
            myDialog = new AlertDialog.Builder(MainActivity.this).create();
            myDialog.show();
            window_service=myDialog.getWindow();
            window_service.setContentView(R.layout.service_content);
            myDialog.getWindow().findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data=new Intent(MainActivity.this,BugActivity.class);
                   /* data.setData(Uri.parse("mailto:49266438@qq.com"));
                    data.putExtra(Intent.EXTRA_SUBJECT, "反馈");
                    data.putExtra(Intent.EXTRA_TEXT, "反馈意见：");*/
                    startActivity(data);
                    myDialog.dismiss();
                }
            });

            Log.e("test","service客服中心");
        }else if (id==R.id.cancellation){
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,LoginActivity.class);
            sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
            startActivity(intent);
            finish();
            Log.e("test","cancel注销用户");
        }else if(id==R.id.back){
            CollectorActivity.finishAll();
            Log.e("test","exit退出软件");
        }else if(id==R.id.rootb){
            Intent intent=new Intent(MainActivity.this,KefuActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /*
    * 加载userbmob头像函数
    * */
    public void setheadphoto(BmobFile bmobFile){
        try {
        Log.e("test","sucess"+bmobFile.getFileUrl());
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)   //加载中显示的正在加载中的图片
                .showImageOnFail(R.mipmap.ic_launcher)      //为了方便，加载失败也显示加载中的图片
                .cacheInMemory(true)                      //在内存中缓存图片
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(25))//不推荐用！！！！是否设置为圆角，弧度为多少
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)   //设置图片以如何的编码方式显示
                .build();
        //异步加载图片，并渲染到指定的控件上

            ImageLoader.getInstance().displayImage("" + bmobFile.getFileUrl(), (ImageView) headerLayout.findViewById(R.id.imageView), options);
        }catch (NullPointerException e){
            Log.e("test","error"+e);
        }

    }
    public void GetData(){
        new Thread(){
            @Override
            public void run() {
                Getshopname();
            }
        }.start();
    }

    private void Getshopname(){
        String result=null;
        String url="http://japi.juhe.cn/manmanmai/allsites.from";
        Map params=new HashMap();
        params.put("key", MyApplication.getAPPKEY());

        try{
            result=net(url,params,"GET");
            Gson gson=new Gson();
            Shop_namelist=new ArrayList<>();
            java.lang.reflect.Type type=new TypeToken<ShopName>(){}.getType();
            ShopName shopname=gson.fromJson(result,type);
            Log.i("test","result:"+shopname.getResult().size());
            for (int i=0;i<shopname.getResult().size();i++){
                Log.i("test","id:"+shopname.getResult().get(i).getId());
                Log.i("test","Site:"+shopname.getResult().get(i).getSite());
                Shop_namelist.add(new Shop_name(shopname.getResult().get(i).getId(),shopname.getResult().get(i).getSite()));
                Log.i("qwe",""+Shop_namelist.get(i).Site);
            }
            Message message=new Message();
            message.what=5;
            handler.sendMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void Getbmob_shopname(){
        BmobQuery<Shopname_Bmob> bmobQuery = new BmobQuery<Shopname_Bmob>();
        bmobQuery.getObject("cb009696ad", new QueryListener<Shopname_Bmob>() {
            @Override
            public void done(Shopname_Bmob _shopname_bmob, BmobException e) {
                if(e==null){
                    String result= _shopname_bmob.getShopname();
                    Log.e("test",""+result);
                    Gson gson=new Gson();
                    Shop_namelist=new ArrayList<>();//存在对象中
                    java.lang.reflect.Type type=new TypeToken<ShopName>(){}.getType();
                    ShopName shopname=gson.fromJson(result,type);
                    Log.i("test","result:"+shopname.getResult().size());
                    for (int i=0;i<shopname.getResult().size();i++){
                        Log.i("test","id:"+shopname.getResult().get(i).getId());
                        Log.i("test","Site:"+shopname.getResult().get(i).getSite());
                        Shop_namelist.add(new Shop_name(shopname.getResult().get(i).getId(),shopname.getResult().get(i).getSite()));
                        Log.i("qwe",""+Shop_namelist.get(i).Site);
                    }
                }else {
                        Log.e("test","查询失败"+e);
                }
                Message message=new Message();
                message.what=5;
                handler.sendMessage(message);
            }
        });
    }
    public void Getbmob_history(){
        BmobQuery<History_Bmob> bmobQuery = new BmobQuery<History_Bmob>();
        bmobQuery.addWhereEqualTo("username",user.getUsername());
        bmobQuery.setLimit(100);
        bmobQuery.findObjects(new FindListener<History_Bmob>() {
            @Override
            public void done(List<History_Bmob> list, BmobException e) {
                if (e==null){
                    Log.e("Getbmob_history","success");
                    Historylist=new ArrayList<>();//存在对象中
                    for (History_Bmob historyBmob:list){
                        Historylist.add(new History(historyBmob.getUsername(),historyBmob.getCreatedAt(),historyBmob.getShopname(),historyBmob.getMode()));
                        Log.e("Getbmob_history","success"+Historylist.size());
                    }
                    Collections.reverse(Historylist);

                }else{
                    Log.e("Getbmob_history","查询失败"+e.getMessage());
                }
                Message message=new Message();
                message.what=6;
                handler.sendMessage(message);

            }
        });


    }
    /*重写返回键
    * */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /*
    * 以下ViewPager
    * */
    /*初始化动画*/
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(this.getResources(), R.drawable.cursor).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量  4-3
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        ViewGroup.LayoutParams params = cursor.getLayoutParams();
        params.width = screenW / 3;//4-3
        cursor.setLayoutParams(params);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }
    /*初始化头标和获取用户信息*/
    private void InitTextViewAImageView() {
        I1 = (ImageView) findViewById(R.id.iv_simple);
        I2 = (ImageView) findViewById(R.id.iv_complex);
        I3 = (ImageView) findViewById(R.id.iv_collect);
        I4 = (ImageView) findViewById(R.id.iv_person);
        t1 = (TextView) findViewById(R.id.tv_simple);
        t2 = (TextView) findViewById(R.id.tv_complex);
        t3 = (TextView) findViewById(R.id.tv_collect);
        t4 = (TextView) findViewById(R.id.tv_person);
        I1.setOnClickListener(new MyOnClickListener(0));
        I2.setOnClickListener(new MyOnClickListener(1));
        I3.setOnClickListener(new MyOnClickListener(2));
        I4.setOnClickListener(new MyOnClickListener(3));
        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        t3.setOnClickListener(new MyOnClickListener(2));
        t4.setOnClickListener(new MyOnClickListener(3));
    }
    /*初始化PageViewer*/
    private void initPagerViewer() {
        pager = (ViewPager) findViewById(R.id.viewpager);
        final ArrayList<View> list = new ArrayList<View>();
        Intent intent = new Intent(context, SimpleActivity.class);
        list.add(getView("SimpleActivity", intent));
        Intent intent2 = new Intent(context, ComplexActivity.class);
        list.add(getView("ComplexActivity", intent2));
        Intent intent3 = new Intent(context, CollectActivity.class);
        list.add(getView("CollectActivity", intent3));
        Intent intent4 = new Intent(context, PersonActivity.class);
        list.add(getView("PersonActivity", intent4));

        pager.setAdapter(new MyPagerAdapter(list));
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
    }
    private View getView(String id, Intent intent) {
        //先获取当前Window对象,获取当前Activity对应的view
        return manager.startActivity(id, intent).getDecorView();
    }
    /*头标点击监听*/
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) { index = i;}

        public void onClick(View v) {
            pager.setCurrentItem(index);
        }
    };
    /* Pager适配器*/
    public class MyPagerAdapter extends PagerAdapter {
        List<View> list = new ArrayList();

        public MyPagerAdapter(ArrayList<View> list) {
            this.list = list;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager pViewPager = ((ViewPager)container);
            pViewPager.removeView(list.get(position));
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getCount() {
            return list.size();
        }

        public Object instantiateItem(View arg0, int arg1) {
            ViewPager pViewPager = ((ViewPager)arg0);
            pViewPager.addView(list.get(arg1));
            return list.get(arg1);
        }

        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        public Parcelable saveState() {
            return null;
        }

        public void startUpdate(View arg0) {
        }
    }
    /*页卡切换监听*/
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量
        int third = one * 3;// 页卡1 -> 页卡4 偏移量

        public void onPageSelected(int arg0) {
            Animation animation = null;
            UnSelectedColor=getResources().getColor(R.color.color_default);
            SelectedColor=getResources().getColor(R.color.color_important);
            ImageView image1 = (ImageView) findViewById(R.id.iv_simple);
            ImageView image2 = (ImageView) findViewById(R.id.iv_complex);
            ImageView image3 = (ImageView) findViewById(R.id.iv_collect);
            ImageView image4 = (ImageView) findViewById(R.id.iv_person);
            TextView text1 = (TextView)findViewById(R.id.tv_simple);
            TextView text2 = (TextView)findViewById(R.id.tv_complex);
            TextView text3 = (TextView)findViewById(R.id.tv_collect);
            TextView text4 = (TextView)findViewById(R.id.tv_person);
            switch(arg0) {
                case 0:
                    image1.setImageResource(R.drawable.simpleicon_focus);
                    image2.setImageResource(R.drawable.complexicon_unfocus);
                    image3.setImageResource(R.drawable.collecticon_unfocus);
                    image4.setImageResource(R.drawable.personicon_unfocus);
                    text1.setTextColor(SelectedColor);
                    text3.setTextColor(UnSelectedColor);
                    text2.setTextColor(UnSelectedColor);
                    text4.setTextColor(UnSelectedColor);
                    if(currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if(currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    } else if(currIndex==3){
                        animation = new TranslateAnimation(third, 0, 0, 0);
                    }
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                    break;
                case 1:
                    image2.setImageResource(R.drawable.complexicon_focus);
                    image1.setImageResource(R.drawable.simpleicon_unfocus);
                    image3.setImageResource(R.drawable.collecticon_unfocus);
                    image4.setImageResource(R.drawable.personicon_unfocus);
                    text2.setTextColor(SelectedColor);
                    text3.setTextColor(UnSelectedColor);
                    text1.setTextColor(UnSelectedColor);
                    text4.setTextColor(UnSelectedColor);
                    if(currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if(currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }else if(currIndex==3){
                        animation = new TranslateAnimation(third, one, 0, 0);
                    }
                    Message message1=new Message();
                    message1.what=2;
                    handler.sendMessage(message1);
                    break;
                case 2:
                    image3.setImageResource(R.drawable.collecticon_focus);
                    image1.setImageResource(R.drawable.simpleicon_unfocus);
                    image2.setImageResource(R.drawable.complexicon_unfocus);
                    image4.setImageResource(R.drawable.personicon_unfocus);
                    text3.setTextColor(SelectedColor);
                    text1.setTextColor(UnSelectedColor);
                    text2.setTextColor(UnSelectedColor);
                    text4.setTextColor(UnSelectedColor);
                    if(currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if(currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }else if(currIndex==3){
                        animation = new TranslateAnimation(third, two, 0, 0);
                    }
                    Message message2=new Message();
                    message2.what=3;
                    handler.sendMessage(message2);
                    break;
                case 3:
                    image4.setImageResource(R.drawable.personicon_focus);
                    image1.setImageResource(R.drawable.simpleicon_unfocus);
                    image2.setImageResource(R.drawable.complexicon_unfocus);
                    image3.setImageResource(R.drawable.collecticon_unfocus);
                    text4.setTextColor(SelectedColor);
                    text1.setTextColor(UnSelectedColor);
                    text2.setTextColor(UnSelectedColor);
                    text3.setTextColor(UnSelectedColor);
                    if(currIndex==0){
                        animation =new TranslateAnimation(offset, third, 0, 0);
                    }else if(currIndex == 1) {
                        animation = new TranslateAnimation(one, third, 0, 0);
                    }else if(currIndex==2){
                        animation = new TranslateAnimation(two, third, 0, 0);
                    }
                    Message message3=new Message();
                    message3.what=4;
                    handler.sendMessage(message3);
                    break;
            }

            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        PersonActivity personActivity=(PersonActivity)manager.getCurrentActivity();
        personActivity.handleActivityResult(requestCode,resultCode,intent);
        super.onActivityResult(requestCode, resultCode, intent);
    }


    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }
    //将map型转为请求参数型
    public static String urlencode(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
