package com.moka.call.ui.passport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.moka.call.R;
import com.moka.call.ui.BaseAppCompatActivity;
import com.moka.call.utils.UiHelper;

/**
 * Created by su on 2017/10/14.
 */

public class LoginActivity extends BaseAppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UiHelper.translucentStatusBar(this);
        UiHelper.translucentNavigationBar(this);

        TextView inputHintView = (TextView) findViewById(R.id.input_hint);
        inputHintView.setText(Html.fromHtml("输入<font color=\"#FFEA9F\">13488688260</font>收到的4位验证码"));
    }
}
