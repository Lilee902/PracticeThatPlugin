package com.lilee.host;

import android.app.Application;
import android.content.Context;

public class HostApp extends Application{

    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.context = base;
    }

    public static Context getContext() {
        return context;
    }
}
