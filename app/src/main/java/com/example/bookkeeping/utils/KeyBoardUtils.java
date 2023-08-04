package com.example.bookkeeping.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.bookkeeping.R;

public class KeyBoardUtils {
    private final Keyboard keyboard;
    private KeyboardView keyboardView;
    private EditText editText;

    public interface onEnsureListener{
        public void onEnsure();
    }
    onEnsureListener onEnsureListener;

    public void setOnEnsureListener(KeyBoardUtils.onEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL); // 取消弹出系统键盘
        keyboard = new Keyboard(this.editText.getContext(), R.xml.key);

        this.keyboardView.setKeyboard(keyboard); // 设置要显示的键盘的样式
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener); // 设置键盘按钮点击监听
    }

    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            // primaryCode指ASCII码
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            switch (primaryCode){
                case Keyboard.KEYCODE_DELETE:
                    // 点击删除键
                    if (editable!=null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL:
                    // 点击了清零
                    editable.clear();
                    break;
                case Keyboard.KEYCODE_DONE:
                    // 点击了确认键
                    onEnsureListener.onEnsure(); // 通过接口回调的方法，处理点击确认键的逻辑
                    break;
                default:
                    editable.insert(start, Character.toString((char) primaryCode));
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };


    // 显示键盘
    public void showKeyboard(){
        int visibility = keyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard(){
        int visibility = keyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }
}
