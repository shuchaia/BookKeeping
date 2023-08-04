package com.example.bookkeeping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bookkeeping.dao.TypesDao;
import com.example.bookkeeping.db.TallyDatabase;
import com.example.bookkeeping.entity.Type;

public class MainActivity extends AppCompatActivity {

    TallyDatabase tallyDatabase;
    TypesDao typesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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