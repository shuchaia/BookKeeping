package com.example.bookkeeping.frag_analysis;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookkeeping.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaseAnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaseAnalysisFragment extends Fragment {

    private BarChart barChart;
    private LineChart lineChart;
    private PieChart pieChart;

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

    public BaseAnalysisFragment(int kind){
        this.kind = kind;
    }


    public BaseAnalysisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_analysis, container, false);
    }
}