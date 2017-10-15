package com.moka.call.ui;

import android.os.Bundle;

/**
 * Created by suzha on 2017/10/14.
 */

public class CommonBaseActivity extends BaseAppCompatActivity {
//    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        trySetupSwipeRefresh();
    }

    private void trySetupSwipeRefresh() {
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
//        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.setColorSchemeResources(
//                    R.color.swipe_color_1, R.color.swipe_color_2,
//                    R.color.swipe_color_3, R.color.swipe_color_4);
//        }
    }

    protected void enableDisableSwipeRefresh(boolean enable) {
//        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.setEnabled(enable);
//        }
    }
}
