package com.zia.test.imageloader.loader;

import android.util.Log;
import android.view.View;

import com.zia.test.imageloader.cache.ICache;
import com.zia.test.imageloader.config.IConfig;

/**
 * Created by zia on 2018/4/2.
 * 驱动抽象类
 */
public abstract class ILoader {

    private static final String TAG = ILoader.class.getSimpleName();
    private ICache cache;
    private IConfig config;

    public abstract void load(Object o, View imageView);

    protected abstract boolean checkSource(Object o);

    ICache getCache() {
        return cache;
    }

    public void setCache(ICache cache) {
        this.cache = cache;
    }

    IConfig getConfig() {
        return config;
    }

    public void setConfig(IConfig config) {
        this.config = config;
    }

    boolean checkEngine() {
        if (cache == null) {
            Log.e(TAG, "cache is null");
            return false;
        }
        if (config == null) {
            Log.e(TAG, "config is null");
            return false;
        }
        return true;
    }
}
