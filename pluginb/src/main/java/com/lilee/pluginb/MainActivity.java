package com.lilee.pluginb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lilee.pluginlib.AppConstants;
import com.lilee.pluginlib.PluginBaseActivity;
import com.lilee.pluginlib.PluginsMap;

public class MainActivity extends PluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_PluginA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pluginADexPath = PluginsMap.plugins.get("pluginA");

                Intent intent = new Intent(AppConstants.PROXY_VIEW_ACTION);
                intent.putExtra(AppConstants.EXTRA_DEX_PATH, pluginADexPath);
                intent.putExtra(AppConstants.EXTRA_CLASS, "com.lilee.plugin.DefaultActivity");
                startActivity(intent);
            }
        });
    }
}
