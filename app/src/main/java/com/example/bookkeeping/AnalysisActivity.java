package com.example.bookkeeping;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.dialog.CalendarDialog;
import com.example.bookkeeping.entity.Type;
import com.example.bookkeeping.frag_analysis.BaseAnalysisFragment;
import com.example.bookkeeping.frag_analysis.IncomeFragment;
import com.example.bookkeeping.frag_analysis.OutcomeFragment;
import com.example.bookkeeping.utils.BarChartUtils;
import com.example.bookkeeping.utils.LineChartUtils;
import com.example.bookkeeping.utils.PieChartUtils;
import com.example.bookkeeping.utils.TimeUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class AnalysisActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    TextView dateTv;
    Fragment curFragment;

    private int year;
    private int month;
    private int day;
    int selectYearPos = 0;
    int selectMonthPos = -1;
    int mIndex = 0;

    Bundle bundle;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        initTime();
        initView();
        changeFragment(OutcomeFragment.newInstance(bundle));
    }

    private void changeFragment(Fragment fm) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.analysis_content, fm);
        transaction.commit();
        curFragment = fm;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        year = localDateTime.getYear();
        month = localDateTime.getMonthValue();
        day = localDateTime.getDayOfMonth();
        bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        bundle.putInt("day", day);
    }

    private void initView() {
        dateTv = findViewById(R.id.analysis_tv_select_date);
        Drawable drawable = getResources().getDrawable(R.mipmap.down);
        drawable.setBounds(0, 0, 24, 24);
        dateTv.setCompoundDrawables(null, null, drawable, null);
        dateTv.setText(year + "年"+String.format("%02d", month)+"月");

        radioGroup = findViewById(R.id.analysis_rg);
        radioGroup.setOnCheckedChangeListener((group, arg1) -> {
            // 遍历radioGroup的子控件
            for (int i = 0; i < group.getChildCount(); i++) {
                RadioButton rb = (RadioButton) group.getChildAt(i);
                if (rb.isChecked()){
                    mIndex = i;
                    setIndexSelected(i);
                    break;
                }
            }
        });
    }

    private void setIndexSelected(int index) {
        switch (index) {
            case 0:
                changeFragment(OutcomeFragment.newInstance(bundle));
                break;
            case 1:
                changeFragment(IncomeFragment.newInstance(bundle));
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.analysis_iv_back:
                finish();
                break;
            case R.id.analysis_tv_select_date:
                //  弹出calendar对话框
                Log.d("kaifa", "select date");
                CalendarDialog dialog = new CalendarDialog(this, selectYearPos, selectMonthPos);
                dialog.show();
                dialog.setDialogSize();
                dialog.setOnRefreshListener((selPos, year, month) -> {
                    dateTv.setText(year + "年" + String.format("%02d", month) + "月");
                    bundle.putInt("year", year);
                    bundle.putInt("month", month);
                    setIndexSelected(mIndex);
                    selectYearPos = selPos;
                    selectMonthPos = month - 1;
                });
                break;
        }
    }
}