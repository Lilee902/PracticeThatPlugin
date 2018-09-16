package com.lilee.pluginlib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public interface IRemoteService {

    IBinder onBind(Intent intent);

    void onCreate();

    int onStartCommand(Intent intent, int flags, int startId);

    void onDestroy();

    boolean onUnbind(Intent intent);

    void setProxy(Service proxyService, String dexPath);
}
