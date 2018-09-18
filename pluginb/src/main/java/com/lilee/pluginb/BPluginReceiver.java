package com.lilee.pluginb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lilee.pluginlib.IRemoteReceiver;

public class BPluginReceiver extends BroadcastReceiver implements IRemoteReceiver {

    private static final String TAG = "liTag";

    private BroadcastReceiver proxyReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG,"BPluginReceiver get onReceive : " + intent.getStringExtra("info"));
    }

    @Override
    public void setProxy(BroadcastReceiver proxyReceiver) {
        this.proxyReceiver = proxyReceiver;
    }
}