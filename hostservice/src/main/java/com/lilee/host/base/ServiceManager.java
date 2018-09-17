package com.lilee.host.base;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import com.lilee.host.HostApp;
import com.lilee.host.hook.AMSHookHelper;
import com.lilee.host.service.ProxyService;
import com.lilee.pluginlib.RefInvoke;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceManager {

    private static final String TAG = "liTag";

    private ServiceManager() {
    }


    private static class Inner {
        private static ServiceManager serviceManager = new ServiceManager();
    }

    private Map<String, Service> mServiceMap = new HashMap<String, Service>();

    public Map<Object, Intent> mServiceMap2 = new HashMap<Object, Intent>();
    // 存储插件的Service信息
    private Map<ComponentName, ServiceInfo> mServiceInfoMap = new HashMap<ComponentName, ServiceInfo>();

    public static ServiceManager getInstance() {
        return Inner.serviceManager;
    }


    public void preLoadService(File apkFile) throws Exception {
        Object packageParser = RefInvoke.createObject("android.content.pm.PackageParser");

        Object packageObj = RefInvoke.invokeInstanceMethod(packageParser
                , "parsePackage"
                , new Class[]{File.class, int.class}
                , new Object[]{apkFile, PackageManager.GET_SERVICES});

        List services = (List) RefInvoke.getFieldObject(packageObj, "services");

        Class<?> packageParser$ServiceClass = Class.forName("android.content.pm.PackageParser$Service");
        Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");

        int userId = (int) RefInvoke.invokeStaticMethod(
                "android.os.UserHandle", "getCallingUserId");
        Object defaultUserState = RefInvoke.createObject("android.content.pm.PackageUserState");

        for (Object service : services) {
            ServiceInfo info = (ServiceInfo) RefInvoke.invokeInstanceMethod(packageParser, "generateServiceInfo"
                    , new Class[]{packageParser$ServiceClass, int.class, packageUserStateClass, int.class}
                    , new Object[]{service, 0, defaultUserState, userId});

            mServiceInfoMap.put(new ComponentName(info.packageName, info.name), info);
        }

    }

    /**
     * 启动某个插件Service; 如果Service还没有启动, 那么会创建新的插件Service
     *
     * @param proxyIntent
     * @param startId
     */
    public int onStartCommand(Intent proxyIntent, int flags, int startId) {

        Intent targetIntent = proxyIntent.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
        ServiceInfo serviceInfo = selectPluginService(targetIntent);

        try {
            if (!mServiceMap.containsKey(serviceInfo.name)) {
                // service还不存在, 先创建
                proxyCreateService(serviceInfo);
            }

            Service service = mServiceMap.get(serviceInfo.name);
            return service.onStartCommand(targetIntent, flags, startId);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public IBinder onBind(Intent proxyIntent) {
        Intent targetIntent = proxyIntent.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
        ServiceInfo serviceInfo = selectPluginService(targetIntent);
        try {
            if (!mServiceMap.containsKey(serviceInfo.name)) {
                proxyCreateService(serviceInfo);
            }

            Service service = mServiceMap.get(serviceInfo.name);
            return service.onBind(targetIntent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean onUnbind(Intent targetIntent) {
        ServiceInfo serviceInfo = selectPluginService(targetIntent);
        if (serviceInfo == null) {
            Log.w(TAG, "can not found service: " + targetIntent.getComponent());
            return false;
        }
        Service service = mServiceMap.get(serviceInfo.name);
        if (service == null) {
            Log.w(TAG, "can not runnning, are you stopped it multi-times?");
            return false;
        }

        service.onUnbind(targetIntent);

        mServiceMap.remove(serviceInfo.name);

        if (mServiceMap.isEmpty()) {
            // 没有Service了, 这个mServiceMap没有必要存在了
            Log.d(TAG, "service all stopped, stop proxy");
            Context appContext = HostApp.getContext();
            appContext.stopService(
                    new Intent().setComponent(new ComponentName(appContext.getPackageName(), ProxyService.class.getName())));
        }
        return true;
    }


    /**
     * 停止某个插件Service, 当全部的插件Service都停止之后, ProxyService也会停止
     *
     * @param targetIntent
     * @return
     */
    public int stopService(Intent targetIntent) {
        ServiceInfo serviceInfo = selectPluginService(targetIntent);
        if (serviceInfo == null) {
            Log.w(TAG, "can not found service: " + targetIntent.getComponent());
            return 0;
        }
        Service service = mServiceMap.get(serviceInfo.name);
        if (service == null) {
            Log.w(TAG, "can not runnning, are you stopped it multi-times?");
            return 0;
        }

        service.onDestroy();

        mServiceMap.remove(serviceInfo.name);
        if (mServiceMap.isEmpty()) {
            // 没有Service了, 这个mServiceMap没有必要存在了
            Log.d(TAG, "service all stopped, stop proxy");
            Context appContext = HostApp.getContext();
            appContext.stopService(new Intent().setComponent(new ComponentName(appContext.getPackageName(), ProxyService.class.getName())));
        }
        return 1;
    }

    /**
     * 选择匹配的ServiceInfo
     *
     * @param pluginIntent 插件的Intent
     * @return
     */
    private ServiceInfo selectPluginService(Intent pluginIntent) {
        for (ComponentName componentName : mServiceInfoMap.keySet()) {
            if (componentName.equals(pluginIntent.getComponent())) {
                return mServiceInfoMap.get(componentName);
            }
        }
        return null;
    }

    /**
     * 通过ActivityThread的handleCreateService方法创建出Service对象
     *
     * @param serviceInfo 插件的ServiceInfo
     * @throws Exception
     */
    private void proxyCreateService(ServiceInfo serviceInfo) throws Exception {
        IBinder token = new Binder();

        // 创建CreateServiceData对象, 用来传递给ActivityThread的handleCreateService 当作参数
        Object createServiceData = RefInvoke.createObject("android.app.ActivityThread$CreateServiceData");

        // 写入我们创建的createServiceData的token字段, ActivityThread的handleCreateService用这个作为key存储Service
        RefInvoke.setFieldObject(createServiceData, "token", token);

        // 写入info对象
        // 这个修改是为了loadClass的时候, LoadedApk会是主程序的ClassLoader, 我们选择Hook BaseDexClassLoader的方式加载插件
        serviceInfo.applicationInfo.packageName = HostApp.getContext().getPackageName();
        RefInvoke.setFieldObject(createServiceData, "info", serviceInfo);

        // 获取默认的compatibility配置
        Object defaultCompatibility = RefInvoke.getStaticFieldObject("android.content.res.CompatibilityInfo", "DEFAULT_COMPATIBILITY_INFO");
        // 写入compatInfo字段
        RefInvoke.setFieldObject(createServiceData, "compatInfo", defaultCompatibility);


        // private void handleCreateService(CreateServiceData data) {
        Object currentActivityThread = RefInvoke.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread");
        RefInvoke.invokeInstanceMethod(currentActivityThread, "handleCreateService",
                createServiceData.getClass(),
                createServiceData);

        // handleCreateService创建出来的Service对象并没有返回, 而是存储在ActivityThread的mServices字段里面, 这里我们手动把它取出来
        Map mServices = (Map) RefInvoke.getFieldObject(currentActivityThread, "mServices");
        Service service = (Service) mServices.get(token);

        // 获取到之后, 移除这个service, 我们只是借花献佛
        mServices.remove(token);

        // 将此Service存储起来
        mServiceMap.put(serviceInfo.name, service);
    }

}

