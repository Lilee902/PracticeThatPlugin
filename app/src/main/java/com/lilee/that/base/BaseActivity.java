package com.lilee.that.base;

import android.app.Activity;
import android.content.Context;

import com.lilee.that.base.bean.PluginItem;
import com.lilee.that.base.utils.ApkCpUtils;
import com.lilee.that.base.utils.DLUtils;

import java.util.HashMap;

public class BaseActivity extends Activity {

    public static final String PLUGIN_A_NAME = "pluginA";
    protected HashMap<String, PluginItem> mPluginItems = new HashMap<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        ApkCpUtils.extractAssets(newBase, PLUGIN_A_NAME + ".apk");

        PluginItem pluginItem = new PluginItem();
        pluginItem.pluginPath = getFileStreamPath(PLUGIN_A_NAME + ".apk").getAbsolutePath();
        pluginItem.packageInfo = DLUtils.getPackageInfo(newBase, pluginItem.pluginPath);

        mPluginItems.put(PLUGIN_A_NAME, pluginItem);
    }
}
