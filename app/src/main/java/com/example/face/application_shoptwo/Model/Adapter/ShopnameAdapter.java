package com.example.face.application_shoptwo.Model.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.face.application_shoptwo.Activity.MainActivity;
import com.example.face.application_shoptwo.Model.Shop_name;
import com.example.face.application_shoptwo.R;

import java.util.List;


/**
 * Created by Face on 2016/10/7.
 */

public class ShopnameAdapter extends BaseAdapter{
    private MainActivity activity;
    private List<Shop_name>dataList;

    public ShopnameAdapter(MainActivity activity, List<Shop_name> dataList){
        this.activity = activity;
        this.dataList = dataList;
        //初始化Android-Universal-Image-Loader框架
        //ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(activity).inflate(R.layout.shop_name_items,null);
        final Shop_name shop_name=dataList.get(position);
        Log.e("test","shop_name.id:"+shop_name.Id);
        Log.e("test","shop_name.site:"+shop_name.Site);
        ((TextView)view.findViewById(R.id.shopname)).setText(shop_name.Site);






        return view;
    }
}
