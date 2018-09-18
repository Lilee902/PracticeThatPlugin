package com.lilee.pluginlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public interface IRemoteReceiver {
    
    void onReceive(Context context, Intent intent);

    void setProxy(BroadcastReceiver proxyReceiver);
}
