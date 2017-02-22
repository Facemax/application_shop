package com.example.face.application_shoptwo.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.face.application_shoptwo.Model.Adapter.SimpleShopAdapter;
import com.example.face.application_shoptwo.Application.MyApplication;
import com.example.face.application_shoptwo.Model.JavaBean.SimpleShop;
import com.example.face.application_shoptwo.Model.History_Bmob;
import com.example.face.application_shoptwo.Model.Simple_Shop;
import com.example.face.application_shoptwo.Model.UserBmob;
import com.example.face.application_shoptwo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wayww.edittextfirework.FireworkView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class SimpleActivity extends BaseActivity implements View.OnClickListener{
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Widndows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    private FireworkView mFireworkView;
    private EditText editText;
    private ImageButton clear,search;
    private String shopname,result;
    private UserBmob user;
    private ProgressDialog progressDialog;
    List<Simple_Shop> Simple_Shoplist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__simple);
        Bmob.initialize(this, MyApplication.getBmobId());
        user = BmobUser.getCurrentUser(UserBmob.class);
        init();

    }
    public void init(){

        editText=(EditText)findViewById(R.id.edit_text);
        clear=(ImageButton)findViewById(R.id.clear_button);
        search=(ImageButton)findViewById(R.id.search_button);
        mFireworkView = (FireworkView) findViewById(R.id.fire_work);//初始化特效烟花
        mFireworkView.bindEditText(editText);//监听文本框
        clear.setOnClickListener(this);
        search.setOnClickListener(this);


    }
    private Handler handler=new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){

                case 0:
                    ListView listView=(ListView)findViewById(R.id.listview);
                    listView.setAdapter(new SimpleShopAdapter(SimpleActivity.this,Simple_Shoplist));
                    break;
                case 1:
                    progressDialog.dismiss();
                    break;
                default:
                    break;
            }

        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_button:
                shopname=editText.getText().toString();
                if (shopname.isEmpty()){
                    Snackbar.make(view,"请输入需要比价的商品",Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i("TEST","搜索框没输入商品");
                        }
                    }).show();
                }else {
                    GetData();

                    progressDialog=new ProgressDialog(SimpleActivity.this,R.style.dialog);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("加载数据中");
                    progressDialog.show();
                    Log.i("TEST","比价一下按钮事件触发");
                }
                break;
            case R.id.clear_button:
                editText.setText("");
                Log.i("TEST","编辑框被清空");
                break;

        }
    }
    public void GetData(){
        new Thread(){
            @Override
            public void run() {
                GetSimpleShop(shopname);
                upload_HistoryBmob(shopname);
            }
        }.start();
    }
    private  void GetSimpleShop(String keyword){
         result=null;
        String url="http://japi.juhe.cn/manmanmai/simple.from";
        Map params=new HashMap();
        params.put("key", MyApplication.getAPPKEY());
        params.put("keyword",keyword);
        try{
            result=net(url,params,"GET");
            if (!TextUtils.isEmpty(result)){
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
            Gson gson=new Gson();
            Simple_Shoplist=new ArrayList<>();
            java.lang.reflect.Type type=new TypeToken<SimpleShop>(){}.getType();
            SimpleShop simpleShop=gson.fromJson(result,type);
            Log.e("GetSimpleShop","result:"+simpleShop.getResult().size());
            Log.e("GetSimpleShop","reason:"+simpleShop.getReason());
            for (int i=0;i<simpleShop.getResult().size();i++){
                Simple_Shoplist.add(new Simple_Shop(simpleShop.getResult().get(i).getSppic(),simpleShop.getResult().get(i).getSiteName(),simpleShop.getResult().get(i).getSpurl(),simpleShop.getResult().get(i).getSpname(),simpleShop.getResult().get(i).getSpprice()));
                Log.e("GetSimpleShop","sppic:"+simpleShop.getResult().get(i).getSppic());
                Log.e("GetSimpleShop","squrl:"+simpleShop.getResult().get(i).getSpurl());
                Log.e("GetSimpleShop","sitename:"+simpleShop.getResult().get(i).getSiteName());
                Log.e("GetSimpleShop","spname:"+simpleShop.getResult().get(i).getSpname());
                Log.e("GetSimpleShop","spprice:"+simpleShop.getResult().get(i).getSpprice());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message message=new Message();
        message.what=0;
        handler.sendMessage(message);

    }
    private void upload_HistoryBmob(String keyword){
        History_Bmob historyBmob=new History_Bmob();
        historyBmob.setUsername(user.getUsername());
        historyBmob.setMode("简单比价模式");
        historyBmob.setShopname(shopname);
        historyBmob.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Intent intent=new Intent();
                    intent.setAction("android.intent.action.updatenumber");
                    sendBroadcast(intent);
                    Log.e("upload_HistoryBmob","success upload");
                }else {
                    Log.e("upload_HistoryBmob","error:"+e.getMessage());
                }
            }
        });

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
