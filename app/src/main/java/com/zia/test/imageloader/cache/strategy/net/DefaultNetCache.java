package com.zia.test.imageloader.cache.strategy.net;

import android.graphics.Bitmap;
import android.util.Log;

import com.zia.test.imageloader.cache.ICache;
import com.zia.test.imageloader.config.IConfig;

/**
 * Created by zia on 2018/4/1.
 * 默认的三级缓存，单例模式，全局图片保存在一个LruCache中
 */
public class DefaultNetCache extends ICache {

    private MemoryCache memoryCache;//LruCache缓存
    private DiskCache diskCache;//磁盘缓存
    private NetCache netCache;//从网络下载
    private volatile static DefaultNetCache instance;

    private DefaultNetCache() {
        memoryCache = new MemoryCache((int) (Runtime.getRuntime().maxMemory()) / 2);
        diskCache = new DiskCache();
        netCache = new NetCache();
    }

    public static DefaultNetCache getInstance() {
        if (instance == null) {
            synchronized (DefaultNetCache.class) {
                if (instance == null) {
                    instance = new DefaultNetCache();
                }
            }
        }
        return instance;
    }

    @Override
    public void setConfig(IConfig config) {
        super.setConfig(config);
        memoryCache.setConfig(getConfig());
        diskCache.setConfig(getConfig());
        netCache.setConfig(getConfig());
    }

    @Override
    public void put(String url, Bitmap bitmap) {

    }

    /**
     * 三级缓存策略
     *
     * @param url
     * @return
     */
    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap == null) {
            bitmap = diskCache.get(url);
        } else {//在memory中找到图片
            Log.e("strategy", "find in memory");
            memoryCache.put(url, bitmap);//在LruCache中增加一次计数
            return bitmap;
        }
        if (bitmap == null) {
            bitmap = netCache.get(url);
            if (bitmap == null) {
                Log.e("strategy", "NetWork is wrong " + url);
                return null;
            }
            Log.e("strategy", "download from netWork");
            memoryCache.put(url, bitmap);
            diskCache.put(url, bitmap);
        } else {//在disk中找到图片，在LruCache中增加一次计数
            memoryCache.put(url, bitmap);
            Log.e("strategy", "find in disk");
            return bitmap;
        }
        return bitmap;
    }
}
