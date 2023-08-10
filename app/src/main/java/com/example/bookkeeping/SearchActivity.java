package com.example.bookkeeping;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bookkeeping.adapter.AccountAdapter;
import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.entity.Account;
import com.example.bookkeeping.utils.KeyBoardUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SearchActivity extends AppCompatActivity {
    ListView searchLv;
    EditText searchEt;
    RelativeLayout emptyTv;

    // 数据源
    List<Account> mDatas;
    AccountAdapter adapter;

    // 多线程
    ExecutorService executorService;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);
        searchLv.setAdapter(adapter);
        // 设置无数据时，显示的布局
        searchLv.setEmptyView(emptyTv);

        // 初始化多线程执行器
        executorService = UniteApp.getExecutorService();
    }

    private void initView() {
        searchLv = findViewById(R.id.search_lv);
        searchEt = findViewById(R.id.search_et);
        emptyTv = findViewById(R.id.search_tv_empty);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_sh:
                String keyword = searchEt.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    Toast.makeText(this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                }
                // 根据keyword去数据库搜索
                Future<List<Account>> future = executorService.submit(() -> DBManager.getAccountsLikeKeyword(keyword));
                List<Account> list = null;
                try {
                    list = future.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mDatas.clear();
                mDatas.addAll(list);
                adapter.notifyDataSetChanged();
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
                    KeyBoardUtils.closeSoftInput(this);//软键盘工具类
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

    /**
     * 通过当前点击的位置判断是否应该隐藏键盘
     * @param v 当前触摸的view
     * @param event 触摸事件MotionEvent.ACTION_DOWN，内包含触摸的初始位置
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            // v是EditText
            editText = (EditText) v;
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // event.getX()和event.getY()获得当前触摸的X-Y坐标
            // 在EditText的范围内 left < x < right && top < y < bottom
            // !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) == false
            // 此时不需要隐藏软键盘

            // 当前触摸的坐标不在EditText的范围内 -> return true
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        // v不是EditText了，直接返回false
        return false;
    }
}