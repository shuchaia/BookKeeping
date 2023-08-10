package com.example.bookkeeping.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bookkeeping.R;
import com.example.bookkeeping.UniteApp;
import com.example.bookkeeping.db.DBManager;
import com.example.bookkeeping.utils.KeyBoardUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

public class BudgeEditDialog extends Dialog {
    // 设置预算对话框中的控件
    EditText dialogBudgetEt;

    onEnsureListener onEnsureListener;

    Activity activity;

    @Override
    protected void onStart() {
        super.onStart();
        dialogBudgetEt.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                KeyBoardUtils.showSoftInput(activity);
            }
        }, 200);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dialogBudgetEt.clearFocus();
        KeyBoardUtils.closeSoftInput(activity);
    }

    public interface onEnsureListener {
        void onEnsure(float money);

    }

    public void setOnEnsureListener(BudgeEditDialog.onEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public BudgeEditDialog(@NonNull Context context) {
        super(context);
        activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);
        dialogBudgetEt = findViewById(R.id.dialog_budget);
        Button ensureBtn = findViewById(R.id.dialog_budget_btn_ensure);
        Button cancelBtn = findViewById(R.id.dialog_budget_btn_cancel);

        ensureBtn.setOnClickListener(v -> {
            String s = dialogBudgetEt.getText().toString().trim();
            float budget = TextUtils.isEmpty(s) ? 0f : Float.parseFloat(s);
            if (budget < 0) {
                Toast.makeText(getContext(), "预算金额不能小于0！", Toast.LENGTH_LONG);
                return;
            }
            if (onEnsureListener != null) {
                onEnsureListener.onEnsure(budget);
            }
            dismiss();
        });

        cancelBtn.setOnClickListener(v -> dismiss());
    }

    public void setDialogSize() {
        Window window = getWindow();

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(layoutParams);
    }
}
