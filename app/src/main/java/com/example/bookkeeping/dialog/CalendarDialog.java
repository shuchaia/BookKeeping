package com.example.bookkeeping.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.bookkeeping.R;
import com.example.bookkeeping.UniteApp;
import com.example.bookkeeping.adapter.CalendarAdapter;
import com.example.bookkeeping.db.DBManager;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CalendarDialog extends Dialog {

    GridView gridView;
    LinearLayout linearLayout;
    ImageView backIv;

    List<TextView> hsvViewList;
    List<Integer> yearList;

    // 表示被点击的year的位置
    // 默认选中今年
    int selectYearPos;
    int selectMonthPos;
    CalendarAdapter adapter;

    public interface OnRefreshListener {
        void onFresh(int selPos, int year, int month);
    }

    OnRefreshListener listener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CalendarDialog(@NonNull @NotNull Context context, int selectYearPos, int selectMonthPos) {
        super(context);
        this.selectYearPos = selectYearPos;
        if (selectMonthPos != -1) {
            this.selectMonthPos = selectMonthPos;
        }else {
            this.selectMonthPos = LocalDateTime.now().getMonthValue() - 1;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        gridView = findViewById(R.id.dialog_calendar_gv);
        linearLayout = findViewById(R.id.dialog_calendar_ll);
        backIv = findViewById(R.id.dialog_calendar_iv_back);

        backIv.setOnClickListener(v -> dismiss());

        // 像横向的scrollView中添加view
        addViewToLayout();

        // 初始化gridview
        initGridView();
        // 设置gv每一个item的点击事件
        setGvOnClickListener();
    }

    private void setGvOnClickListener() {
        gridView.setOnItemClickListener(((parent, view, position, id) -> {
            adapter.selectPos = position;
            adapter.notifyDataSetInvalidated();
            // 获取到被选中的年份和月份 刷新listView
            listener.onFresh(selectYearPos, adapter.year, position + 1);
            dismiss();
        }));
    }

    private void initGridView() {
        Integer year = yearList.get(selectYearPos);
        adapter = new CalendarAdapter(getContext(), year);
        adapter.selectPos = selectMonthPos;
        gridView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addViewToLayout() {
        // 将添加进线性布局中的textView进行统一管理
        hsvViewList = new ArrayList<>();
        yearList = new ArrayList<>();

        List<Integer> list = null;
        try {
            list = UniteApp.getExecutorService().submit(DBManager::getYears).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        yearList.addAll(list);
        if (yearList.size() == 0) {
            int year = LocalDateTime.now().getYear();
            yearList.add(year);
        }

        for (Integer year : yearList) {
            View view = getLayoutInflater().inflate(R.layout.item_dialog_hsv, null);
            linearLayout.addView(view); // 将view添加到布局

            TextView hsvTv = view.findViewById(R.id.item_dialog_hsv_tv);
            hsvTv.setText(year + "");
            hsvViewList.add(hsvTv);
        }

        // 改变selectPos上的textView的样式
        changeTvbg();

        setHSVClickListener(); // 设置HSV中每一个textView的监听时间
    }

    /**
     * 给横向的scrollView中每一个textView设置点击时间
     */
    private void setHSVClickListener() {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView textView = hsvViewList.get(i);
            final int pos = i;
            textView.setOnClickListener(v -> {
                selectYearPos = pos;
                changeTvbg();
                // 更新gridview的数据
                adapter.setYear(yearList.get(selectYearPos));
                // 把selectYearPos传出去
                listener.onFresh(selectYearPos, adapter.year, selectMonthPos + 1);
            });
        }
    }

    // 改变selectPos上的textView的样式
    private void changeTvbg() {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView textView = hsvViewList.get(i);
            if (selectYearPos == i) {
                // 换成被选中的背景
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.drawable.main_recordbtn_bg);
            } else {
                // 设置默认背景
                textView.setTextColor(Color.BLACK);
                textView.setBackgroundResource(R.drawable.dialog_ensurebtn_bg);
            }
        }
    }

    /* 设置Dialog的尺寸和屏幕尺寸一致*/
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void setDialogSize() {
//        获取当前窗口对象
        Window window = getWindow();
//        获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
//        获取屏幕宽度
        // Display d = window.getWindowManager().getDefaultDisplay();
        WindowManager windowManager = window.getWindowManager();
        //wlp.width = (int)(d.getWidth());  //对话框窗口为屏幕窗口
        wlp.width = windowManager.getCurrentWindowMetrics().getBounds().width();
        wlp.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
