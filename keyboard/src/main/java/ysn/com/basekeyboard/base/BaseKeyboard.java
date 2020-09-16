package ysn.com.basekeyboard.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.IntegerRes;

import ysn.com.basekeyboard.R;

/**
 * @Author yangsanning
 * @ClassName BaseKeyboard
 * @Description 键盘基类
 * @Date 2020/9/15
 */
public abstract class BaseKeyboard extends Keyboard implements KeyboardView.OnKeyboardActionListener {

    protected Context context;

    private EditText editText;
    private View nextFocusView;

    public BaseKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
        this.context = context;
    }

    public BaseKeyboard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
        this.context = context;
    }

    public BaseKeyboard(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
        this.context = context;
    }

    public BaseKeyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
        this.context = context;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        // 这里进行自定义按键处理
        if (null != editText && editText.hasFocus() && !handleSpecialKey(primaryCode)) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            int end = editText.getSelectionEnd();
            if (end > start) {
                editable.delete(start, end);
            }

            // 删除键，保持跟系统相同
            if (primaryCode == KEYCODE_DELETE) {
                if (!TextUtils.isEmpty(editable)) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == getKeyCode(R.integer.keyboard_next_focus)) {
                // 切换到下一个控件
                nextFocus();
            } else {
                // 不处理的默认是插入事件
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    }

    public int getKeyCode(@IntegerRes int redId) {
        return context.getResources().getInteger(redId);
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

    /**
     * 获取按键背景
     */
    public abstract Drawable getKeyBackground(Keyboard.Key key);

    /**
     * 获取按键要显示的文本
     */
    public abstract CharSequence getKeyLabel(Key key);

    /**
     * 获取按键文本字体大小
     */
    public abstract Float getKeyTextSize(Key key);

    /**
     * 获取按键文本颜色
     */
    public abstract Integer getKeyTextColor(Key key);

    /**
     * 用于处理自定义按键
     *
     * @return 如果处理了返回 true，反之 false
     */
    public abstract boolean handleSpecialKey(int primaryCode);

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setNextFocusView(View nextFocusView) {
        this.nextFocusView = nextFocusView;
    }

    public View getNextFocusView() {
        return nextFocusView;
    }

    public void nextFocus() {
        if (nextFocusView != null) {
            nextFocusView.requestFocus();
        }
    }
}
