package com.lilee.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lilee.pluginlib.IRemoteReceiver;

public class APluginReceiver extends BroadcastReceiver implements IRemoteReceiver {

    private static final String TAG = "liTag";

    private BroadcastReceiver proxyReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG,"APluginReceiver get onReceive : " + intent.getStringExtra("info"));
    }

    @Override
    public void setProxy(BroadcastReceiver proxyReceiver) {
        this.proxyReceiver = proxyReceiver;
    }
}
