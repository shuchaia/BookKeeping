package com.example.bookkeeping.utils;

import android.graphics.Color;
import android.graphics.Matrix;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 柱状图
 */
public class BarChartUtils {
    private static String[] xValueArray = new String[]{"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
    private static List<String> xValueList = new ArrayList<>();

    static {
        for (String s : xValueArray) {
            xValueList.add(s);
        }
    }


    public static void showBarChart(BarChart barChart, BarData barData, boolean isSlither) {
        //设置值显示在柱状图的上边或者下边
        barChart.setDrawValueAboveBar(true);
        //设置绘制网格背景
        barChart.setDrawGridBackground(true);
        //设置双击缩放功能
        barChart.setDoubleTapToZoomEnabled(false);
        //设置规模Y启用
        barChart.setScaleYEnabled(false);
        //设置规模X启用
        barChart.setScaleXEnabled(false);
        //启用设置阻力
        barChart.setScaleEnabled(true);
        //设置每个拖动启用的高亮显示
        barChart.setHighlightPerDragEnabled(false);
        // 设置硬件加速功能
        barChart.setHardwareAccelerationEnabled(true);
        // 设置绘制标记视图
        barChart.setDrawMarkerViews(true);
        // 设置启用日志
        barChart.setLogEnabled(true);
        // 设置拖动减速功能
        barChart.setDragDecelerationEnabled(true);
        barChart.setNoDataText("O__O …");
        // 是否显示表格颜色
        barChart.setDrawGridBackground(false);

        barChart.getDescription().setEnabled(false);


        /**
         * 下面这几个属性你们可以试试 挺有意思的
         * */
        // 设置是否可以触摸
        barChart.setTouchEnabled(isSlither);
        // 是否可以拖拽
        barChart.setDragEnabled(isSlither);//放大可拖拽
        // 是否可以缩放
        barChart.setScaleEnabled(false);
        // 集双指缩放
        barChart.setPinchZoom(false);


        // 设置背景
        barChart.setBackgroundColor(Color.parseColor("#01000000"));
        // 如果打开，背景矩形将出现在已经画好的绘图区域的后边。
        barChart.setDrawGridBackground(false);
        // 集拉杆阴影
        barChart.setDrawBarShadow(false);
        // 图例
        barChart.getLegend().setEnabled(false);
        // 设置数据
        barChart.setData(barData);
        // 隐藏右边的坐标轴 (就是右边的0 - 100 - 200 - 300 ... 和图表中横线)
        barChart.getAxisRight().setEnabled(false);
        // 隐藏左边的坐标轴 (同上)
        // 网格背景颜色
        barChart.setGridBackgroundColor(Color.parseColor("#00000000"));
        // 是否显示表格颜色
        barChart.setDrawGridBackground(false);
        // 设置边框颜色
        barChart.setBorderColor(Color.parseColor("#00000000"));
        // 拉杆阴影
        barChart.setDrawBarShadow(false);
        // 打开或关闭绘制的图表边框。（环绕图表的线）
        barChart.setDrawBorders(false);
        Legend mLegend = barChart.getLegend(); // 设置比例图标示
        // 设置窗体样式
        mLegend.setForm(Legend.LegendForm.CIRCLE);
        // 字体
        mLegend.setFormSize(4f);
        // 字体颜色
        mLegend.setTextColor(Color.parseColor("#00000000"));
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);
        IndexAxisValueFormatter axisValueFormatter = new IndexAxisValueFormatter(xValueArray);
        xAxis.setValueFormatter(axisValueFormatter);
        xAxis.setTextSize(10f);

//        xAxis.setTextColor(0x000000); // 设置轴标签的颜色。
//        xAxis.setTextSize(18); // 设置轴标签的文字大小。
//        xAxis.setTypeface( ) ;// 设置轴标签的 Typeface。
//        xAxis.setGridColor( int color); /// 设置该轴的网格线颜色。
//        xAxis.setGridLineWidth( float width);// 设置该轴网格线的宽度。
//        xAxis.setAxisLineColor( int color); // 设置轴线的轴的颜色。
//        xAxis.setAxisLineWidth( float width);// 设置该轴轴行的宽度。
//        barChart.animateY(1000); // 立即执行的动画,Y轴

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setEnabled(false);

        if (isSlither) {
            //当为true时,放大图
            // 为了使 柱状图成为可滑动的,将水平方向 放大 1.2倍
            barChart.invalidate();
            Matrix mMatrix = new Matrix();
            mMatrix.postScale(1.2f, 1f);
            barChart.getViewPortHandler().refresh(mMatrix, barChart, false);
            barChart.animateY(1000);
        } else {
            //当为false时 不对图进行操作
            barChart.animateY(1000);
        }


    }
}
