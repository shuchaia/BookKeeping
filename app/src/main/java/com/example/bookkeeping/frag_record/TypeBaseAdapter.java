package com.example.bookkeeping.frag_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookkeeping.R;
import com.example.bookkeeping.entity.Type;

import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {

    List<Type> typeList;
    Context context;
    int selectPos = 0; // 被选中的位置

    public TypeBaseAdapter(List<Type> typeList) {
        this.typeList = typeList;
    }

    public TypeBaseAdapter(Context context, List<Type> typeList) {
        this.typeList = typeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return typeList.size();
    }

    @Override
    public Object getItem(int position) {
        return typeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent, false);
        // 查找布局控件
        ImageView iv = convertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = convertView.findViewById(R.id.item_recordfrag_tv);

        //获取指定位置的数据源
        Type type = typeList.get(position);
        tv.setText(type.getTypeName());

        // 判断当前位置是否被选中
        if (selectPos == position) {
            iv.setImageResource(type.getsImageId());
        }else {
            iv.setImageResource(type.getImageId());
        }

        return convertView;
    }
}
