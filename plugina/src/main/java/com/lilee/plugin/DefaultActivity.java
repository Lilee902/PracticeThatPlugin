package com.lilee.plugin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lilee.pluginlib.AppConstants;
import com.lilee.pluginlib.PluginBaseActivity;

public class DefaultActivity extends PluginBaseActivity {

    private Button btn_goSecondActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        btn_goSecondActivity = (Button) findViewById(R.id.btn_goSecondActivity);
        btn_goSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppConstants.PROXY_VIEW_ACTION);
                intent.putExtra(AppConstants.EXTRA_DEX_PATH, dexPath);
                //com.lilee.plugin.DefaultActivity
                intent.putExtra(AppConstants.EXTRA_CLASS, "com.lilee.plugin.SecondActivity");
                startActivity(intent);
            }
        });
    }

}
