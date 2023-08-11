package com.example.bookkeeping.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.bookkeeping.R;
import com.example.bookkeeping.entity.Account;
import com.google.android.material.transition.Hold;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AccountAdapter extends BaseAdapter {

    Context context;
    List<Account> mDatas;
    LayoutInflater inflater;
    LocalDateTime dateTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public AccountAdapter(Context context, List<Account> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
        dateTime = LocalDateTime.now();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_main_lv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Account account = mDatas.get(position);
        holder.typeIv.setImageResource(account.getsImageId());
        holder.typeTv.setText(account.getTypeName());
        holder.beizhuTv.setText(account.getBeizhu());

        // 设置不同时间的显示方式
        if (dateTime.toLocalDate().toString().equals(account.getTime().split(" ")[0])) {
            holder.timeTV.setText("今天 "+account.getTime().split(" ")[1]);
            Log.d("kaifa", "今天");
        }else if (dateTime.minusDays(1L).toLocalDate().toString().equals(account.getTime().split(" ")[0])){
            holder.timeTV.setText("昨天 "+account.getTime().split(" ")[1]);
            Log.d("kaifa", "昨天");
        }else {
            holder.timeTV.setText(account.getTime());
        }

        holder.moneyTv.setText("￥ "+ account.getMoney());

        return convertView;
    }

    static class ViewHolder{
        ImageView typeIv;
        TextView typeTv, beizhuTv, timeTV, moneyTv;

        public ViewHolder(View view){
            typeIv = view.findViewById(R.id.item_mainlv_iv);
            typeTv = view.findViewById(R.id.item_mainlv_tv_title);
            beizhuTv = view.findViewById(R.id.item_mainlv_tv_beizhu);
            timeTV = view.findViewById(R.id.item_mainlv_tv_time);
            moneyTv = view.findViewById(R.id.item_mainlv_tv_money);
        }
    }
}
