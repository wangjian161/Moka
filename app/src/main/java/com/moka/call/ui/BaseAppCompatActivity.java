package com.moka.call.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.moka.call.R;
import com.moka.call.component.permission.OnPermissionCheckListener;
import com.moka.call.component.permission.PermissionProcessor;
import com.moka.call.component.permission.PermissionRequest;
import com.moka.call.utils.MenuHelper;

/**
 * Created by su on 17-10-13.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements PermissionRequest {

    protected Toolbar mToolbar;

    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void setTitle(int titleRes) {
        setTitle(getString(titleRes));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupToolbar();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        //抢标页面/转让页面等没有toolbar
        if (mToolbar != null) {
//            mToolbar.inflateMenu(R.menu.main_menu);
//            prepareOptionsMenu(mToolbar.getMenu());
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MenuHelper.popStackIfNeeded(BaseAppCompatActivity.this);
                }
            });
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mToolbar != null) {
                mToolbar.showOverflowMenu();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void permissionRequest(Activity activity, int requestCode, String permission) {
        PermissionProcessor.getInstance().permissionRequest(activity, requestCode, permission);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionProcessor.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    protected void putOnPermissionCheckListener(String permission, OnPermissionCheckListener l) {
        PermissionProcessor.getInstance().putOnPermissionCheckListener(permission, l);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
