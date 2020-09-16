package ysn.com.basekeyboard.utils;

import android.os.Build;
import android.text.InputType;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * @Author yangsanning
 * @ClassName DeviceUtils
 * @Description 一句话概括作用
 * @Date 2020/9/15
 */
public class DeviceUtils {

    /**
     * 隐藏系统自带键盘
     */
    public static void hideSystemSoftKeyboard(EditText editText) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 11) {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            editText.setInputType(InputType.TYPE_NULL);
        }
    }
}
