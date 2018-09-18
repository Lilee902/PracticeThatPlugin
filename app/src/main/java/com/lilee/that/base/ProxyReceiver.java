package com.lilee.that.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lilee.pluginlib.AppConstants;
import com.lilee.pluginlib.IRemoteReceiver;
import com.lilee.pluginlib.PluginsMap;

import java.lang.reflect.Constructor;

import dalvik.system.DexClassLoader;

public class ProxyReceiver extends BroadcastReceiver {

    private static final String TAG = "liTag";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "ProxyReceiver onReceive");

        String pluginName = intent.getStringExtra(AppConstants.EXTRA_PLUGIN_NAME);
        String mClass = intent.getStringExtra(AppConstants.EXTRA_CLASS);
        try {
            DexClassLoader dexClassLoader = PluginsMap.classLoaders.get(pluginName);
            Class<?> loadClass = dexClassLoader.loadClass(mClass);
            Constructor<?> classConstructor = loadClass.getConstructor(new Class[]{});
            Object o = classConstructor.newInstance(new Object[]{});

            IRemoteReceiver remoteReceiver = (IRemoteReceiver) o;
            remoteReceiver.setProxy(this);
            remoteReceiver.onReceive(context,intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
