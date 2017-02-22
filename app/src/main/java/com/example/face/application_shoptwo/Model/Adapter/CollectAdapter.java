package com.example.face.application_shoptwo.Model.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.face.application_shoptwo.Activity.CollectActivity;
import com.example.face.application_shoptwo.Activity.CollectActivity_web;
import com.example.face.application_shoptwo.Model.Shop_Collect;

import com.example.face.application_shoptwo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by Face on 2016/11/8.
 */

public class CollectAdapter extends BaseAdapter {
    private CollectActivity activity;
    private List<Shop_Collect> dataList;

    public CollectAdapter(CollectActivity activity, List<Shop_Collect> dataList) {
        this.activity = activity;
        this.dataList = dataList;
        //初始化Android-Universal-Image-Loader框架
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
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
        view= LayoutInflater.from(activity).inflate(R.layout.shop_simple_shop_items,null);
        final Shop_Collect shop_collect=dataList.get(position);
        Log.e("test","CollectAdapter.siteName:"+shop_collect.sitename);
        Log.e("test","CollectAdapter.spname:"+shop_collect.spname);
        Log.e("test","CollectAdapter.spprice:"+shop_collect.spprice);

        ((TextView)view.findViewById(R.id.tv_sitename)).setText(shop_collect.sitename);
        ((TextView)view.findViewById(R.id.tv_spname)).setText(shop_collect.spname);
        ((TextView)view.findViewById(R.id.tv_spprice)).setText(shop_collect.spprice);
        //设置图片显示格式(我们可以设置圆角、缓存等一些列配置)
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)   //加载中显示的正在加载中的图片
                .showImageOnFail(R.drawable.loading)      //为了方便，加载失败也显示加载中的图片
                .cacheInMemory(true)                      //在内存中缓存图片
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(15))//不推荐用！！！！是否设置为圆角，弧度为多少
                .imageScaleType(ImageScaleType.EXACTLY)   //设置图片以如何的编码方式显示
                .build();
        //异步加载图片，并渲染到指定的控件上
        ImageLoader.getInstance().displayImage(""+shop_collect.sppic, (ImageView) view.findViewById(R.id.iv_sppic), options);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("test","CollectAdapter.spurl:"+shop_collect.spurl);
                Intent intent=new Intent(activity,CollectActivity_web.class);
                intent.putExtra("objectid",shop_collect.objectid);
                intent.putExtra("webspurl",shop_collect.spurl);
                intent.putExtra("spname",shop_collect.spname);
                intent.putExtra("siteName",shop_collect.sitename);
                intent.putExtra("sppic",shop_collect.sppic);
                intent.putExtra("spprice",shop_collect.spprice);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        return view;
    }
}
