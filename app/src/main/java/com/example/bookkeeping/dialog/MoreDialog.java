package com.example.bookkeeping.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.bookkeeping.AboutActivity;
import com.example.bookkeeping.R;
import com.example.bookkeeping.SettingActivity;

public class MoreDialog extends Dialog implements View.OnClickListener {

    Button aboutBtn, settingBtn, historyBtn, infoBtn;
    ImageView backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);
        aboutBtn = findViewById(R.id.dialog_more_btn_about);
        settingBtn = findViewById(R.id.dialog_more_btn_settings);
        historyBtn = findViewById(R.id.dialog_more_btn_history);
        infoBtn = findViewById(R.id.dialog_more_btn_info);
        backIv = findViewById(R.id.dialog_more_iv);

        aboutBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        backIv.setOnClickListener(this);
    }

    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.dialog_more_btn_about:
                // intent.setClass(getContext(), AboutActivity.class);
                intent = new Intent(getContext(), AboutActivity.class);
                break;
            case R.id.dialog_more_btn_settings:
                intent = new Intent(getContext(), SettingActivity.class);
                break;
            case R.id.dialog_more_btn_history:
                break;
            case R.id.dialog_more_btn_info:
                break;
            case R.id.dialog_more_iv:
                break;
        }
        if (intent != null) {
            getContext().startActivity(intent);
        }
        cancel();
    }

    /* 设置Dialog的尺寸和屏幕尺寸一致*/
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void setDialogSize(){
//        获取当前窗口对象
        Window window = getWindow();
//        获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
//        获取屏幕宽度
        // Display d = window.getWindowManager().getDefaultDisplay();
        WindowManager windowManager = window.getWindowManager();
        //wlp.width = (int)(d.getWidth());  //对话框窗口为屏幕窗口
        wlp.width = windowManager.getCurrentWindowMetrics().getBounds().width();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
