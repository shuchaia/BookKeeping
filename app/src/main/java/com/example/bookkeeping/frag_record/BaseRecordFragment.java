package com.example.bookkeeping.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookkeeping.R;
import com.example.bookkeeping.UniteApp;
import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.entity.Account;
import com.example.bookkeeping.entity.Type;
import com.example.bookkeeping.utils.KeyBoardUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class BaseRecordFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;
    EditText moneyEt, beizhuEt;
    ImageView typeIv;
    TextView typeTv, timeTv;
    GridView typeGv;

    List<Type> typeList;
    Account account; // 需要插入到数据库中的对象
    // 收入是0，支出是1
    int kind;

    TypeBaseAdapter adapter;

    public BaseRecordFragment(int kind){
        this.kind = kind;
    }

    public BaseRecordFragment() {
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置默认分类
        if (kind == 0){
            account = new Account("其他", R.mipmap.ic_qita_fs);
        }else if (kind == 1){
            account = new Account("其他", R.mipmap.in_qt_fs);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        initTime();
        // 给gridview填充数据
        loadDataToGV();
        setGVListener(); // 设置GV中每一个item的点击事件
        return view;
    }

    /**
     * 设置初始化时间（当前时间），显示在timeTv上
     * todo 用LocalDateTime优化一下
     */
    private void initTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = simpleDateFormat.format(date);
        timeTv.setText(time);
        account.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        account.setYear(year);
        account.setMonth(month);
        account.setDay(day);
    }

    /**
     * 设置GV中每一个item的点击事件
     */
    private void setGVListener() {
        typeGv.setOnItemClickListener(((parent, view, position, id) -> {
            adapter.selectPos = position;
            adapter.notifyDataSetInvalidated();

            // 更新输入框的分类名和图标
            Type type = typeList.get(position);

            typeTv.setText(type.getTypeName());
            account.setTypeName(type.getTypeName());

            typeIv.setImageResource(type.getsImageId());
            account.setsImageId(type.getsImageId());
        }));
    }

    /**
     * 从数据库中加载出数据，填入GV
     */
    private void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
        // 获取数据源
        // 耗时操作放到线程池里
        Callable<List<Type>> callableTask = () -> DBManager.getTypeList(kind);
        Future<List<Type>> future = UniteApp.getExecutorService().submit(callableTask);
        List<Type> list = null;
        try {
            list = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        typeList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuEt = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);

        // 设置显示默认分类
        if (kind == 0) {
            typeIv.setImageResource(R.mipmap.ic_qita_fs);
        }else if (kind == 1){
            typeIv.setImageResource(R.mipmap.in_qt_fs);
        }

        // 显示软键盘
        KeyBoardUtils keyBoardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        keyBoardUtils.showKeyboard();

        // 设置接口监听
        keyBoardUtils.setOnEnsureListener(()->{
            // 获取当前输入框中输入的金额
            String moneyStr = moneyEt.getText().toString();
            if (!TextUtils.isEmpty(moneyStr)) {
                getActivity().finish();
            }
            float money = Float.parseFloat(moneyStr);
            account.setMoney(money);
            account.setKind(kind);
            String beizhu = beizhuEt.getText().toString().trim();
            if (!TextUtils.isEmpty(beizhu)) {
                account.setBeizhu(beizhu);
            }
            // 获取记录的信息，保存在数据库当中
            ExecutorService executorService = UniteApp.getExecutorService();
            Runnable runnableTask = () -> DBManager.tallyDatabase.accountsDao().insertAccounts(account);
            executorService.execute(runnableTask);

            // 返回上一级页面
            getActivity().finish();
        });

        beizhuEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 把软键盘收起来
                    keyBoardUtils.hideKeyboard();
                    // 将布局贴近屏幕底部
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    RelativeLayout rl = view.findViewById(R.id.frag_record_rl);
                    rl.setLayoutParams(params);
                }else {
                    keyBoardUtils.showKeyboard();
                    // 将布局置于软键盘之上
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ABOVE, R.id.frag_record_keyboard);
                    RelativeLayout rl = view.findViewById(R.id.frag_record_rl);
                    rl.setLayoutParams(params);
                }
            }
        });

        // 给时间设置点击时间
        // 通过点击弹出时间框，修改记录的时间
        timeTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_record_tv_time:
                break;
        }
    }
}