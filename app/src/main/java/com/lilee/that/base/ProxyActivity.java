package com.lilee.that.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lilee.pluginlib.AppConstants;
import com.lilee.pluginlib.IRemoteActivity;

import java.lang.reflect.Constructor;

public class ProxyActivity extends BaseHostActivity {

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

            mRemoteActivity.onCreate(new Bundle());

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
}
