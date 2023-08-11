package com.example.bookkeeping.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bookkeeping.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {

    List<String> mDatas;
    Context context;
    public int year;
    // 当前月份被选中
    public int selectPos = -1;

    public CalendarAdapter(Context context, int year) {
        this.context = context;
        this.year = year;
        mDatas = new ArrayList<>();
        loadDatas(year);
    }

    private void loadDatas(int year) {
        for (int i = 1; i < 13; i++) {
            mDatas.add(year +"-"+String.format("%02d", i));
        }
    }

    public void setYear(int year) {
        this.year = year;
        mDatas.clear();
        loadDatas(year);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_calendar_gv, parent, false);
        TextView tv = convertView.findViewById(R.id.item_dialog_calendar_gv_tv);
        tv.setText(mDatas.get(position));
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundResource(R.color.grey_f3f3f3);
        if (position == selectPos){
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundResource(R.color.green_006400);
        }
        return convertView;
    }
}
