package ysn.com.demo.basekeyboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.text.Editable;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import ysn.com.basekeyboard.base.BaseKeyboard;

/**
 * @Author yangsanning
 * @ClassName StockPriceKeyboard
 * @Description 股票价格键盘（扩展出来的键盘）
 * @Date 2020/9/15
 */
public class StockPriceKeyboard extends BaseKeyboard {

    public static final int DEFAULT_NUMBER_XML_LAYOUT = R.xml.keyboard_number;

    private boolean enableDotInput = true;

    public StockPriceKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public StockPriceKeyboard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
    }

    public StockPriceKeyboard(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public StockPriceKeyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    public void setEnableDotInput(boolean enableDotInput) {
        this.enableDotInput = enableDotInput;
    }

    @Override
    public boolean handleSpecialKey(int primaryCode) {
        Editable editable = getEditText().getText();
        int start = getEditText().getSelectionStart();
        //小数点
        if (primaryCode == 46) {
            if (!enableDotInput) {
                return true;
            }
            if (!editable.toString().contains(".")) {
                if (!editable.toString().startsWith(".")) {
                    editable.insert(start, Character.toString((char) primaryCode));
                } else {
                    editable.insert(start, "0" + Character.toString((char) primaryCode));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Drawable getKeyBackground(Keyboard.Key key) {
        if (key.iconPreview != null) {
            return key.iconPreview;
        } else {
            return ContextCompat.getDrawable(context, R.drawable.selector_keyboard);
        }
    }

    @Override
    public Float getKeyTextSize(Keyboard.Key key) {
        return convertSpToPixels(context, 24f);
    }

    @Override
    public Integer getKeyTextColor(Keyboard.Key key) {
        return null;
    }

    @Override
    public CharSequence getKeyLabel(Keyboard.Key key) {
        return null;
    }

    public float convertSpToPixels(Context context, float sp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }
}
