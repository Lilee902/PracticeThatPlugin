package com.lilee.pluginb;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lilee.pluginlib.IMyInterface;
import com.lilee.pluginlib.PluginBaseService;

public class TestBindService extends PluginBaseService{

    private MyBinder binder = new MyBinder();
    private int count;

    public class MyBinder extends Binder implements IMyInterface {
        public int getCount() {
            return count;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG,"TestBindService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG,"TestBindService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG,"TestBindService onBind");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG,"TestBindService onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG,"TestBindService onDestroy");
        super.onDestroy();
    }

}
