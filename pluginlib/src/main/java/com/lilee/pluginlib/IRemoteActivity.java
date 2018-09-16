package com.lilee.pluginlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public interface IRemoteActivity {
    void onStart();

    void onRestart();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onCreate(Bundle savedInstanceState);

    void setProxy(Activity proxyActivity, String dexPath);

    void onSaveInstanceState(Bundle outState);

    void onNewIntent(Intent intent);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void onWindowAttributesChanged(WindowManager.LayoutParams params);

    void onWindowFocusChanged(boolean hasFocus);
}
