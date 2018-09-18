package com.lilee.that.base;

import android.app.Activity;
import android.content.Context;

import com.lilee.pluginlib.PluginsMap;
import com.lilee.that.base.bean.PluginItem;
import com.lilee.that.base.utils.ApkCpUtils;
import com.lilee.that.base.utils.DLUtils;

import java.io.File;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

public class BaseHostActivity extends Activity {

    public static final String PLUGIN_A_NAME = "pluginA";
    public static final String PLUGIN_B_NAME = "pluginB";
    protected HashMap<String, PluginItem> mPluginItems = new HashMap<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

        ApkCpUtils.extractAssets(newBase, PLUGIN_A_NAME + ".apk");

        PluginItem pluginItem = new PluginItem();
        pluginItem.pluginPath = getFileStreamPath(PLUGIN_A_NAME + ".apk").getAbsolutePath();
        pluginItem.packageInfo = DLUtils.getPackageInfo(newBase, pluginItem.pluginPath);

        String dexPath = pluginItem.pluginPath;
        File dexOutputDir = this.getDir("dex", Context.MODE_PRIVATE);
        String dexOutputPath = dexOutputDir.getAbsolutePath();
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, dexOutputPath
                , null, getClassLoader());

        mPluginItems.put(PLUGIN_A_NAME, pluginItem);
        PluginsMap.plugins.put(PLUGIN_A_NAME, pluginItem.pluginPath);
        PluginsMap.classLoaders.put(PLUGIN_A_NAME,dexClassLoader);


        ApkCpUtils.extractAssets(newBase, PLUGIN_B_NAME + ".apk");

        PluginItem pluginItemB = new PluginItem();
        pluginItemB.pluginPath = getFileStreamPath(PLUGIN_B_NAME + ".apk").getAbsolutePath();
        pluginItemB.packageInfo = DLUtils.getPackageInfo(newBase, pluginItemB.pluginPath);

        String dexPathB = pluginItemB.pluginPath;
        File dexOutputDirB = this.getDir("dex", Context.MODE_PRIVATE);
        String dexOutputPathB = dexOutputDirB.getAbsolutePath();
        DexClassLoader dexClassLoaderB = new DexClassLoader(dexPathB, dexOutputPathB
                , null, getClassLoader());

        mPluginItems.put(PLUGIN_B_NAME, pluginItemB);
        PluginsMap.plugins.put(PLUGIN_B_NAME, pluginItemB.pluginPath);
        PluginsMap.classLoaders.put(PLUGIN_B_NAME,dexClassLoaderB);
    }
}
