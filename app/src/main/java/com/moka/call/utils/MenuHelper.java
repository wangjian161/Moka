package com.moka.call.utils;

import com.moka.call.ui.BaseAppCompatActivity;
import com.moka.call.ui.CommonBaseActivity;
import com.moka.call.ui.html.WebViewActivity;

/**
 * Created by suzha on 2017/10/14.
 */

public class MenuHelper extends CommonBaseActivity {

    private MenuHelper() {}

    public static void popStackIfNeeded(BaseAppCompatActivity activity) {
        int count = activity.getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (activity instanceof WebViewActivity) {
                activity.finish();
            } else {
                activity.onBackPressed();
            }
        } else {
            activity.getSupportFragmentManager().popBackStack();
        }
    }
}
