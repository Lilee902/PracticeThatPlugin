package com.lilee.pluginb;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
        String name = Thread.currentThread().getName();
        Log.v(TAG,"name : " + name);
        Toast.makeText(getProxyService(),"name : " + name , Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG,"onDestroy");
        super.onDestroy();
    }
}
