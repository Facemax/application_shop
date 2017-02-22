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

import com.example.face.application_shoptwo.Activity.ComplexActivity;
import com.example.face.application_shoptwo.Activity.ComplexActivity_web;
import com.example.face.application_shoptwo.Model.Complex_Shop;
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

public class ComplexShopAdapter extends BaseAdapter{
    private ComplexActivity activity;
    private List<Complex_Shop> dataList;
    public ComplexShopAdapter(ComplexActivity activity, List<Complex_Shop> dataList){
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
        final Complex_Shop complex_shop=dataList.get(position);
        /*Log.homepage("test","shop_name.id:"+shop_name.Id);
        Log.homepage("test","shop_name.site:"+shop_name.Site);
        ((TextView)view.findViewById(R.id.shopname)).setText(shop_name.Site);*/
        Log.e("test","complex_shop.siteName"+complex_shop.siteName);
        Log.e("test","complex_shop.spname:"+complex_shop.spname);
        Log.e("test","complex_shop.spprice:"+complex_shop.spprice);

        ((TextView)view.findViewById(R.id.tv_sitename)).setText(complex_shop.siteName);
        ((TextView)view.findViewById(R.id.tv_spname)).setText(complex_shop.spname);
        ((TextView)view.findViewById(R.id.tv_spprice)).setText(complex_shop.spprice);
        //设置图片显示格式(我们可以设置圆角、缓存等一些列配置)
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)   //加载中显示的正在加载中的图片
                .showImageOnFail(R.drawable.loading)      //为了方便，加载失败也显示加载中的图片
                .cacheInMemory(true)                      //在内存中缓存图片
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(25))//不推荐用！！！！是否设置为圆角，弧度为多少
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)   //设置图片以如何的编码方式显示
                .build();
        //异步加载图片，并渲染到指定的控件上
        ImageLoader.getInstance().displayImage(""+complex_shop.sppic, (ImageView) view.findViewById(R.id.iv_sppic), options);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("test","complex_shop 开始传数据--->Complex_webviewShop_Activity");
                Intent intent=new Intent(activity,ComplexActivity_web.class);
                intent.putExtra("siteName",complex_shop.siteName);
                intent.putExtra("ziying",complex_shop.ziying);
                intent.putExtra("classname",complex_shop.className);
                intent.putExtra("brandname",complex_shop.brandName);
                intent.putExtra("spprice",complex_shop.spprice);
                intent.putExtra("webspurl",complex_shop.spurl);
                intent.putExtra("spname",complex_shop.spname);
                intent.putExtra("sppic",complex_shop.sppic);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                Log.e("test","complex_shop 结束传数据--->Complex_webviewShop_Activity");

            }
        });
        return view;
    }
}
