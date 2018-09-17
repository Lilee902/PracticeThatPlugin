package com.lilee.host.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.lilee.host.base.ServiceManager;

public class ProxyService extends Service {
    private static final String TAG = "ProxyService";

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() called");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStart() called with " + "intent = [" + intent + "], startId = [" + startId + "]");

        // 分发Service
        ServiceManager.getInstance().onStartCommand(intent, flags, startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return ServiceManager.getInstance().onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "Service is unbinded");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
    }
}
