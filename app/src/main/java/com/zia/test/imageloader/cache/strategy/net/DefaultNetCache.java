package com.zia.test.imageloader.cache.strategy.net;

import android.graphics.Bitmap;
import android.util.Log;

import com.zia.test.imageloader.ImageLoader;
import com.zia.test.imageloader.cache.ICache;
import com.zia.test.imageloader.config.IConfig;

import java.io.File;

/**
 * Created by zia on 2018/4/1.
 * 默认的三级缓存，单例模式，全局图片保存在一个LruCache中
 */
public class DefaultNetCache implements ICache {

    private MemoryCache memoryCache;//LruCache缓存
    private DiskCache diskCache;//磁盘缓存
    private NetCache netCache;//从网络下载
    private volatile static DefaultNetCache instance;

    private DefaultNetCache() {
        File cacheDir = ImageLoader.getInstance().getContext().getExternalCacheDir();
        if (cacheDir == null) {
            Log.e(ImageLoader.TAG, "获取缓存目录失败");
            return;
        }
        String filePath = cacheDir.getPath();
        memoryCache = new MemoryCache();
        diskCache = new DiskCache(filePath);
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
        Bitmap bitmap = memoryCache.get(url);//在memory中找到图片
        if (bitmap != null) {
            Log.e("strategy", "find in memory");
            memoryCache.put(url, bitmap);//在LruCache中增加一次计数
            return bitmap;
        }
        bitmap = diskCache.get(url);//从disk加载图片
        if (bitmap != null) {//在disk中找到图片，在LruCache中增加一次计数
            memoryCache.put(url, bitmap);
            Log.e("strategy", "find in disk");
            return bitmap;
        }
        bitmap = netCache.get(url);
        if (bitmap != null) {//从网络中加载
            Log.e("strategy", "download from netWork");
            memoryCache.put(url, bitmap);
            diskCache.put(url, bitmap);
            return bitmap;
        }
        Log.e("strategy", "NetWork is wrong " + url);
        return null;
    }
}
