package com.example.bookkeeping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.bookkeeping.adapter.RecordPagerAdapter;
import com.example.bookkeeping.frag_record.IncomeFragment;
import com.example.bookkeeping.frag_record.OutcomeFragment;
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
}