package com.example.bookkeeping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.bookkeeping.adapter.RecordPagerAdapter;
import com.example.bookkeeping.frag_record.IncomeFragment;
import com.example.bookkeeping.frag_record.BaseRecordFragment;
import com.example.bookkeeping.frag_record.OutcomeFragment;
import com.example.bookkeeping.utils.KeyBoardUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // 查找控件
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);

        // 设置viewpager加载的页面
        initPager();
    }

    private void initPager() {
        // 初始化两个fragment的集合
        List<Fragment> fragmentList = new ArrayList<>();
        // 支出在前，收入在后
        OutcomeFragment outcomeFragment = new OutcomeFragment();
        IncomeFragment incomeFragment = new IncomeFragment();
        fragmentList.add(outcomeFragment);
        fragmentList.add(incomeFragment);

        // 创建适配器
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        // 设置适配器对象
        viewPager.setAdapter(pagerAdapter);

        // 将tabLayout和viewPager关联
        tabLayout.setupWithViewPager(viewPager);
    }

    // 叉号的点击事件
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_iv_back:
                finish();
                break;
        }
    }

    //使editText点击外部时候失去焦点
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {//点击editText控件外部
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    KeyBoardUtils.closeKeyboard(v);//软键盘工具类
                    if (editText != null) {
                        editText.clearFocus();
                    }
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    EditText editText = null;

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            editText = (EditText) v;
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}