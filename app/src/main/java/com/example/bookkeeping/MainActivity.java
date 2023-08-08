package com.example.bookkeeping;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.bookkeeping.adapter.AccountAdapter;
import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.entity.Account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    // 控件
    ListView todayLv;

    // 数据源
    List<Account> mDatas;

    // Adapter
    AccountAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todayLv = findViewById(R.id.main_lv);
        mDatas = new ArrayList<>();
        // 设置适配器
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }

    /**
     * 当activity获取焦点时调用
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        // 加载数据库信息
        LocalDateTime dateTime = LocalDateTime.now();
        // 获得今天的记录
        Callable<List<Account>> callableTask = () -> DBManager.getAccountsByTime(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
        Future<List<Account>> future = UniteApp.getExecutorService().submit(callableTask);
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
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_iv_search:
                break;
            case R.id.main_btn_edit:
                Intent intent = new Intent(this, RecordActivity.class); // 跳转到记录页面
                startActivity(intent);
                break;
            case R.id.main_btn_more:
                break;
        }
    }
}