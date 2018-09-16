package com.lilee.pluginlib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class PluginBaseService extends Service implements IRemoteService{

    protected final static String TAG = "liTag";
    private final static String ClsName = "PluginBaseService";
    private Service proxyService;
    private String dexPath;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG,ClsName + " : onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.v(TAG,ClsName + " : onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG,ClsName + " : onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG,ClsName + " : onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG,ClsName + " : onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void setProxy(Service proxyService, String dexPath) {

        this.proxyService = proxyService;
        this.dexPath = dexPath;
    }


}
