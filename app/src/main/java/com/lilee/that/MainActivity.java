package com.lilee.that;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lilee.pluginlib.AppConstants;
import com.lilee.pluginlib.IMyInterface;
import com.lilee.that.base.BaseHostActivity;
import com.lilee.that.base.ProxyService;

public class MainActivity extends BaseHostActivity {


    private static final String TAG = "liTag";
    private Button btnPluginA;
    private Button btnPluginB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPluginA = findViewById(R.id.btn_pluginA);
        btnPluginA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppConstants.PROXY_VIEW_ACTION);
                intent.putExtra(AppConstants.EXTRA_DEX_PATH, mPluginItems.get(PLUGIN_A_NAME).pluginPath);
                //com.lilee.plugin.DefaultActivity
                intent.putExtra(AppConstants.EXTRA_CLASS, mPluginItems.get(PLUGIN_A_NAME).packageInfo.packageName + ".DefaultActivity");
                startActivity(intent);
            }
        });

        btnPluginB = findViewById(R.id.btn_pluginB);
        btnPluginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppConstants.PROXY_VIEW_ACTION);
                intent.putExtra(AppConstants.EXTRA_DEX_PATH, mPluginItems.get(PLUGIN_B_NAME).pluginPath);
                //com.lilee.pluginb.MainActivity
                intent.putExtra(AppConstants.EXTRA_CLASS, mPluginItems.get(PLUGIN_B_NAME).packageInfo.packageName + ".MainActivity");
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_startBService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProxyService1.class);
                intent.putExtra(AppConstants.EXTRA_DEX_PATH, mPluginItems.get(PLUGIN_B_NAME).pluginPath);
                //com.lilee.pluginb.TestService
                intent.putExtra(AppConstants.EXTRA_CLASS, mPluginItems.get(PLUGIN_B_NAME).packageInfo.packageName + ".TestService");
                startService(intent);
            }
        });

        findViewById(R.id.btn_stopBService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProxyService1.class);
                intent.putExtra(AppConstants.EXTRA_DEX_PATH, mPluginItems.get(PLUGIN_B_NAME).pluginPath);
                //com.lilee.pluginb.TestService
                intent.putExtra(AppConstants.EXTRA_CLASS, mPluginItems.get(PLUGIN_B_NAME).packageInfo.packageName + ".TestService");
                stopService(intent);
            }
        });


        findViewById(R.id.btn_bindService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProxyService2.class);
                intent.putExtra(AppConstants.EXTRA_DEX_PATH, mPluginItems.get(PLUGIN_B_NAME).pluginPath);
                //com.lilee.pluginb.TestService
                intent.putExtra(AppConstants.EXTRA_CLASS, mPluginItems.get(PLUGIN_B_NAME).packageInfo.packageName + ".TestBindService");
                bindService(intent,conn, Service.BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.btn_unbindService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(conn);
            }
        });

        findViewById(R.id.btn_sendBroadCast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppConstants.PROXY_BROAD_CAST_ACTION);
                intent.putExtra(AppConstants.EXTRA_PLUGIN_NAME,PLUGIN_A_NAME);
                intent.putExtra(AppConstants.EXTRA_CLASS,"com.lilee.plugin.APluginReceiver");
                intent.putExtra("info","this is a message form hostApp");
                sendBroadcast(intent);
            }
        });

        findViewById(R.id.btn_sendBroadCastB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppConstants.PROXY_BROAD_CAST_ACTION);
                intent.putExtra(AppConstants.EXTRA_PLUGIN_NAME,PLUGIN_B_NAME);
                intent.putExtra(AppConstants.EXTRA_CLASS,"com.lilee.pluginb.BPluginReceiver");
                intent.putExtra("info","this is a message form hostApp");
                sendBroadcast(intent);
            }
        });
    }


    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            IMyInterface a = (IMyInterface) service;
            int result = a.getCount();
            Log.e(TAG, "bindCount : "+String.valueOf(result));
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };


}
