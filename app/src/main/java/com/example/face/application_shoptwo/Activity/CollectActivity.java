package com.example.face.application_shoptwo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.face.application_shoptwo.Model.Adapter.CollectAdapter;
import com.example.face.application_shoptwo.Application.MyApplication;
import com.example.face.application_shoptwo.Model.Shop_Collect;
import com.example.face.application_shoptwo.Model.UserBmob;
import com.example.face.application_shoptwo.Model.Collect_Bmob;
import com.example.face.application_shoptwo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CollectActivity extends BaseActivity {
    List<Shop_Collect> Simple_Shoplist;//初始化泛型
    private PullToRefreshListView mPullToRefreshListView;
    private UserBmob user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__collect);
        Bmob.initialize(this, MyApplication.getBmobId());
        user = BmobUser.getCurrentUser(UserBmob.class);
        mPullToRefreshListView=(PullToRefreshListView)findViewById(R.id.listview);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getCollect_Bmob();
            }
        });
        getCollect_Bmob();


    }

    public void getCollect_Bmob(){
        BmobQuery<Collect_Bmob> query = new BmobQuery<Collect_Bmob>();
        query.addWhereEqualTo("username",user.getUsername());
        query.setLimit(50);
        query.findObjects(new FindListener<Collect_Bmob>() {
            @Override
            public void done(List<Collect_Bmob> list, BmobException e) {
                Simple_Shoplist=new ArrayList<>();//存在对象中
                if(e==null){
                    Log.e("getCollect_Bmob","success,数据有："+list.size());
                    for(Collect_Bmob collect_bmob:list){

                        Simple_Shoplist.add(new Shop_Collect(collect_bmob.getObjectId(),collect_bmob.getUsername(),collect_bmob.getSpprice(),collect_bmob.getSpname(),collect_bmob.getSpurl(),collect_bmob.getSitename(),collect_bmob.getSppic()));
                    }
                    Intent intent=new Intent();
                    intent.setAction("android.intent.action.updatecollect");
                    sendBroadcast(intent);
                    Collections.reverse( Simple_Shoplist);
                    mPullToRefreshListView.setAdapter(new CollectAdapter(CollectActivity.this,Simple_Shoplist));
                    mPullToRefreshListView.onRefreshComplete();
                }else {
                    Log.e("getCollect_Bmob","error："+e.getMessage());
                }
            }
        });


    }
}
