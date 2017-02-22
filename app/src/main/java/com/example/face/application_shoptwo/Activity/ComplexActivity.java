package com.example.face.application_shoptwo.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.face.application_shoptwo.Model.Adapter.ComplexShopAdapter;
import com.example.face.application_shoptwo.Application.MyApplication;
import com.example.face.application_shoptwo.Model.JavaBean.ComplexShop;
import com.example.face.application_shoptwo.Model.Complex_Shop;
import com.example.face.application_shoptwo.Model.History_Bmob;
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

public class ComplexActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Widndows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    private FireworkView mFireworkView;
    private EditText editText,et_pricemin,et_pricemax;
    private ImageButton clear,search,xia,shang;
    private TextView paper;
    private Spinner spinner;
    private ListView listView;
    private CheckBox ziying,taobao;
    private int pricemax,pricemin,Taobao=1,papernum=1,paperflag;
    private String orderby="score",keyword;
    private boolean Ziying=false;
    private ProgressDialog progressDialog;
    List<Complex_Shop> Complex_Shoplist;
    private UserBmob user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__complex);
        Bmob.initialize(this, MyApplication.getBmobId());
        user = BmobUser.getCurrentUser(UserBmob.class);
        init();
    }
    public void init(){
        editText=(EditText)findViewById(R.id.edit_text);
        et_pricemax=(EditText)findViewById(R.id.et_pricemax);
        et_pricemin=(EditText)findViewById(R.id.et_pricemin);
        clear=(ImageButton)findViewById(R.id.clear_button);
        search=(ImageButton)findViewById(R.id.search_button);
        shang=(ImageButton)findViewById(R.id.shang);
        xia=(ImageButton)findViewById(R.id.xia);
        paper=(TextView)findViewById(R.id.tv_paper);
        mFireworkView = (FireworkView) findViewById(R.id.fire_work);//初始化特效烟花
        mFireworkView.bindEditText(editText);//监听文本框
        clear.setOnClickListener(this);
        search.setOnClickListener(this);
        shang.setOnClickListener(this);
        xia.setOnClickListener(this);
        listView=(ListView)findViewById(R.id.listview);
        ziying=(CheckBox)findViewById(R.id.ziying);
        taobao=(CheckBox)findViewById(R.id.taobao);//初始化复选框
        ziying.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Ziying=true;
                    Log.e("test",""+ziying.getText()+Ziying);
                }else {
                    Ziying=false;
                    Log.e("test",""+ziying.getText()+Ziying);
                }
            }
        });
        taobao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Taobao=1;
                    Log.e("test",""+taobao.getText()+Taobao);
                }else {
                    Taobao=0;
                    Log.e("test",""+taobao.getText()+Taobao);
                }
            }
        });//设置复选框监听事件

        spinner=(Spinner)findViewById(R.id.spinner);//初始化列表选择框
        spinner.setOnItemSelectedListener(this);//设置列表选择框监听事件
    }
    private Handler handler=new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){

                case 0:
                    shang.setVisibility(View.VISIBLE);
                    xia.setVisibility(View.VISIBLE);
                    paper.setVisibility(View.VISIBLE);
                    listView.setAdapter(new ComplexShopAdapter(ComplexActivity.this,Complex_Shoplist));
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
            case R.id.clear_button:
                editText.setText("");
                et_pricemin.setText("");
                et_pricemax.setText("");
                ziying.setChecked(false);
                taobao.setChecked(false);
                listView.setAdapter(null);
                shang.setVisibility(View.GONE);
                xia.setVisibility(View.GONE);
                paper.setVisibility(View.GONE);
                Log.e("test","clear_button");
                break;
            case R.id.search_button:
                Search(view);
                progressDialog=new ProgressDialog(ComplexActivity.this,R.style.dialog);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("加载数据中");
                progressDialog.show();

                break;
            case R.id.xia:
                int j=Integer.parseInt(paper.getText().toString());
                Log.e("CA","下一页:"+j);
                Log.e("CA","paperflag:"+paperflag);

                if (paperflag==0){

                    Toast.makeText(ComplexActivity.this, "已经是最后一页",Toast.LENGTH_LONG).show();
                }else {
                    j++;
                    papernum=j;
                    GetData();
                    paper.setText(""+j);
                }

                break;
            case R.id.shang:
                int i=Integer.parseInt(paper.getText().toString());
                Log.e("CA","上一页:"+i);
                if(i==1){
                    Toast.makeText(ComplexActivity.this, "已经是第一页",Toast.LENGTH_LONG).show();
                }
                if (i>1){
                    i--;
                    papernum=i;
                    GetData();
                    paper.setText(""+i);
                }
                break;
        }

    }
    public void Search(View view){
        if (TextUtils.isEmpty(et_pricemin.getText().toString())){
            pricemin=0;
            Toast.makeText(ComplexActivity.this, "无最低价格限制",Toast.LENGTH_LONG).show();
        }else {
            pricemin=Integer.parseInt(et_pricemin.getText().toString());
            Log.e("ComplexActivity","pricemin:"+pricemin);
        }
        if (TextUtils.isEmpty(et_pricemax.getText().toString())){
            pricemax=0;
            Toast.makeText(ComplexActivity.this, "无最高价格限制",Toast.LENGTH_LONG).show();
        }else {
            pricemax=Integer.parseInt(et_pricemax.getText().toString());
            Log.e("ComplexActivity","pricemax:"+pricemax);
        }
        if(!TextUtils.isEmpty(editText.getText().toString())){
            keyword=editText.getText().toString();
            GetData();
        }else {
            Snackbar.make(view,"请输入需要比价的商品",Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("ComplexActivity","搜索框没输入商品");
                }
            }).show();
        }


    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 1:
                orderby="price";
                Log.e("orderby","1"+orderby);
                break;
            case 2:
                orderby="sell";
                Log.e("orderby","2"+orderby);
                break;
        }
        if(spinner.getSelectedItem().toString().equals("按权重从高到底排序")){
            orderby="score";
            Log.e("123","0"+orderby);
        }
    }
    private void upload_HistoryBmob(String keyword){
        History_Bmob historyBmob=new History_Bmob();
        historyBmob.setUsername(user.getUsername());
        historyBmob.setMode("复杂比价模式");
        historyBmob.setShopname(keyword);
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
    public void GetData(){
        new Thread(){
            @Override
            public void run() {
                Log.e("ComplexActivity","Start_getdata");
                GetComplexShop(keyword,pricemax,pricemin,Ziying,orderby,Taobao,papernum);
                upload_HistoryBmob(keyword);
                Log.e("ComplexActivity","GetComplexShop各参数："+"keyword:"+keyword+"pricemin:"+pricemin+"pricemax:"+pricemax+"Ziying:"+Ziying+"orderby:"+orderby+"Taobao:"+Taobao+"papernum:"+papernum);

            }
        }.start();
        Log.e("ComplexActivity","End_getdata");
    }
    private void GetComplexShop(String keyword,int pricemax,int pricemin,boolean Ziying,String orderby,int Taobao,int Papernum){
        String result=null;
        String url="http://japi.juhe.cn/manmanmai/complex.from";
        Map params=new HashMap();

        params.put("key", MyApplication.getAPPKEY());
        params.put("keyword",keyword);
        params.put("PageNum",Papernum);//页号
        params.put("PageSize",50);//返回一页结果为50
        params.put("PriceMin",pricemin);
        params.put("PriceMax",pricemax);
        params.put("Orderby",orderby);
        params.put("ZiYing",Ziying);
        params.put("ExtraParameter",Taobao);
        try{
            result=net(url,params,"GET");
            if (!TextUtils.isEmpty(result)){
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
            Log.e("ComplexActivity",""+result);
            Gson gson=new Gson();
            Complex_Shoplist=new ArrayList<>();
            java.lang.reflect.Type type = new TypeToken<ComplexShop>(){}.getType();
            ComplexShop complexshop = gson.fromJson(result,type);

            Log.e("ComplexActivity","SearchResultList:"+complexshop.getResult().getSearchResultList().size());
                paperflag=complexshop.getResult().getSearchResultList().size();
            for(int i=0;i<complexshop.getResult().getSearchResultList().size();i++){
                Log.i("ComplexActivity","spname:"+complexshop.getResult().getSearchResultList().get(i).getSpname());//商品名字
                Log.i("ComplexActivity","sppic:"+complexshop.getResult().getSearchResultList().get(i).getSppic());//商品图片地址
                Log.i("ComplexActivity","spurl:"+complexshop.getResult().getSearchResultList().get(i).getSpurl());//商品url地址
                Log.i("ComplexActivity","spprice:"+complexshop.getResult().getSearchResultList().get(i).getSpprice());//商品价格
                Log.i("ComplexActivity","className:"+complexshop.getResult().getSearchResultList().get(i).getClassName());//分类名称
                Log.i("ComplexActivity","brandName:"+complexshop.getResult().getSearchResultList().get(i).getBrandName());//品牌名称
                Log.i("ComplexActivity","siteName:"+complexshop.getResult().getSearchResultList().get(i).getSiteName());//商城名称
                Log.i("ComplexActivity","commentUrl:"+complexshop.getResult().getSearchResultList().get(i).getCommentUrl());//评论地址
                Log.i("ComplexActivity","commentCount:"+complexshop.getResult().getSearchResultList().get(i).getCommentCount());//评论数量
                Log.i("ComplexActivity","TitleHighLighter:"+complexshop.getResult().getSearchResultList().get(i).getTitleHighLighter());//标题
                Log.i("ComplexActivity","ziying:"+complexshop.getResult().getSearchResultList().get(i).getZiying());//是否自营，1表示自营，2表示第三方
                Log.i("ComplexActivity","id:"+complexshop.getResult().getSearchResultList().get(i).getId());

                Complex_Shoplist.add(new Complex_Shop(complexshop.getResult().getSearchResultList().get(i).getSpname(),complexshop.getResult().getSearchResultList().get(i).getZiying(),complexshop.getResult().getSearchResultList().get(i).getTitleHighLighter(),complexshop.getResult().getSearchResultList().get(i).getCommentCount(),complexshop.getResult().getSearchResultList().get(i).getCommentUrl(),complexshop.getResult().getSearchResultList().get(i).getSiteName(),complexshop.getResult().getSearchResultList().get(i).getBrandName(),complexshop.getResult().getSearchResultList().get(i).getClassName(),complexshop.getResult().getSearchResultList().get(i).getSpprice(),complexshop.getResult().getSearchResultList().get(i).getSpurl(),complexshop.getResult().getSearchResultList().get(i).getSppic()));
                Log.e("ComplexActivity-finish","good");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message message=new Message();
        message.what=0;
        handler.sendMessage(message);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
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
