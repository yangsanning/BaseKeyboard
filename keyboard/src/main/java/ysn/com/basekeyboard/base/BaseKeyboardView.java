package ysn.com.basekeyboard.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.util.AttributeSet;

import java.util.List;

import ysn.com.basekeyboard.utils.ReflectionUtils;

/**
 * @Author yangsanning
 * @ClassName BaseKeyboardView
 * @Description 键盘控件，进行自定义绘制
 * @Date 2020/9/15
 */
public class BaseKeyboardView extends KeyboardView {

    /**
     * defaultKeyBackground：按键背景
     * labelTextSize：按键字体大小（文本和图标上的字体）
     * keyTextSize：按键字体大小
     * keyTextColor：按键字体颜色
     * shadowRadius：按键字体阴影圆角
     * shadowColor：按键字体阴影颜色
     */
    private Drawable keyBackground;
    private int labelTextSize;
    private int keyTextSize;
    private int keyTextColor;
    private float shadowRadius;
    private int shadowColor;

    private Rect clipRegion;
    private Keyboard.Key invalidatedKey;

    public BaseKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        keyBackground = (Drawable) ReflectionUtils.getFieldValue(this, "mKeyBackground");
        labelTextSize = (int) ReflectionUtils.getFieldValue(this, "mLabelTextSize");
        keyTextSize = (int) ReflectionUtils.getFieldValue(this, "mKeyTextSize");
        keyTextColor = (int) ReflectionUtils.getFieldValue(this, "mKeyTextColor");
        shadowRadius = (float) ReflectionUtils.getFieldValue(this, "mShadowRadius");
        shadowColor = (int) ReflectionUtils.getFieldValue(this, "mShadowColor");
    }

    @Override
    public void onDraw(Canvas canvas) {
        //  keyboard 为空则默认绘制，否则自定义绘制
        if (null == getKeyboard() || !(getKeyboard() instanceof BaseKeyboard)) {
            super.onDraw(canvas);
            return;
        }
        clipRegion = (Rect) ReflectionUtils.getFieldValue(this, "mClipRegion");
        invalidatedKey = (Keyboard.Key) ReflectionUtils.getFieldValue(this, "mInvalidatedKey");
        rewriteOnBufferDraw(canvas);
    }

    /**
     * 对父类的private void onBufferDraw()进行的重写
     * 只是在对key的绘制过程中进行了重新设置.
     */
    private void rewriteOnBufferDraw(Canvas canvas) {
        final Paint paint = (Paint) ReflectionUtils.getFieldValue(this, "mPaint");
        final Rect padding = (Rect) ReflectionUtils.getFieldValue(this, "mPadding");

        paint.setColor(keyTextColor);
        final int kbdPaddingLeft = getPaddingLeft();
        final int kbdPaddingTop = getPaddingTop();

        final Rect clipRegion = this.clipRegion;
        final Keyboard.Key invalidKey = invalidatedKey;
        boolean drawSingleKey = false;
        if (invalidKey != null && canvas.getClipBounds(clipRegion)) {
            // clipRegion是否完全包含在无效的按键中
            if (invalidKey.x + kbdPaddingLeft - 1 <= clipRegion.left &&
                    invalidKey.y + kbdPaddingTop - 1 <= clipRegion.top &&
                    invalidKey.x + invalidKey.width + kbdPaddingLeft + 1 >= clipRegion.right &&
                    invalidKey.y + invalidKey.height + kbdPaddingTop + 1 >= clipRegion.bottom) {
                drawSingleKey = true;
            }
        }

        // 处理按键的自定义属性
        BaseKeyboard keyboard = (BaseKeyboard) getKeyboard();
        List<Keyboard.Key> keys = keyboard.getKeys();
        final int keyCount = keys.size();
        for (int i = 0; i < keyCount; i++) {
            final Keyboard.Key key = keys.get(i);
            if (drawSingleKey && invalidKey != key) {
                continue;
            }

            // 绘制按键背景
            drawKeyBackground(canvas, keyboard, key);

            // 获取为按键自定义的显示文本, 若没有则使用 xml 布局中指定的
            CharSequence keyLabel = keyboard.getKeyLabel(key);
            if (keyLabel == null) {
                keyLabel = key.label;
            }
            // 如果按下 shift 键，则将字符切换为大写
            String label = keyLabel == null ? null : adjustCase(keyLabel).toString();
            if (label != null) {
                // 获取按键的字体大小, 若没有则使用 KeyboardView 的默认属性 keyTextSize 设置
                Float customKeyTextSize = keyboard.getKeyTextSize(key);
                // For characters, use large font. For labels like "Done", use small font.
                if (null != customKeyTextSize) {
                    paint.setTextSize(customKeyTextSize);
                    paint.setTypeface(Typeface.DEFAULT);
                } else {
                    if (label.length() > 1 && key.codes.length < 2) {
                        paint.setTextSize(labelTextSize);
                        paint.setTypeface(Typeface.DEFAULT);
                    } else {
                        paint.setTextSize(keyTextSize);
                        paint.setTypeface(Typeface.DEFAULT);
                    }
                }

                // 获取为按键的字体颜色, 若没有则使用 KeyboardView 的默认属性 keyTextColor 设置
                Integer keyTextColor = keyboard.getKeyTextColor(key);
                if (null != keyTextColor) {
                    paint.setColor(keyTextColor);
                } else {
                    paint.setColor(this.keyTextColor);
                }
                // 绘制文本阴影
                paint.setShadowLayer(shadowRadius, 0, 0, shadowColor);
                // 绘制文本
                canvas.drawText(label,
                        ((key.width - padding.left - padding.right) / 2f + padding.left),
                        ((key.height - padding.top - padding.bottom) / 2f + (paint.getTextSize() - paint.descent()) / 2 + padding.top),
                        paint);
                // Turn off drop shadow
                paint.setShadowLayer(0, 0, 0, 0);
            } else if (key.icon != null) {
                final int drawableX = (key.width - padding.left - padding.right - key.icon.getIntrinsicWidth()) / 2 + padding.left;
                final int drawableY = (key.height - padding.top - padding.bottom - key.icon.getIntrinsicHeight()) / 2 + padding.top;
                canvas.translate(drawableX, drawableY);
                key.icon.setBounds(0, 0, key.icon.getIntrinsicWidth(), key.icon.getIntrinsicHeight());
                key.icon.draw(canvas);
                canvas.translate(-drawableX, -drawableY);
            }
            canvas.translate(-key.x - kbdPaddingLeft, -key.y - kbdPaddingTop);
        }
        invalidatedKey = null;
    }

    /**
     * 绘制按键背景
     */
    private void drawKeyBackground(Canvas canvas, BaseKeyboard keyboard, Keyboard.Key key) {
        // 获取按键自定义的背景, 若没有则使用 KeyboardView 的默认属性 keyBackground 设置
        Drawable keyBackground = keyboard.getKeyBackground(key);
        if (keyBackground == null) {
            keyBackground = this.keyBackground;
        }
        int[] drawableState = key.getCurrentDrawableState();
        keyBackground.setState(drawableState);
        final Rect bounds = keyBackground.getBounds();
        if (key.width != bounds.right || key.height != bounds.bottom) {
            keyBackground.setBounds(0, 0, key.width, key.height);
        }
        canvas.translate(key.x + getPaddingLeft(), key.y + getPaddingTop());
        keyBackground.draw(canvas);
    }

    private CharSequence adjustCase(CharSequence label) {
        if (getKeyboard().isShifted() && label != null && label.length() < 3
                && Character.isLowerCase(label.charAt(0))) {
            label = label.toString().toUpperCase();
        }
        return label;
    }
}

