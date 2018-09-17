package com.lilee.that.base;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lilee.pluginlib.AppConstants;
import com.lilee.pluginlib.IRemoteService;

import java.io.File;
import java.lang.reflect.Constructor;

import dalvik.system.DexClassLoader;

public class ProxyService extends Service {

    private String mClass;
    private String dexPath;
    private ClassLoader dexClassLoader;
    private IRemoteService remoteService;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        dexPath = intent.getStringExtra(AppConstants.EXTRA_DEX_PATH);
        mClass = intent.getStringExtra(AppConstants.EXTRA_CLASS);

        loadClassLoader();

        try {
            Class<?> localClass = dexClassLoader.loadClass(mClass);
            Constructor<?> localClassConstructor = localClass.getConstructor(new Class[]{});
            Object o = localClassConstructor.newInstance(new Object[]{});
            remoteService = (IRemoteService) o;
            remoteService.setProxy(this, dexPath);

            remoteService.onCreate();

            onStart(intent, startId);

            return remoteService.onStartCommand(intent, flags, startId);
        } catch (Exception e) {
            e.printStackTrace();
            return super.onStartCommand(intent, flags, startId);
        }

    }

    private void loadClassLoader() {
        File dex = this.getDir("dex", Context.MODE_PRIVATE);
        String dexAbsolutePath = dex.getAbsolutePath();
        dexClassLoader = new DexClassLoader(dexPath, dexAbsolutePath, null, getClassLoader());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        dexPath = intent.getStringExtra(AppConstants.EXTRA_DEX_PATH);
        mClass = intent.getStringExtra(AppConstants.EXTRA_CLASS);

        loadClassLoader();

        try {
            Class<?> localClass = dexClassLoader.loadClass(mClass);
            Constructor<?> localClassConstructor = localClass.getConstructor(new Class[]{});
            Object o = localClassConstructor.newInstance(new Object[]{});
            remoteService = (IRemoteService) o;
            remoteService.setProxy(this, dexPath);
            remoteService.onCreate();
            return remoteService.onBind(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return remoteService.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        remoteService.onDestroy();
    }
}
