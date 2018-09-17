package com.lilee.host;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.lilee.host.base.ApkCpUtils;
import com.lilee.host.base.BaseDexClassLoaderHookHelper;
import com.lilee.host.base.ServiceManager;
import com.lilee.host.hook.AMSHookHelper;
import com.lilee.host.service.R;
import com.lilee.pluginlib.IMyInterface;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MainActivity extends Activity {

    private static final String TAG = "liTag";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

        try {
            String apkName = "pluginB.apk";
            ApkCpUtils.extractAssets(newBase, apkName);

            File dexFile = getFileStreamPath(apkName);
            File optDexFile = getFileStreamPath("pluginB.dex");

            BaseDexClassLoaderHookHelper.patchClassLoader(getClassLoader(), dexFile, optDexFile);

            ServiceManager.getInstance().preLoadService(dexFile);

            AMSHookHelper.hookAMN();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //启动服务
    public void startService(View view) {
        Intent intent = new Intent();
        intent.setComponent(
                new ComponentName("com.lilee.pluginb",
                        "com.lilee.pluginb.TestService"));

        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent();
        intent.setComponent(
                new ComponentName("com.lilee.pluginb",
                        "com.lilee.pluginb.TestService"));

        stopService(intent);
    }

    public void startBindService(View view) {
        Intent intent = new Intent();
        intent.setComponent(
                new ComponentName("com.lilee.pluginb",
                        "com.lilee.pluginb.TestBindService"));
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    public void stopBindService(View view) {
//        Intent intent = new Intent();
//        intent.setComponent(
//                new ComponentName("com.lilee.pluginb",
//                        "com.lilee.pluginb.TestBindService"));
//
//        stopService(intent);

        unbindService(conn);
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            IMyInterface a = (IMyInterface) service;
            int result = a.getCount();
            Log.e(TAG, String.valueOf(result));
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

}
