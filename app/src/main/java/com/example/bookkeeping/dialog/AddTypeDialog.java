package com.example.bookkeeping.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.bookkeeping.R;

public class AddTypeDialog extends Dialog {

    EditText typeNameEt;
    Button cancelBtn, ensureBtn;

    onEnsureListener listener;

    public AddTypeDialog(@NonNull Context context) {
        super(context);
    }

    public void setOnEnsureListener(onEnsureListener listener) {
        this.listener = listener;
    }

    public interface onEnsureListener {
        void onEnsure(String typeName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_type);
        typeNameEt = findViewById(R.id.dialog_typename_et);
        cancelBtn = findViewById(R.id.dialog_newtype_btn_cancel);
        ensureBtn = findViewById(R.id.dialog_newtype_btn_ensure);

        cancelBtn.setOnClickListener(v -> dismiss());
        ensureBtn.setOnClickListener(v -> {
            listener.onEnsure(typeNameEt.getText().toString());
            dismiss();
        });
    }

    public void setDialogSize() {
        Window window = getWindow();

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(layoutParams);
    }
}
