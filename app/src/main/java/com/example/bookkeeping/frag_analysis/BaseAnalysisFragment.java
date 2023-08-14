package com.example.bookkeeping.frag_analysis;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookkeeping.R;
import com.example.bookkeeping.UniteApp;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class BaseAnalysisFragment extends Fragment {

    View view;

    private BarChart barChart;
    private LineChart lineChart;
    private PieChart pieChart;

    TextView monthlyOutTv, monthlyOutputTv, dailyOutTv, dailyOutputTv, compareTitleTv, compareTv, lastTv, lineChartTv, pieChartTV;

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

    int kind;

    public BaseAnalysisFragment(int kind){
        this.kind = kind;
    }


    public BaseAnalysisFragment() {
        // Required empty public constructor
    }

    public static BaseAnalysisFragment newInstance() {

        Bundle args = new Bundle();

        BaseAnalysisFragment fragment = new BaseAnalysisFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        view = inflater.inflate(R.layout.fragment_base_analysis, container, false);
        initTime();
        initView();

        // 造数据
        initData();

        loadDataToChart();
        loadDataToView();
        return view;
    }

    private void loadDataToView() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (kind == 0) {
            monthlyOutputTv.setText(String.format("%.2f", monthlyOutput.get(month - 1)));
            dailyOutputTv.setText(String.format("%.2f", monthlyOutput.get(month - 1) / day));
            compareTv.setText(String.format("%.2f", monthlyOutput.get(month - 1) - monthlyOutput.get(month - 2)));
        }else if (kind == 1){
            monthlyOutputTv.setText(String.format("%.2f", monthlyInput.get(month - 1)));
            dailyOutputTv.setText(String.format("%.2f", monthlyInput.get(month - 1) / day));
            compareTv.setText(String.format("%.2f", monthlyInput.get(month - 1) - monthlyInput.get(month - 2)));
        }
        lastTv.setText(String.format("%.2f", monthlyInput.get(month - 1) - monthlyOutput.get(month - 1)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadDataToChart() {
        try {
            // 主线程阻塞1s，等待其他线程加载数据
            Thread.sleep(500);
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

        barChart = view.findViewById(R.id.analysis_barchart);
        lineChart = view.findViewById(R.id.analysis_linechart);
        pieChart = view.findViewById(R.id.analysis_piechart);

        barChart.setNoDataText("暂无数据，去添加一条记录吧~");
        lineChart.setNoDataText("暂无数据，去添加一条记录吧~");
        pieChart.setNoDataText("暂无数据，去添加一条记录吧~");

        monthlyOutTv = view.findViewById(R.id.analysis_monthly_output);
        monthlyOutputTv = view.findViewById(R.id.analysis_monthly_output_money);
        dailyOutTv = view.findViewById(R.id.analysis_daily_output);
        dailyOutputTv = view.findViewById(R.id.analysis_daily_output_money);
        compareTitleTv = view.findViewById(R.id.analysis_compare_tv);
        compareTv = view.findViewById(R.id.analysis_compare_to_last_month);
        lastTv = view.findViewById(R.id.analysis_input_minus_output);
        lineChartTv = view.findViewById(R.id.analysis_linechart_tv);
        pieChartTV = view.findViewById(R.id.analysis_piechart_tv);

        String kindStr = kind == 0 ? "支出":"收入";
        monthlyOutTv.setText(month+"月"+kindStr+"(元)");
        dailyOutTv.setText("日均"+kindStr+"(元)");
        compareTitleTv.setText("比上月"+kindStr+"(元)");
        lineChartTv.setText(month+"月"+kindStr+"趋势");
        pieChartTV.setText(month+"月"+kindStr+"分类构成");
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
}