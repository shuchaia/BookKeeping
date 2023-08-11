package com.example.bookkeeping;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookkeeping.adapter.AccountAdapter;
import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.dialog.BudgeEditDialog;
import com.example.bookkeeping.dialog.MoreDialog;
import com.example.bookkeeping.entity.Account;
import com.example.bookkeeping.utils.KeyBoardUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

// TODO 用XPopup代替dialog
// Link: https://github.com/li-xiaojun/XPopup
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // 主页面控件
    ListView todayLv;
    View headView;
    ImageView searchIv;
    ImageButton moreBtn;
    Button editBtn;
    AlertDialog budgetEditDialog;

    // 头布局中的控件
    TextView topOutTv, topInTv, topBudgetTv, topConTv, analysisTv;
    ImageView topShowIv;

    // 数据源
    List<Account> mDatas;
    SharedPreferences sharedPreferences;

    // Adapter
    AccountAdapter adapter;
    private int year, month, dayOfMonth;

    // 多线程
    ExecutorService executorService;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        sharedPreferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        // 添加listview头布局
        addLVHeaderView();
        mDatas = new ArrayList<>();
        // 设置适配器
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        if (budgetEditDialog != null) {
            budgetEditDialog.dismiss();
        }
        super.onDestroy();
    }

    /**
     * 初始化主页面控件
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        searchIv = findViewById(R.id.main_iv_search);
        moreBtn = findViewById(R.id.main_btn_more);
        editBtn = findViewById(R.id.main_btn_edit);

        moreBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);

        // TODO 用SwipeMenuListView优化成侧滑删除
        // Link: https://github.com/baoyongzhang/SwipeMenuListView
        todayLv.setOnItemLongClickListener((parent, view, position, id) -> {
            if (position == 0){
                // 当前被点击的是头布局
                return false;
            }
            int pos = position - 1;
            // 获取被点击的信息
            Account account = mDatas.get(pos);
            // 弹框询问是否要删除
            showDeleteItemDialog(account);
            return true;
        });
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
                    setTopTvShow(); // 更新头布局的信息
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

    /**
     * 给listview添加头布局
     */
    private void addLVHeaderView() {
        // 将布局转换成view对象
        headView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headView); // 将头布局加入到listView中
        // 查找头布局的控件
        topOutTv = headView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headView.findViewById(R.id.item_mainlv_top_tv_in);
        topBudgetTv = headView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headView.findViewById(R.id.item_mainlv_top_iv_hide);
        analysisTv = headView.findViewById(R.id.item_mainlv_top_tv_analysis);

        topBudgetTv.setOnClickListener(this);
        analysisTv.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
    }

    /**
     * 当activity获取焦点时调用
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        LocalDateTime dateTime = LocalDateTime.now();
        executorService = UniteApp.getExecutorService();
        year = dateTime.getYear();
        month = dateTime.getMonthValue();
        dayOfMonth = dateTime.getDayOfMonth();
        loadDBData();

        setTopTvShow();
    }

    /**
     * 设置listview的头布局的数据
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setTopTvShow() {
        // 获取本月的支出情况
        Callable<Float> getMonthlyOutput = () -> DBManager.getTotalMoney(year, month, 0);
        Callable<Float> getMonthlyInput = () -> DBManager.getTotalMoney(year, month, 1);

        // 获取今日收支情况
        Callable<Float> getDailyOutput = () -> DBManager.getTotalMoney(year, month, dayOfMonth, 0);
        Callable<Float> getDailyInout = () -> DBManager.getTotalMoney(year, month, dayOfMonth, 1);

        Float dailyInput = null;
        Float dailyOutput = null;
        Float monthlyInput = null;
        Float monthlyOutput = null;
        try {
            dailyOutput = executorService.submit(getDailyOutput).get();
            dailyInput = executorService.submit(getDailyInout).get();
            monthlyOutput = executorService.submit(getMonthlyOutput).get();
            monthlyInput = executorService.submit(getMonthlyInput).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 显示到view当中
        String infoOneDay = "今日支出 ￥" + dailyOutput + " 收入 ￥" + dailyInput;
        topConTv.setText(infoOneDay);
        topInTv.setText("￥ " + monthlyInput);
        topOutTv.setText("￥ " + monthlyOutput);

        // 设置显示预算剩余
        float money = sharedPreferences.getFloat("money", 0);
        topBudgetTv.setText("￥ "+(money - monthlyOutput));
    }

    /**
     * 加载数据源mData
     */
    private void loadDBData() {
        // 加载数据库信息
        // 获得今天的记录
        Callable<List<Account>> getDailyRecords = () -> DBManager.getAccountsByTime(year, month, dayOfMonth);
        Future<List<Account>> future = executorService.submit(getDailyRecords);
        List<Account> list = null;
        try {
            list = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.main_iv_search:
                // 跳转二级页面 通过分类名或备注模糊查询
                intent = new Intent(this, SearchActivity.class); // 跳转到搜索页面
                break;
            case R.id.main_btn_edit:
                intent = new Intent(this, RecordActivity.class); // 跳转到记录页面
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetEditDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:
                // 切换TextView明文密文显示
                toggleShow();
                break;
            case R.id.item_mainlv_top_tv_analysis:
        }

        if (intent != null){
            startActivity(intent);
        }
    }

    /**
     * 显示设置预算对话框
     */
    private void showBudgetEditDialog() {
        BudgeEditDialog dialog = new BudgeEditDialog(this);
        dialog.setContentView(R.layout.dialog_budget);

        dialog.show();
        dialog.setDialogSize();
        
        dialog.setOnEnsureListener((money) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("money", money);
            editor.commit();

            // 计算剩余预算
            String s = topOutTv.getText().toString().split(" ")[1];
            float monthlyOutput = Float.parseFloat(s);
            float restMoney = money - monthlyOutput;
            topBudgetTv.setText("￥ "+restMoney);
        });
    }

    // true表示显示明文
    boolean isShown = true;

    /**
     * 切换收支及预算信息的显示情况（明文or密文）
     */
    private void toggleShow() {
        TransformationMethod transformationMethod = null;
        if (isShown) {
            // 明文转密文
            transformationMethod = PasswordTransformationMethod.getInstance();
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShown = false;
        } else {
            // 密文转铭文
            transformationMethod = HideReturnsTransformationMethod.getInstance();
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShown = true;
        }
        topInTv.setTransformationMethod(transformationMethod);
        topOutTv.setTransformationMethod(transformationMethod);
        topBudgetTv.setTransformationMethod(transformationMethod);
    }
}