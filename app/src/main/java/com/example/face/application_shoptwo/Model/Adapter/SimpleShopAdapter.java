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

import com.example.face.application_shoptwo.Activity.SimpleActivity;

import com.example.face.application_shoptwo.Activity.SimpleActivity_web;
import com.example.face.application_shoptwo.Model.Simple_Shop;
import com.example.face.application_shoptwo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by Face on 2016/10/9.
 */

public class SimpleShopAdapter extends BaseAdapter{
    private SimpleActivity activity;
    private List<Simple_Shop> dataList;
    public SimpleShopAdapter(SimpleActivity activity, List<Simple_Shop> dataList){
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
        final Simple_Shop simple_shop=dataList.get(position);
        /*Log.homepage("test","shop_name.id:"+shop_name.Id);
        Log.homepage("test","shop_name.site:"+shop_name.Site);
        ((TextView)view.findViewById(R.id.shopname)).setText(shop_name.Site);*/
        Log.e("test","simple_shop.siteName:"+simple_shop.siteName);
        Log.e("test","simple_shop.spname:"+simple_shop.spname);
        Log.e("test","simple_shop.spprice:"+simple_shop.spprice);

        ((TextView)view.findViewById(R.id.tv_sitename)).setText(simple_shop.siteName);
        ((TextView)view.findViewById(R.id.tv_spname)).setText(simple_shop.spname);
        ((TextView)view.findViewById(R.id.tv_spprice)).setText(simple_shop.spprice);
        //设置图片显示格式(我们可以设置圆角、缓存等一些列配置)
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)   //加载中显示的正在加载中的图片
                .showImageOnFail(R.drawable.loading)      //为了方便，加载失败也显示加载中的图片
                .cacheInMemory(true)                      //在内存中缓存图片
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(25))//不推荐用！！！！是否设置为圆角，弧度为多少
                .imageScaleType(ImageScaleType.EXACTLY)   //设置图片以如何的编码方式显示
                .build();
        //异步加载图片，并渲染到指定的控件上
        ImageLoader.getInstance().displayImage(""+simple_shop.sppic, (ImageView) view.findViewById(R.id.iv_sppic), options);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("test","simple_shop.spurl:"+simple_shop.spurl);
                Intent intent=new Intent(activity,SimpleActivity_web.class);
                intent.putExtra("webspurl",simple_shop.spurl);
                intent.putExtra("spname",simple_shop.spname);
                intent.putExtra("siteName",simple_shop.siteName);
                intent.putExtra("sppic",simple_shop.sppic);
                intent.putExtra("spprice",simple_shop.spprice);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

            }
        });
        return view;
    }
}
