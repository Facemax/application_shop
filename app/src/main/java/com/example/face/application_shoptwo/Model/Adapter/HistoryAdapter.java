package com.example.face.application_shoptwo.Model.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.face.application_shoptwo.Activity.MainActivity;
import com.example.face.application_shoptwo.Model.History;
import com.example.face.application_shoptwo.R;

import java.util.List;

/**
 * Created by Face on 2016/11/11.
 */

public class HistoryAdapter extends BaseAdapter {
    private MainActivity activity;
    private List<History> dataList;

    public HistoryAdapter( MainActivity activity,List<History> dataList) {
        this.dataList = dataList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(activity).inflate(R.layout.history_items,null);
        final History history=dataList.get(i);
        Log.e("HistoryAdapter","history.username:"+history.username);
        Log.e("HistoryAdapter","history.mode:"+history.mode);
        Log.e("HistoryAdapter","history.shopname:"+history.shopname);
        Log.e("HistoryAdapter","history.data:"+history.data);
        ((TextView)view.findViewById(R.id.tv_mode)).setText(history.mode);
        ((TextView)view.findViewById(R.id.tv_shopname)).setText(history.shopname);
        ((TextView)view.findViewById(R.id.tv_data)).setText(history.data);



        return view;
    }
}
