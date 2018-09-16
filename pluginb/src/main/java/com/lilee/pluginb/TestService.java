package com.lilee.pluginb;

import android.content.Intent;
import android.util.Log;

import com.lilee.pluginlib.PluginBaseService;

public class TestService extends PluginBaseService{

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG,"onDestroy");
        super.onDestroy();
    }
}
