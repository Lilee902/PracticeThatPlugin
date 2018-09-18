package com.lilee.pluginlib;

import java.util.HashMap;

import dalvik.system.DexClassLoader;

public class PluginsMap {

    public final static HashMap<String, String> plugins = new HashMap<String, String>();

    public final static HashMap<String,DexClassLoader> classLoaders = new HashMap<>();
}
