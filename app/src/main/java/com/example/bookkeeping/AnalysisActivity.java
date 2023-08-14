package com.example.bookkeeping;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.entity.Type;
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

    private BarChart barChart;
    private LineChart lineChart;
    private PieChart pieChart;

    //RadioButton outBtn, inBtn;
    RadioGroup radioGroup;
    TextView dateTv;
    TextView monthlyOutTv, monthlyOutputTv, dailyOutputTv, compareTv, lastTv, lineChartTv, pieChartTV;

    // 数据源
    List<BarEntry> barEntries;
    List<Entry> lineEntries;
    List<PieEntry> pieEntries;

    List<Float> monthlyOutput;
    List<Float> monthlyInput;

    private int year;
    private int month;
    private int day;

    ExecutorService executorService;
    // 默认查看支出
    private int kind = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        initTime();
        initView();

        // 造数据
        initData();

        loadDataToChart();
        loadDataToView();
    }

    private void loadDataToView() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        monthlyOutputTv.setText(String.format("%.2f",monthlyOutput.get(month-1)));
        dailyOutputTv.setText(String.format("%.2f",monthlyOutput.get(month-1) / day));
        compareTv.setText(String.format("%.2f",monthlyOutput.get(month-1) - monthlyOutput.get(month-2)));
        lastTv.setText(String.format("%.2f",monthlyInput.get(month-1)-monthlyOutput.get(month-1)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadDataToChart() {
        try {
            // 主线程阻塞1s，等待其他线程加载数据
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 设置柱状图
        BarDataSet barDataSet = new BarDataSet(barEntries, "label");
        barDataSet.setHighLightAlpha(37);
        BarData barData = new BarData((barDataSet));
        barData.setBarWidth(0.5f);
        BarChartUtils.showBarChart(barChart, barData, true);

        // 设置折线图
        LineChartUtils.initChart(lineChart, true, true, false);
        LineChartUtils.setChartData(lineChart, lineEntries, LineDataSet.Mode.LINEAR);

        // 设置饼图
        ArrayList<Integer> colors = initColors();
        PieChartUtils.initPieChart(pieChart);
        PieChartUtils.showRingPieChart(pieChart, pieEntries, colors);
    }

    @NotNull
    private ArrayList<Integer> initColors() {
        String[] colorArray = new String[]{"#5b9bd5","#ed7d31","#70ad47","#ffc000","#4472c4","#91d024",
                "#b235e6","#02ae75","#95a2ff","#fa8080","#ffc076","#fae768","#87e885","#63b2ee",
                "#73abf5","#cb9bff","#434348","#90ed7d","#f7a35c","#8085e9","#05f8d6","#0082fc",
                "#fdd845","#22ed7c","#fa6d1d","#da1f18",};
        ArrayList<Integer> colors = new ArrayList<>();
        for (String colorString : colorArray) {
            colors.add(Color.parseColor(colorString));
        }
        return colors;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        year = localDateTime.getYear();
        month = localDateTime.getMonthValue();
        day = localDateTime.getDayOfMonth();
    }

    private void initView() {
        dateTv = findViewById(R.id.analysis_tv_select_date);
        Drawable drawable = getResources().getDrawable(R.mipmap.down);
        drawable.setBounds(0, 0, 24, 24);
        dateTv.setCompoundDrawables(null, null, drawable, null);
        dateTv.setText(year + "年"+String.format("%02d", month)+"月");

        barChart = findViewById(R.id.analysis_barchart);
        lineChart = findViewById(R.id.analysis_linechart);
        pieChart = findViewById(R.id.analysis_piechart);

        barChart.setNoDataText("暂无数据，去添加一条记录吧~");
        lineChart.setNoDataText("暂无数据，去添加一条记录吧~");
        pieChart.setNoDataText("暂无数据，去添加一条记录吧~");

        monthlyOutTv = findViewById(R.id.analysis_monthly_output);
        monthlyOutputTv = findViewById(R.id.analysis_monthly_output_money);
        dailyOutputTv = findViewById(R.id.analysis_daily_output_money);
        compareTv = findViewById(R.id.analysis_compare_to_last_month);
        lastTv = findViewById(R.id.analysis_input_minus_output);
        lineChartTv = findViewById(R.id.analysis_linechart_tv);
        pieChartTV = findViewById(R.id.analysis_piechart_tv);

        monthlyOutTv.setText(month+"月支出(元)");
        lineChartTv.setText(month+"月支出趋势");
        pieChartTV.setText(month+"月支出分类构成");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initData() {
        /*barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, 30f));
        barEntries.add(new BarEntry(1f, 30f));
        barEntries.add(new BarEntry(2f, 30f));
        barEntries.add(new BarEntry(3f, 30f));
        barEntries.add(new BarEntry(4f, 30f));
        barEntries.add(new BarEntry(5f, 50f));
        barEntries.add(new BarEntry(6f, 50f));

        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(50.0f, "餐饮"));
        pieEntries.add(new PieEntry(50.0f, "购物"));*/

        executorService = UniteApp.getExecutorService();
        // 获得当年所有月份的总支出or收入
        Runnable monthlyMoneyTask = () -> {
            barEntries = new ArrayList<>();
            monthlyOutput = new ArrayList<>();
            monthlyInput = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                float moneyOut = DBManager.getTotalMoney(year, i, 0);
                monthlyOutput.add(moneyOut);
                float moneyIn = DBManager.getTotalMoney(year, i, 1);
                monthlyInput.add(moneyIn);
                if (kind == 0) {
                    barEntries.add(new BarEntry((float) i - 1, moneyOut));
                }else if (kind == 1){
                    barEntries.add(new BarEntry((float) i - 1, moneyIn));
                }
            }
        };

        // 获得当月所有日期的总支出or收入
        Runnable dailyMoneyTask = () -> {
            lineEntries = new ArrayList<>();
            for (int i = 1; i <= TimeUtils.getDays(year, month); i++) {
                float money = DBManager.getTotalMoney(year, month, i, kind);
                lineEntries.add(new Entry((float)i-1, money));
            }
        };

        // 获得当月所有分类的总支出or收入
        Runnable typeMoneyTask = () -> {
            pieEntries = new ArrayList<>();
            List<Type> types = DBManager.getTypeList(kind);
            for (Type type : types) {
                String typeName = type.getTypeName();
                float money = DBManager.getTotalMoney(typeName, year, month, kind);
                if (money > 0){
                    pieEntries.add(new PieEntry(money, typeName));
                }
            }
        };

        // 用多线程执行上述任务
        executorService.execute(monthlyMoneyTask);
        executorService.execute(dailyMoneyTask);
        executorService.execute(typeMoneyTask);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.analysis_iv_back:
                finish();
                break;
            case R.id.analysis_rb_outcome:
                // 查看当月支出分析
                Log.d("kaifa", "switch to output analysis");
                break;
            case R.id.analysis_rb_income:
                // 查看当月收入分析
                Log.d("kaifa", "switch to input analysis");
                break;
            case R.id.analysis_tv_select_date:
                // 弹出calendar对话框
                Log.d("kaifa", "select date");
                break;
        }
    }
}