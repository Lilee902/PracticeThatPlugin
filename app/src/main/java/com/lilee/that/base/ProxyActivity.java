package com.lilee.that.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.lilee.pluginlib.AppConstants;
import com.lilee.pluginlib.IRemoteActivity;

import java.lang.reflect.Constructor;

public class ProxyActivity extends BaseProxyActivity {

    private static final String TAG = "liTag";
    //remote activity class path
    String mClass;
    IRemoteActivity mRemoteActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDexPath = getIntent().getStringExtra(AppConstants.EXTRA_DEX_PATH);
        mClass = getIntent().getStringExtra(AppConstants.EXTRA_CLASS);

        loadClassLoader();
        loadResources();

        launchTargetActivity(mClass, savedInstanceState);

    }

    private void launchTargetActivity(String mClass, Bundle savedInstanceState) {
        try {


            Class<?> loadClass = dexClassLoader.loadClass(mClass);

            Constructor<?> constructor = loadClass.getConstructor(new Class[]{});
            Object newInstance = constructor.newInstance(new Object[]{});

            mRemoteActivity = (IRemoteActivity) newInstance;
            mRemoteActivity.setProxy(this, mDexPath);

            mRemoteActivity.onCreate(savedInstanceState);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult resultCode=" + resultCode);
        mRemoteActivity.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRemoteActivity.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mRemoteActivity.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRemoteActivity.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRemoteActivity.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRemoteActivity.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRemoteActivity.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRemoteActivity.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mRemoteActivity.onNewIntent(intent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRemoteActivity.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
        mRemoteActivity.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mRemoteActivity.onWindowFocusChanged(hasFocus);
    }
}
