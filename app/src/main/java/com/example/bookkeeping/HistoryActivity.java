package com.example.bookkeeping;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookkeeping.adapter.AccountAdapter;
import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.dialog.CalendarDialog;
import com.example.bookkeeping.entity.Account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HistoryActivity extends AppCompatActivity {

    RelativeLayout emptyDataTv;

    ListView historyLv;
    TextView timeTv;

    List<Account> mDatas;
    AccountAdapter adapter;

    int year, month;
    int selectYearPos = 0;
    int selectMonthPos = -1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);
        emptyDataTv = findViewById(R.id.history_tv_empty);

        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);
        historyLv.setAdapter(adapter);
        historyLv.setEmptyView(emptyDataTv);

        // 获取当前时间信息
        initTime();
        timeTv.setText(year + "年" + String.format("%02d", month) + "月");

        // 获取数据源
        loadData(year, month);

        // 给listView添加长按事件，长按删除记录
        historyLv.setOnItemLongClickListener(((parent, view, position, id) -> {
            Account account = mDatas.get(position);
            showDeleteItemDialog(account);
            return false;
        }));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDeleteItemDialog(Account account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog cancelItemDialog = builder.setMessage("确定要删除这条账单吗？删除后不可恢复")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    // 确定删除
                    // 从数据库中删除记录
                    UniteApp.getExecutorService().execute(() -> DBManager.delectAccount(account));
                    // 从数据源中删除记录
                    mDatas.remove(account);
                    adapter.notifyDataSetChanged(); // 更新listView中的信息
                }).create();
        cancelItemDialog.setOnShowListener((dialog) -> {
            Button positiveBtn = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeBtn = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveBtn.setBackground(getResources().getDrawable(R.drawable.dialog_ensurebtn_bg));
            positiveBtn.setTextColor(Color.BLACK);
            negativeBtn.setTextColor(Color.BLACK);
        });
        cancelItemDialog.show();
    }

    private void loadData(int year, int month) {
        List<Account> list = null;
        try {
            list = UniteApp.getExecutorService().submit(() -> DBManager.getMonthlyAccounts(year, month)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mDatas.clear();
        mDatas.addAll(list);
        if (mDatas.size() > 0) {
            //historyLv.setVisibility(View.VISIBLE);
            //emptyDataTv.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        year = localDateTime.getYear();
        month = localDateTime.getMonthValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.history_iv_calendar:
                CalendarDialog dialog = new CalendarDialog(this, selectYearPos, selectMonthPos);
                dialog.show();
                dialog.setDialogSize();
                dialog.setOnRefreshListener((selPos, year, month) -> {
                    timeTv.setText(year + "年" + String.format("%02d", month) + "月");
                    loadData(year, month);
                    selectYearPos = selPos;
                    selectMonthPos = month - 1;
                });
                break;
        }
    }
}