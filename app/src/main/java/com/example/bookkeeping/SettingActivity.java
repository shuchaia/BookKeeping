package com.example.bookkeeping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookkeeping.db.DBManager;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_iv_back:
                finish();
                break;
            case R.id.setting_tv_clearAll:
                showClearDialog();
                Log.d("kaifa", "clicked clearAll");
                break;
        }
    }

    private void showClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.setTitle("警告")
                .setMessage("确定要删除所有记录吗？\n删除后无法恢复，请慎重选择！")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    // 调用数据库进行数据清空
                    UniteApp.getExecutorService().execute(DBManager::clearAllAccounts);
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                }).create();
        alertDialog.show();
    }
}