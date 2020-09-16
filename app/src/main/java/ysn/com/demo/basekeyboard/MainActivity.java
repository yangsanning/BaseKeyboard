package ysn.com.demo.basekeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import ysn.com.basekeyboard.KeyboardManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private KeyboardManager keyboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initKeyboard();

        findViewById(R.id.main_activity_hide).setOnClickListener(this);
    }

    private void initKeyboard() {
        EditText editText = (EditText) findViewById(R.id.main_activity_stock_price);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        StockPriceKeyboard keyboard = new StockPriceKeyboard(this, StockPriceKeyboard.DEFAULT_NUMBER_XML_LAYOUT);
        keyboard.setNextFocusView(findViewById(R.id.main_activity_next));
        keyboard.setEnableDotInput(true);

        keyboardManager = new KeyboardManager(this, R.layout.layout_keyboard_view);
        keyboardManager.bind(editText, keyboard);
    }

    @Override
    public void onClick(View view) {
        keyboardManager.hideSoftKeyboard();
    }
}