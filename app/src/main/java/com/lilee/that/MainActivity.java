package com.lilee.that;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lilee.pluginlib.AppConstants;
import com.lilee.that.base.BaseHostActivity;
import com.lilee.that.base.ProxyService;

public class MainActivity extends BaseHostActivity {

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
                intent.setClass(MainActivity.this, ProxyService.class);
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
                intent.setClass(MainActivity.this, ProxyService.class);
                intent.putExtra(AppConstants.EXTRA_DEX_PATH, mPluginItems.get(PLUGIN_B_NAME).pluginPath);
                //com.lilee.pluginb.TestService
                intent.putExtra(AppConstants.EXTRA_CLASS, mPluginItems.get(PLUGIN_B_NAME).packageInfo.packageName + ".TestService");
                stopService(intent);
            }
        });
    }
}
