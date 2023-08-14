package com.example.bookkeeping.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.List;

/* 饼图工具类
 */
public class PieChartUtils {

    //初始化
    public static void initPieChart(PieChart pieChart) {
        //饼状图
        pieChart.setUsePercentValues(true);//设置value是否用显示百分数,默认为false
        pieChart.getDescription().setEnabled(false);//设置描述
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f);//设置饼状图距离上下左右的偏移量

        //设置中间文字
        pieChart.setDrawCenterText(false);//是否绘制中间的文字

        pieChart.setNoDataText("暂无数据，去添加一条记录吧~");// 如果没有数据的时候，会显示这个，类似ListView的EmptyView

        // 输入标签样式
        pieChart.setEntryLabelColor(Color.GRAY);//设置绘制Label的颜色
        pieChart.setEntryLabelTextSize(13f);//设置绘制Label的字体大小

        pieChart.animateX(500);

        //设置每个tab比例块的显示位置(饼图外字体)
        Legend l = pieChart.getLegend();//设置比例块  饼图外数据的位置
        l.setEnabled(false);
    }

    /**
     * 显示圆环
     * @param
     * @param
     */
    public static void showRingPieChart(PieChart pieChart, List<PieEntry> entries, List<Integer> colors) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(1f);//饼子间距
        dataSet.setColors(colors);
        // 设置描述的位置
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1Length(0.6f);
        // 设置数据的位置
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart2Length(0.6f);
        //设置两根连接线的颜色
        dataSet.setValueLineColor(Color.GRAY);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(11f); //百分比字体的大小
        data.setValueTextColor(Color.GRAY);

        pieChart.setData(data);

        //刷新
        pieChart.invalidate();
        pieChart.animateX(1000);
    }
}




