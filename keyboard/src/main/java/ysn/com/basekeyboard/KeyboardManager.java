package ysn.com.basekeyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;

import ysn.com.basekeyboard.base.BaseKeyboard;
import ysn.com.basekeyboard.base.BaseKeyboardView;
import ysn.com.basekeyboard.utils.DeviceUtils;

/**
 * @Author yangsanning
 * @ClassName KeyboardManager
 * @Description 一句话概括作用
 * @Date 2020/9/15
 */
public class KeyboardManager {

    protected Context context;
    protected ViewGroup rootView;
    protected BaseKeyboardView keyboardView;
    protected FrameLayout.LayoutParams keyboardLayoutParams;
    protected EditText editText;
    protected BaseKeyboard baseKeyboard;
    protected int scrollY;

    public KeyboardManager(Context context, @LayoutRes int keyboardViewLayoutRes) {
        this.context = context;
        if (this.context instanceof Activity) {
            rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
            keyboardView = (BaseKeyboardView) LayoutInflater.from(this.context).inflate(keyboardViewLayoutRes, null);
            keyboardLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            keyboardLayoutParams.gravity = Gravity.BOTTOM;
        } else {
            throw new IllegalArgumentException("context 必须是 activity");
        }
    }

    /**
     * 绑定输入框和键盘
     */
    public void bind(EditText editText, BaseKeyboard baseKeyboard) {
        this.editText = editText;
        this.baseKeyboard = baseKeyboard;

        initKeyboard();
        initEditText(editText);
    }

    private void initKeyboard() {
        baseKeyboard.setEditText(editText);
        keyboardView.setKeyboard(baseKeyboard);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(baseKeyboard);
    }

    private void initEditText(final EditText editText) {
        DeviceUtils.hideSystemSoftKeyboard(editText);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.requestFocus();
                showSoftKeyboard();
                return true;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    if (hasFocus) {
                        showSoftKeyboard();
                    } else {
                        hideSoftKeyboard();
                    }
                }
            }
        });
    }

    /**
     * 弹出键盘
     */
    public void showSoftKeyboard() {
        if (editText == null || baseKeyboard == null) {
            throw new NullPointerException("请先调用 bind 方法");
        }
        rootView.addOnLayoutChangeListener(onLayoutChangeListener);
        if (rootView.indexOfChild(keyboardView) == -1) {
            rootView.addView(keyboardView, keyboardLayoutParams);
        } else {
            keyboardView.setVisibility(View.VISIBLE);
        }
        keyboardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.keyboard_show));
    }

    /**
     * 收起键盘
     */
    public void hideSoftKeyboard() {
        keyboardView.setVisibility(View.GONE);
        keyboardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.keyboard_hide));
    }

    private final View.OnLayoutChangeListener onLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (keyboardView.getVisibility() == View.GONE) {
                // 键盘隐藏时进行回滚
                rootView.removeOnLayoutChangeListener(onLayoutChangeListener);
                if (scrollY > 0) {
                    rootView.getChildAt(0).scrollBy(0, -scrollY);
                    scrollY = 0;
                }
            } else {
                // 获取显示区域
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);

                int[] etLocation = new int[2];
                BaseKeyboard keyboard = (BaseKeyboard) keyboardView.getKeyboard();
                EditText editText = keyboard.getEditText();
                editText.getLocationOnScreen(etLocation);
                // 计算键盘上方位置，1px 是下划线
                int keyboardTop = etLocation[1] + editText.getHeight()+ 1;

                // 计算需要移动的距离
                int moveY = keyboardTop + keyboardView.getHeight() - rect.bottom;
                // moveY > 0 rootView 需要继续上滑
                if (moveY > 0) {
                    rootView.getChildAt(0).scrollBy(0, moveY);
                    scrollY += moveY;
                } else {
                    int moveBackY = Math.min(scrollY, Math.abs(moveY));
                    if (moveBackY > 0) {
                        rootView.getChildAt(0).scrollBy(0, -1 * moveBackY);
                        scrollY -= moveBackY;
                    }
                }
            }
        }
    };
}
